package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.converter.SysDictConverter;
import com.lvxc.user.domain.dto.SysDictDto;
import com.lvxc.user.domain.vo.DictVo;
import com.lvxc.user.domain.vo.SysDictListVo;
import com.lvxc.user.domain.vo.SysDictPageVo;
import com.lvxc.user.entity.SysDataSet;
import com.lvxc.user.entity.SysDataSetIndex;
import com.lvxc.user.entity.SysDict;
import com.lvxc.user.entity.SysRoleMenuData;
import com.lvxc.user.mapper.flw.SysDataSetIndexMapper;
import com.lvxc.user.mapper.flw.SysDataSetMapper;
import com.lvxc.user.mapper.flw.SysDictMapper;
import com.lvxc.user.mapper.flw.SysRoleMenuDataMappper;
import com.lvxc.user.service.ISysDictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 用户中心-字典表
 * @Author: mengy
 * @Date: 2023-05-04
 * @Version: V1.0
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictConverter sysDictConverter;

    @Resource
    private SysDataSetIndexMapper sysDataSetIndexMapper;

    @Resource
    private SysDataSetMapper sysDataSetMapper;

    @Resource
    private SysRoleMenuDataMappper sysRoleMenuDataMappper;

    @Override
    public IPage<SysDictPageVo> getPageList(Page<SysDict> page, String platformId) {
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        query.eq(SysDict::getDelFlag, false);
        query.eq(SysDict::getPlatformId, platformId);
        query.eq(SysDict::getParentId, "0");
        query.orderByAsc(SysDict::getSortOrder).orderByDesc(SysDict::getUpdateTime);
        Page<SysDict> list = page(page, query);
        Page<SysDictPageVo> pageList = sysDictConverter.sysDictPageDaoToVo(list);
        return pageList;
    }

    @Override
    public List<SysDictListVo> getList(String platformId, String parentId, Boolean status) {
        List<SysDict> allList = getAllList(platformId, status);
        List<SysDictListVo> list = sysDictConverter.sysDictListDaoToVo(allList);
        Map<String, List<SysDictListVo>> allMap = list.stream().collect(Collectors.groupingBy(SysDictListVo::getParentId));
        List<SysDictListVo> treeMenuList = list.stream().filter(e -> StringUtils.equals(e.getParentId(), parentId)).collect(Collectors.toList());
        getTreeDictList(allMap, treeMenuList);
        return treeMenuList;
    }

    @Override
    @Transactional
    public ResponseResult add(SysDictDto dto) {
        // 添加校验
        ResponseResult result = check(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        SysDict sysDict = sysDictConverter.sysDictDtoToDao(dto);
        sysDict.setStatus(dto.getStatus() != null ? dto.getStatus() : true);
        sysDict.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        save(sysDict);
        return ResponseResult.success("新增成功！");
    }

    @Override
    @Transactional
    public ResponseResult edit(SysDictDto dto) {
        // 修改校验
        ResponseResult result = check(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysDict sysDict = getById(dto.getId());
        sysDict.setParentId(dto.getParentId());
        sysDict.setKey(dto.getKey());
        sysDict.setLabel(dto.getLabel());
        sysDict.setValue(dto.getValue());
        sysDict.setStatus(dto.getStatus() != null ? dto.getStatus() : true);
        sysDict.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        updateById(sysDict);
        // 关于停用的相关处理
        if (dto.getStatus() != null && !dto.getStatus()) {
            List<SysDict> allList = getAllList(dto.getPlatformId(), null);
            List<SysDict> childrenDictList = new ArrayList<>();
            getChildrenDictList(childrenDictList, allList, Arrays.asList(dto.getId()));
            if (!CollectionUtils.isEmpty(childrenDictList)) {
                childrenDictList.forEach(e -> e.setStatus(false));
                // 停用字典所有下级
                updateBatchById(childrenDictList);
                // 删除对应所有指标
                List<String> indexIds = childrenDictList.stream().map(SysDict::getId).collect(Collectors.toList());
                indexIds.add(sysDict.getId());
                LambdaQueryWrapper<SysDataSetIndex> query = new LambdaQueryWrapper<>();
                query.in(SysDataSetIndex::getIndexId, indexIds);
                sysDataSetIndexMapper.delete(query);
                // 删除角色相关的功能权限
                LambdaQueryWrapper<SysRoleMenuData> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(SysRoleMenuData::getDataSetIndexId, indexIds);
                sysRoleMenuDataMappper.delete(queryWrapper);
            }
        } else {
            // 修改对应指标
            if (StringUtils.equals(dto.getParentId(), "0")) {
                LambdaQueryWrapper<SysDataSet> query = new LambdaQueryWrapper<>();
                query.eq(SysDataSet::getContentId, dto.getId());
                List<SysDataSet> dataSetList = sysDataSetMapper.selectList(query);
                if (!CollectionUtils.isEmpty(dataSetList)) {
                    dataSetList.forEach(e -> e.setContent(dto.getLabel()));
                    dataSetList.forEach(e -> sysDataSetMapper.updateById(e));
                }
            } else {
                LambdaQueryWrapper<SysDataSetIndex> query = new LambdaQueryWrapper<>();
                query.eq(SysDataSetIndex::getIndexId, dto.getId());
                List<SysDataSetIndex> indexList = sysDataSetIndexMapper.selectList(query);
                if (!CollectionUtils.isEmpty(indexList)) {
                    indexList.forEach(e -> {
                        e.setKey(dto.getKey());
                        e.setLabel(dto.getLabel());
                        e.setLabel(dto.getLabel());
                        e.setSortOrder(dto.getSortOrder());
                    });
                    indexList.forEach(e -> sysDataSetIndexMapper.updateById(e));
                }
            }
        }
        return ResponseResult.success("修改成功！");
    }

    @Override
    public ResponseResult check(String id) {
        LambdaQueryWrapper<SysDataSet> query = new LambdaQueryWrapper<>();
        query.eq(SysDataSet::getContentId, id);
        List<SysDataSet> sysDataSet = sysDataSetMapper.selectList(query);
        LambdaQueryWrapper<SysDataSetIndex> queryIndex = new LambdaQueryWrapper<>();
        queryIndex.eq(SysDataSetIndex::getIndexId, id);
        List<SysDataSetIndex> sysDataSetIndices = sysDataSetIndexMapper.selectList(queryIndex);
        return ResponseResult.success(!CollectionUtils.isEmpty(sysDataSet) || !CollectionUtils.isEmpty(sysDataSetIndices));
    }

    @Override
    @Transactional
    public ResponseResult delete(String id) {
        SysDict sysDict = getById(id);
        if (sysDict == null) {
            return ResponseResult.error("字典不存在");
        }
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        query.eq(SysDict::getDelFlag, false);
        query.eq(SysDict::getPlatformId, sysDict.getPlatformId());
        query.eq(SysDict::getParentId, sysDict.getId());
        List<SysDict> list = list(query);
        if (!CollectionUtils.isEmpty(list)) {
            return ResponseResult.error("存在下级，不允许删除！");
        }
        sysDict.setDelFlag(true);
        updateById(sysDict);
        // 删除对应指标
        LambdaQueryWrapper<SysDataSetIndex> queryIndex = new LambdaQueryWrapper<>();
        queryIndex.eq(SysDataSetIndex::getIndexId, id);
        sysDataSetIndexMapper.delete(queryIndex);
        // 删除角色相关的功能权限
        LambdaQueryWrapper<SysRoleMenuData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenuData::getDataSetIndexId, id);
        sysRoleMenuDataMappper.delete(queryWrapper);
        return ResponseResult.success("删除成功！");
    }

    @Override
    public List<DictVo> getDictByPlatformId(String platformId) {
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        query.eq(SysDict::getDelFlag, false);
        query.eq(SysDict::getPlatformId, platformId);
        query.eq(SysDict::getParentId, "0");
        List<SysDict> list = list(query);
        List<DictVo> dictVoList = sysDictConverter.sysDictListToDictList(list);
        for (DictVo dictVo : dictVoList) {
            List<SysDictListVo> dictList = getList(platformId, dictVo.getId(), true);
            dictVo.setDictList(dictList);
        }
        return dictVoList;
    }

    /**
     * 根据平台id获取所有字典
     *
     * @param platformId
     */
    public List<SysDict> getAllList(String platformId, Boolean status) {
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        query.eq(SysDict::getDelFlag, false);
        query.eq(SysDict::getPlatformId, platformId);
        if (status != null) {
            query.eq(SysDict::getStatus, status);
        }
        query.orderByAsc(SysDict::getSortOrder).orderByDesc(SysDict::getUpdateTime);
        List<SysDict> allList = list(query);
        return allList;
    }

    /**
     * 获取字典树结构
     *
     * @param allMap
     * @param treeMenuList
     */
    public void getTreeDictList(Map<String, List<SysDictListVo>> allMap, List<SysDictListVo> treeMenuList) {
        for (SysDictListVo sysDictListVo : treeMenuList) {
            String id = sysDictListVo.getId();
            if (allMap.containsKey(id)) {
                List<SysDictListVo> children = allMap.get(id);
                sysDictListVo.setChildren(children);
                getTreeDictList(allMap, children);
            }
        }
    }

    /**
     * 根据父级id获取所有下级字典
     *
     * @param allList
     * @param parentIds
     */
    public List<SysDict> getChildrenDictList(List<SysDict> childrenDictList, List<SysDict> allList, List<String> parentIds) {
        for (String parentId : parentIds) {
            List<SysDict> children = allList.stream().filter(e -> StringUtils.equals(e.getParentId(), parentId)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(children)) {
                childrenDictList.addAll(children);
                List<String> childrenIds = children.stream().map(SysDict::getId).collect(Collectors.toList());
                getChildrenDictList(childrenDictList, allList, childrenIds);
            }
        }
        return childrenDictList;
    }

    public ResponseResult check(SysDictDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getPlatformId())) {
            return ResponseResult.error("平台id不能为空！");
        }
        if (StringUtils.isEmpty(dto.getParentId())) {
            return ResponseResult.error("父级id不能为空！");
        }
        if (StringUtils.isEmpty(dto.getKey())) {
            return ResponseResult.error("字典key不能为空！");
        }
        if (StringUtils.isEmpty(dto.getLabel())) {
            return ResponseResult.error("字典label不能为空！");
        }

        List<SysDict> dicts = new ArrayList<>();
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        query.eq(SysDict::getDelFlag, false);
        query.eq(SysDict::getPlatformId, dto.getPlatformId());
        query.eq(SysDict::getParentId, dto.getParentId());
        query.and(wq -> {
            wq.eq(SysDict::getKey, dto.getKey());
            wq.or().eq(SysDict::getLabel, dto.getLabel());
            if (!StringUtils.isEmpty(dto.getValue())) {
                wq.or().eq(SysDict::getValue, dto.getValue());
            }
        });
        dicts = list(query);
        SysDict parentDict = getById(dto.getParentId());
        if (isAdd) {
            if (parentDict != null && !parentDict.getStatus()) {
                return ResponseResult.error("上级字典已停用，不允许添加下级！");
            }
            if (!CollectionUtils.isEmpty(dicts)) {
                return ResponseResult.error("字典已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空");
            }
            SysDict sysDict = getById(dto.getId());
            if (sysDict == null) {
                return ResponseResult.error("字典不存在");
            } else {
                if (!CollectionUtils.isEmpty(dicts)) {
                    if (dicts.size() > 1 ||
                            !StringUtils.equals(dicts.get(0).getKey(), sysDict.getKey()) ||
                            !StringUtils.equals(dicts.get(0).getLabel(), sysDict.getLabel()) ||
                            (!StringUtils.isEmpty(dicts.get(0).getValue()) && !StringUtils.equals(dicts.get(0).getValue(), sysDict.getValue()))) {
                        return ResponseResult.error("字典已存在！");
                    }
                }
            }
            // 关于启用的相关处理
            if (dto.getStatus() != null && dto.getStatus() && parentDict != null && !parentDict.getStatus()) {
                return ResponseResult.error("上级字典已停用，不允许启用！");
            }
        }
        return ResponseResult.success("校验成功");
    }

}
