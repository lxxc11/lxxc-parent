package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.converter.SysDataSetConverter;
import com.lvxc.user.domain.dto.IndexDto;
import com.lvxc.user.domain.dto.SysDataSetDto;
import com.lvxc.user.domain.vo.LoginDataSet;
import com.lvxc.user.domain.vo.SysDataSetIndexVo;
import com.lvxc.user.domain.vo.SysDataSetVo;
import com.lvxc.user.entity.SysDataSet;
import com.lvxc.user.entity.SysDataSetIndex;
import com.lvxc.user.entity.SysDict;
import com.lvxc.user.entity.SysMenu;
import com.lvxc.user.mapper.flw.SysDataSetMapper;
import com.lvxc.user.mapper.flw.SysMenuMapper;
import com.lvxc.user.service.ISysDataSetIndexService;
import com.lvxc.user.service.ISysDataSetService;
import com.lvxc.user.service.ISysDictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDataSetServiceImpl extends ServiceImpl<SysDataSetMapper, SysDataSet> implements ISysDataSetService {

    @Autowired
    private ISysDataSetIndexService sysDataSetIndexService;

    @Autowired
    private ISysDictService sysDictService;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysDataSetConverter sysDataSetConverter;

    @Override
    public List<SysDataSetVo> getList(String menuId) {
        LambdaQueryWrapper<SysDataSet> query = new LambdaQueryWrapper<>();
        query.eq(SysDataSet::getDelFlag, false);
        query.eq(SysDataSet::getMenuId, menuId);
        query.orderByDesc(SysDataSet::getUpdateTime);
        List<SysDataSet> allList = list(query);
        List<SysDataSetVo> dataSetList = sysDataSetConverter.sysDataSetListDaoToVo(allList);
        SysMenu sysMenu = sysMenuMapper.selectById(menuId);
        dataSetList.forEach(e -> e.setMenuName(sysMenu.getMenuName()));
        // 返回树级结构指标
        List<String> dataSetIds = dataSetList.stream().map(SysDataSetVo::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(dataSetIds)) {
            LambdaQueryWrapper<SysDataSetIndex> queryIndex = new LambdaQueryWrapper<>();
            queryIndex.eq(SysDataSetIndex::getDelFlag, false);
            queryIndex.in(SysDataSetIndex::getDataSetId, dataSetIds);
            queryIndex.orderByAsc(SysDataSetIndex::getSortOrder).orderByDesc(SysDataSetIndex::getUpdateTime);
            List<SysDataSetIndex> sysDataSetIndices = sysDataSetIndexService.list(queryIndex);
            if (!CollectionUtils.isEmpty(sysDataSetIndices)) {
                List<SysDataSetIndexVo> dataSetIndices = sysDataSetConverter.sysDataSetIndexDaoToVo(sysDataSetIndices);
                Map<String, List<SysDataSetIndexVo>> dataSetIndexMap = dataSetIndices.stream().collect(Collectors.groupingBy(SysDataSetIndexVo::getDataSetId));
                for (SysDataSetVo sysDataSetVo : dataSetList) {
                    if (dataSetIndexMap.containsKey(sysDataSetVo.getId())) {
                        List<SysDataSetIndexVo> indexList = dataSetIndexMap.get(sysDataSetVo.getId());
                        Map<String, List<SysDataSetIndexVo>> indexMap = indexList.stream().collect(Collectors.groupingBy(SysDataSetIndexVo::getParentId));
                        List<SysDataSetIndexVo> treeMenuList = indexList.stream().filter(e -> StringUtils.equals(sysDataSetVo.getContentId(), "0") ? StringUtils.equals(e.getParentId(), "0") : StringUtils.equals(e.getParentId(), sysDataSetVo.getContentId())).collect(Collectors.toList());
                        getTreeIndexList(indexMap, treeMenuList, StringUtils.equals(sysDataSetVo.getContentId(), "0") ? false : true);
                        sysDataSetVo.setIndexList(treeMenuList);
                    }
                }
            }
        }
        return dataSetList;
    }

    @Override
    @Transactional
    public ResponseResult add(SysDataSetDto dto) {
        // 添加校验
        ResponseResult result = check(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        SysDataSet sysDataSet = sysDataSetConverter.sysDataSetDtoToDao(dto);
        save(sysDataSet);
        // 数据集指标添加
        if (!StringUtils.equals(dto.getContentId(), "0") && !CollectionUtils.isEmpty(dto.getIndexIds())) {
            List<SysDataSetIndexVo> treeMenuList = addDataSetIndex(dto, sysDataSet);
            return ResponseResult.success("新增成功！", treeMenuList);
        }
        return ResponseResult.success("新增成功！");
    }

    @Override
    @Transactional
    public ResponseResult edit(SysDataSetDto dto) {
        // 修改校验
        ResponseResult result = check(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysDataSet sysDataSet = getById(dto.getId());
        sysDataSet.setMenuId(dto.getMenuId());
        sysDataSet.setContent(dto.getContent());
        sysDataSet.setDataName(dto.getDataName());
        sysDataSet.setContentId(dto.getContentId());
        updateById(sysDataSet);
        // 数据集指标修改
        if (!StringUtils.equals(dto.getContentId(), "0") && !CollectionUtils.isEmpty(dto.getIndexIds())) {
            LambdaQueryWrapper<SysDataSetIndex> query = new LambdaQueryWrapper<>();
            query.eq(SysDataSetIndex::getDataSetId, sysDataSet.getId());
            sysDataSetIndexService.remove(query);
            List<SysDataSetIndexVo> treeMenuList = addDataSetIndex(dto, sysDataSet);
            return ResponseResult.success("修改成功！", treeMenuList);
        }
        return ResponseResult.success("修改成功！");
    }

    /**
     * 数据集指标添加
     *
     * @param dto
     * @param sysDataSet
     * @return
     */
    public List<SysDataSetIndexVo> addDataSetIndex(SysDataSetDto dto, SysDataSet sysDataSet) {
        List<SysDict> sysDicts = sysDictService.listByIds(dto.getIndexIds());
        List<SysDataSetIndex> sysDataSetIndices = sysDataSetConverter.sysDictsToSysDataSets(sysDicts);
        sysDataSetIndices.forEach(e -> e.setDataSetId(sysDataSet.getId()));
        sysDataSetIndexService.saveBatch(sysDataSetIndices);
        // 返回树级结构指标
        List<SysDataSetIndexVo> list = sysDataSetConverter.sysDataSetIndexDaoToVo(sysDataSetIndices);
        Map<String, List<SysDataSetIndexVo>> allMap = list.stream().collect(Collectors.groupingBy(SysDataSetIndexVo::getParentId));
        List<SysDataSetIndexVo> treeMenuList = list.stream().filter(e -> StringUtils.equals(e.getParentId(), dto.getContentId())).collect(Collectors.toList());
        getTreeIndexList(allMap, treeMenuList, true);
        return treeMenuList;
    }

    /**
     * 获取指标树形结构
     *
     * @param allMap
     * @param treeMenuList
     */
    @Override
    public void getTreeIndexList(Map<String, List<SysDataSetIndexVo>> allMap, List<SysDataSetIndexVo> treeMenuList, boolean isDict) {
        for (SysDataSetIndexVo sysDataSetIndexVo : treeMenuList) {
            String childrenId = isDict ? sysDataSetIndexVo.getIndexId() : sysDataSetIndexVo.getId();
            if (allMap.containsKey(childrenId)) {
                List<SysDataSetIndexVo> children = allMap.get(childrenId);
                sysDataSetIndexVo.setChildren(children);
                getTreeIndexList(allMap, children, isDict);
            }
        }
    }

    @Override
    public List<LoginDataSet> getDataSetByRoleIds(List<String> roleIds) {
        return baseMapper.getDataSetByRoleIds(roleIds);
    }


    @Override
    @Transactional
    public ResponseResult addIndex(IndexDto dto) {
        // 添加校验
        ResponseResult result = checkIndex(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        SysDataSetIndex dataSetIndex = sysDataSetConverter.sysDataSetIndexDtoToDao(dto);
        sysDataSetIndexService.save(dataSetIndex);
        return ResponseResult.success("新增成功！");
    }

    @Override
    @Transactional
    public ResponseResult editIndex(IndexDto dto) {
        // 修改校验
        ResponseResult result = checkIndex(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysDataSetIndex sysDataSetIndex = sysDataSetIndexService.getById(dto.getId());
        sysDataSetIndex.setDataSetId(dto.getDataSetId());
        sysDataSetIndex.setParentId(dto.getParentId());
        sysDataSetIndex.setKey(dto.getKey());
        sysDataSetIndex.setValue(dto.getLabel());
        sysDataSetIndex.setValue(dto.getValue());
        sysDataSetIndex.setSortOrder(dto.getSortOrder());
        sysDataSetIndexService.updateById(sysDataSetIndex);
        return ResponseResult.success("修改成功！");
    }

    @Override
    @Transactional
    public ResponseResult deleteIndex(String id) {
        SysDataSetIndex sysDataSetIndex = sysDataSetIndexService.getById(id);
        if (sysDataSetIndex == null) {
            return ResponseResult.error("指标不存在！");
        }
        sysDataSetIndexService.removeById(sysDataSetIndex);
        // 删除下级所有指标
        SysDataSet sysDataSet = getById(sysDataSetIndex.getDataSetId());
        LambdaQueryWrapper<SysDataSetIndex> query = new LambdaQueryWrapper<>();
        query.eq(SysDataSetIndex::getDelFlag, false);
        query.eq(SysDataSetIndex::getDataSetId, sysDataSetIndex.getDataSetId());
        List<SysDataSetIndex> allList = sysDataSetIndexService.list(query);
        List<SysDataSetIndex> childrenIndexList = new ArrayList<>();
        getChildrenIndexList(childrenIndexList, allList,
                StringUtils.equals(sysDataSet.getContentId(), "0") ? Arrays.asList(id) : Arrays.asList(sysDataSetIndex.getIndexId()),
                StringUtils.equals(sysDataSet.getContentId(), "0") ? false : true);
        if (!CollectionUtils.isEmpty(childrenIndexList)) {
            sysDataSetIndexService.removeBatchByIds(childrenIndexList);
        }
        return ResponseResult.success("删除成功！");
    }

    /**
     * 根据父级id获取所有下级指标
     *
     * @param allList
     * @param parentIds
     */
    public List<SysDataSetIndex> getChildrenIndexList(List<SysDataSetIndex> childrenIndexList, List<SysDataSetIndex> allList,
                                                      List<String> parentIds, boolean isDict) {
        for (String parentId : parentIds) {
            List<SysDataSetIndex> children = allList.stream().filter(e -> StringUtils.equals(e.getParentId(), parentId)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(children)) {
                childrenIndexList.addAll(children);
                List<String> childrenIds = children.stream().map(e -> isDict ? e.getIndexId() : e.getId()).collect(Collectors.toList());
                getChildrenIndexList(childrenIndexList, allList, childrenIds, isDict);
            }
        }
        return childrenIndexList;
    }

    @Override
    @Transactional
    public ResponseResult delete(String id) {
        SysDataSet sysDataSet = getById(id);
        if (sysDataSet == null) {
            return ResponseResult.error("数据集不存在！");
        }
        sysDataSet.setDelFlag(true);
        updateById(sysDataSet);
        // 删除指标
        LambdaQueryWrapper<SysDataSetIndex> query = new LambdaQueryWrapper<>();
        query.eq(SysDataSetIndex::getDataSetId, sysDataSet.getId());
        sysDataSetIndexService.remove(query);
        return ResponseResult.success("删除成功！");
    }

    public ResponseResult check(SysDataSetDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getMenuId())) {
            return ResponseResult.error("菜单id不能为空！");
        }
        if (StringUtils.isEmpty(dto.getDataName())) {
            return ResponseResult.error("数据集名称不能为空！");
        }
        if (StringUtils.isEmpty(dto.getContentId())) {
            return ResponseResult.error("数据集内容id不能为空！");
        }
        if (StringUtils.isEmpty(dto.getContent())) {
            return ResponseResult.error("数据集内容不能为空！");
        }
        if (!StringUtils.equals(dto.getContentId(), "0") && CollectionUtils.isEmpty(dto.getIndexIds())) {
            return ResponseResult.error("数据集指标不能为空！");
        }

        SysDataSet dataSet = null;
        LambdaQueryWrapper<SysDataSet> query = new LambdaQueryWrapper<>();
        query.eq(SysDataSet::getDelFlag, false);
        query.eq(SysDataSet::getMenuId, dto.getMenuId());
        query.eq(SysDataSet::getDataName, dto.getDataName());
        dataSet = getOne(query);
        if (isAdd) {
            if (dataSet != null) {
                return ResponseResult.error("数据集已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空");
            }
            SysDataSet sysDataSet = getById(dto.getId());
            if (sysDataSet == null) {
                return ResponseResult.error("数据集不存在");
            } else {
                if (dataSet != null && !StringUtils.equals(dataSet.getDataName(), sysDataSet.getDataName())) {
                    return ResponseResult.error("数据集已存在！");
                }
            }
        }
        return ResponseResult.success("校验成功");
    }

    public ResponseResult checkIndex(IndexDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getDataSetId())) {
            return ResponseResult.error("数据集id不能为空！");
        } else {
            SysDataSet sysDataSet = getById(dto.getDataSetId());
            if (sysDataSet == null) {
                return ResponseResult.error("数据集不存在！");
            }
            if (!StringUtils.equals(sysDataSet.getContentId(), "0")) {
                return ResponseResult.error("数据集不是自定义类型，不允许操作指标！");
            }
        }
        if (StringUtils.isEmpty(dto.getParentId())) {
            return ResponseResult.error("父级id不能为空！");
        }
        if (StringUtils.isEmpty(dto.getKey())) {
            return ResponseResult.error("key不能为空！");
        }
        if (StringUtils.isEmpty(dto.getLabel())) {
            return ResponseResult.error("label不能为空！");
        }
        if (StringUtils.isEmpty(dto.getValue())) {
            return ResponseResult.error("value不能为空！");
        }

        List<SysDataSetIndex> dataSetIndexs = null;
        LambdaQueryWrapper<SysDataSetIndex> query = new LambdaQueryWrapper<>();
        query.eq(SysDataSetIndex::getDelFlag, false);
        query.eq(SysDataSetIndex::getDataSetId, dto.getDataSetId());
        query.eq(SysDataSetIndex::getParentId, dto.getParentId());
        query.and(wq -> {
            wq.eq(SysDataSetIndex::getKey, dto.getKey());
            wq.or().eq(SysDataSetIndex::getLabel, dto.getLabel());
            wq.or().eq(SysDataSetIndex::getValue, dto.getValue());
        });
        dataSetIndexs = sysDataSetIndexService.list(query);
        if (isAdd) {
            if (!CollectionUtils.isEmpty(dataSetIndexs)) {
                return ResponseResult.error("指标已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空");
            }
            SysDataSetIndex sysDataSetIndex = sysDataSetIndexService.getById(dto.getId());
            if (sysDataSetIndex == null) {
                return ResponseResult.error("指标不存在");
            } else {
                if (!CollectionUtils.isEmpty(dataSetIndexs)) {
                    if (dataSetIndexs.size() > 1 ||
                            !StringUtils.equals(dataSetIndexs.get(0).getKey(), sysDataSetIndex.getKey()) ||
                            !StringUtils.equals(dataSetIndexs.get(0).getLabel(), sysDataSetIndex.getLabel()) ||
                            !StringUtils.equals(dataSetIndexs.get(0).getValue(), sysDataSetIndex.getValue())) {
                        return ResponseResult.error("指标已存在！");
                    }
                }
            }
        }
        return ResponseResult.success("校验成功");
    }


}
