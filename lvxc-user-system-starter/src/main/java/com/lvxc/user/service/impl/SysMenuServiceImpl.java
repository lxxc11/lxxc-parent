package com.lvxc.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.converter.SysDataSetConverter;
import com.lvxc.user.converter.SysFunctionConverter;
import com.lvxc.user.converter.SysMenuConverter;
import com.lvxc.user.domain.dto.SysMenuDto;
import com.lvxc.user.domain.vo.*;
import com.lvxc.user.entity.*;
import com.lvxc.user.mapper.flw.*;
import com.lvxc.user.mapper.flw.SysMenuMapper;
import com.lvxc.user.mapper.flw.SysRoleMapper;
import com.lvxc.user.service.ISysDataSetIndexService;
import com.lvxc.user.service.ISysDataSetService;
import com.lvxc.user.service.ISysFunctionService;
import com.lvxc.user.service.ISysMenuService;
import com.lvxc.user.domain.vo.*;
import com.lvxc.user.entity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuConverter sysMenuConverter;

    @Autowired
    private SysFunctionConverter sysFunctionConverter;

    @Autowired
    private SysDataSetConverter sysDataSetConverter;

    @Autowired
    private ISysDataSetService sysDataSetService;

    @Resource
    private ISysFunctionService sysFunctionService;

    @Resource
    private ISysDataSetIndexService sysDataSetIndexService;

    @Override
    public List<SysMenuVo> getList(String platformId) {
        LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>();
        query.eq(SysMenu::getDelFlag, false);
        query.eq(SysMenu::getPlatformId, platformId);
        query.orderByAsc(SysMenu::getSortOrder).orderByDesc(SysMenu::getUpdateTime);
        List<SysMenu> allList = list(query);
        List<SysMenuVo> list = sysMenuConverter.sysMenuListDaoToVo(allList);

        Map<String, List<SysMenuVo>> allMap = list.stream().collect(Collectors.groupingBy(SysMenuVo::getParentId));
        List<SysMenuVo> treeMenuList = list.stream().filter(e -> StringUtils.equals(e.getParentId(), "0")).collect(Collectors.toList());
        getTreeMenuList(allMap, treeMenuList);
        return treeMenuList;
    }

    /**
     * 获取菜单树结构
     *
     * @param allMap
     * @param treeMenuList
     */
    public void getTreeMenuList(Map<String, List<SysMenuVo>> allMap, List<SysMenuVo> treeMenuList) {
        for (SysMenuVo sysMenuVo : treeMenuList) {
            String id = sysMenuVo.getId();
            if (allMap.containsKey(id)) {
                List<SysMenuVo> children = allMap.get(id);
                children.forEach(e -> e.setParentName(sysMenuVo.getMenuName()));
                sysMenuVo.setChildren(children);
                getTreeMenuList(allMap, children);
            }
        }
    }

    @Override
    @Transactional
    public ResponseResult add(SysMenuDto dto) {
        // 添加校验
        ResponseResult result = check(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        dto.setParentId(!StringUtils.isEmpty(dto.getParentId()) ? dto.getParentId() : "0");
        SysMenu sysMenu = sysMenuConverter.sysMenuDtoToDao(dto);
        save(sysMenu);
        return ResponseResult.success("新增成功！");
    }

    @Override
    @Transactional
    public ResponseResult edit(SysMenuDto dto) {
        // 修改校验
        ResponseResult result = check(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysMenu sysMenu = getById(dto.getId());
//        sysMenu.setParentId(!StringUtils.isEmpty(dto.getParentId()) ? dto.getParentId() : "0");
        sysMenu.setPlatformId(dto.getPlatformId());
        sysMenu.setMenuName(dto.getMenuName());
        sysMenu.setMenuEnName(dto.getMenuEnName());
        sysMenu.setType(dto.getType());
        sysMenu.setUrl(dto.getUrl());
        sysMenu.setSortOrder(dto.getSortOrder());
        sysMenu.setStatus(dto.getStatus());
        sysMenu.setIcon(dto.getIcon());
        sysMenu.setOtherData(dto.getOtherData());
        updateById(sysMenu);
        // 删除功能权限与数据权限
        if (dto.getType() == 1) {
            LambdaQueryWrapper<SysFunction> functionQuery = new LambdaQueryWrapper<>();
            functionQuery.eq(SysFunction::getDelFlag, false);
            functionQuery.eq(SysFunction::getMenuId, dto.getId());
            List<SysFunction> sysFunctions = sysFunctionService.list(functionQuery);
            if (!CollectionUtils.isEmpty(sysFunctions)) {
                sysFunctions.forEach(e -> e.setDelFlag(true));
                sysFunctionService.updateBatchById(sysFunctions);
            }
            LambdaQueryWrapper<SysDataSet> dataSetQuery = new LambdaQueryWrapper<>();
            dataSetQuery.eq(SysDataSet::getDelFlag, false);
            dataSetQuery.eq(SysDataSet::getMenuId, dto.getId());
            List<SysDataSet> dataSetList = sysDataSetService.list(dataSetQuery);
            if (!CollectionUtils.isEmpty(dataSetList)) {
                dataSetList.forEach(e -> e.setDelFlag(true));
                sysDataSetService.updateBatchById(dataSetList);
                List<String> dataSetIds = dataSetList.stream().map(SysDataSet::getId).collect(Collectors.toList());
                LambdaQueryWrapper<SysDataSetIndex> dataSetIndexQuery = new LambdaQueryWrapper<>();
                dataSetIndexQuery.eq(SysDataSetIndex::getDelFlag, false);
                dataSetIndexQuery.in(SysDataSetIndex::getDataSetId, dataSetIds);
                List<SysDataSetIndex> dataSetIndexList = sysDataSetIndexService.list(dataSetIndexQuery);
                dataSetIndexList.forEach(e -> e.setDelFlag(true));
                sysDataSetIndexService.updateBatchById(dataSetIndexList);
            }
        }
        return ResponseResult.success("修改成功！");
    }

    @Override
    @Transactional
    public ResponseResult delete(String id) {
        SysMenu sysMenu = getById(id);
        if (sysMenu == null) {
            return ResponseResult.error("菜单不存在！");
        }
        LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>();
        query.eq(SysMenu::getDelFlag, false);
        query.eq(SysMenu::getParentId, id);
        List<SysMenu> children = list(query);
        if (!CollectionUtils.isEmpty(children)) {
            return ResponseResult.error("存在下级，不允许删除！");
        }
        sysMenu.setDelFlag(true);
        updateById(sysMenu);
        return ResponseResult.success("删除成功！");
    }

    @Override
    public ResponseResult checkMenu(String platformId, String menuName) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SysMenu sysMenu = baseMapper.checkMenu(loginUser.getId(), platformId, menuName);
        if (sysMenu == null) {
            return ResponseResult.error("访问菜单没有权限，请联系管理员！");
        } else {
            return ResponseResult.success("校验成功！");
        }
    }

    @Override
    public ResponseResult getMenuList() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<SysRole> roles = sysRoleMapper.getRoleByUserId(loginUser.getId());
        List<String> roleIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());
        List<LoginMenu> menus = baseMapper.getMenuListByRoleIds(roleIds, loginUser.getPlatformId());
        return ResponseResult.success(menus);
    }

    @Override
    public List<SysMenuVo> getListByPlatformId(String platformId) {
        LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>();
        query.eq(SysMenu::getDelFlag, false);
        query.eq(SysMenu::getStatus, true);
        query.eq(SysMenu::getPlatformId, platformId);
        query.orderByAsc(SysMenu::getSortOrder).orderByDesc(SysMenu::getUpdateTime);
        List<SysMenu> allList = list(query);
        List<SysMenuVo> list = sysMenuConverter.sysMenuListDaoToVo(allList);
        if (!CollectionUtils.isEmpty(list)) {
            List<String> menuIds = list.stream().map(SysMenuVo::getId).collect(Collectors.toList());
            // 查询功能点
            LambdaQueryWrapper<SysFunction> functionQuery = new LambdaQueryWrapper<>();
            functionQuery.eq(SysFunction::getDelFlag, false);
            functionQuery.eq(SysFunction::getStatus, true);
            functionQuery.in(SysFunction::getMenuId, menuIds);
            functionQuery.orderByDesc(SysFunction::getUpdateTime);
            List<SysFunction> sysFunctions = sysFunctionService.list(functionQuery);
            List<SysFunctionVo> sysFunctionList = sysFunctionConverter.sysFunctionListDaoToVo(sysFunctions);
            Map<String, List<SysFunctionVo>> sysFunctionMap = sysFunctionList.stream().collect(Collectors.groupingBy(SysFunctionVo::getMenuId));
            // 查询数据集
            LambdaQueryWrapper<SysDataSet> dataSetQuery = new LambdaQueryWrapper<>();
            dataSetQuery.eq(SysDataSet::getDelFlag, false);
            dataSetQuery.in(SysDataSet::getMenuId, menuIds);
            dataSetQuery.orderByDesc(SysDataSet::getUpdateTime);
            List<SysDataSet> sysDataSets = sysDataSetService.list(dataSetQuery);
            List<SysDataSetVo> sysDataSetList = sysDataSetConverter.sysDataSetListDaoToVo(sysDataSets);
            // 查询指标
            if (!CollectionUtils.isEmpty(sysDataSetList)) {
                List<String> dataSetIds = sysDataSetList.stream().map(SysDataSetVo::getId).collect(Collectors.toList());
                LambdaQueryWrapper<SysDataSetIndex> queryIndex = new LambdaQueryWrapper<>();
                queryIndex.eq(SysDataSetIndex::getDelFlag, false);
                queryIndex.in(SysDataSetIndex::getDataSetId, dataSetIds);
                queryIndex.orderByAsc(SysDataSetIndex::getSortOrder).orderByDesc(SysDataSetIndex::getUpdateTime);
                List<SysDataSetIndex> sysDataSetIndices = sysDataSetIndexService.list(queryIndex);
                if (!CollectionUtils.isEmpty(sysDataSetIndices)) {
                    List<SysDataSetIndexVo> dataSetIndices = sysDataSetConverter.sysDataSetIndexDaoToVo(sysDataSetIndices);
                    Map<String, List<SysDataSetIndexVo>> dataSetIndexMap = dataSetIndices.stream().collect(Collectors.groupingBy(SysDataSetIndexVo::getDataSetId));
                    for (SysDataSetVo sysDataSetVo : sysDataSetList) {
                        if (dataSetIndexMap.containsKey(sysDataSetVo.getId())) {
                            List<SysDataSetIndexVo> indexList = dataSetIndexMap.get(sysDataSetVo.getId());
                            Map<String, List<SysDataSetIndexVo>> indexMap = indexList.stream().collect(Collectors.groupingBy(SysDataSetIndexVo::getParentId));
                            List<SysDataSetIndexVo> treeMenuList = indexList.stream().filter(e -> StringUtils.equals(sysDataSetVo.getContentId(), "0") ? StringUtils.equals(e.getParentId(), "0") : StringUtils.equals(e.getParentId(), sysDataSetVo.getContentId())).collect(Collectors.toList());
                            sysDataSetService.getTreeIndexList(indexMap, treeMenuList, StringUtils.equals(sysDataSetVo.getContentId(), "0") ? false : true);
                            sysDataSetVo.setIndexList(treeMenuList);
                        }
                    }
                }
            }
            Map<String, List<SysDataSetVo>> sysDataSetMap = sysDataSetList.stream().collect(Collectors.groupingBy(SysDataSetVo::getMenuId));
            for (SysMenuVo sysMenuVo : list) {
                if (sysFunctionMap.containsKey(sysMenuVo.getId())) {
                    List<SysFunctionVo> sysFunctionVos = sysFunctionMap.get(sysMenuVo.getId());
                    sysFunctionVos.forEach(e -> e.setMenuName(sysMenuVo.getMenuName()));
                    sysMenuVo.setFunctionList(sysFunctionVos);
                }
                if (sysDataSetMap.containsKey(sysMenuVo.getId())) {
                    List<SysDataSetVo> sysDataSetVos = sysDataSetMap.get(sysMenuVo.getId());
                    sysDataSetVos.forEach(e -> e.setMenuName(sysMenuVo.getMenuName()));
                    sysMenuVo.setDataSetList(sysDataSetVos);
                }
            }
            Map<String, List<SysMenuVo>> allMap = list.stream().collect(Collectors.groupingBy(SysMenuVo::getParentId));
            List<SysMenuVo> treeMenuList = list.stream().filter(e -> StringUtils.equals(e.getParentId(), "0")).collect(Collectors.toList());
            getTreeMenuList(allMap, treeMenuList);
            return treeMenuList;
        }
        return list;
    }

    @Override
    public List<LoginMenu> getMenuListByRoleIds(List<String> roleIds, String platformId) {
        return baseMapper.getMenuListByRoleIds(roleIds, platformId);
    }

    public ResponseResult check(SysMenuDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getPlatformId())) {
            return ResponseResult.error("平台id不能为空！");
        }
        if (StringUtils.isEmpty(dto.getMenuName())) {
            return ResponseResult.error("菜单名称不能为空！");
        }
        if (StringUtils.isEmpty(dto.getMenuEnName())) {
            return ResponseResult.error("英文名称不能为空！");
        }
        if (dto.getType() == null) {
            return ResponseResult.error("菜单类型不能为空！");
        }
        if (StringUtils.isEmpty(dto.getUrl())) {
            return ResponseResult.error("菜单URL不能为空！");
        }
        if (dto.getSortOrder() == null) {
            return ResponseResult.error("排序不能为空！");
        }
        if (dto.getStatus() == null) {
            return ResponseResult.error("菜单状态不能为空！");
        }

        SysMenu menu = null;
        LambdaQueryWrapper<SysMenu> query = new LambdaQueryWrapper<>();
        query.eq(SysMenu::getDelFlag, false);
        query.eq(SysMenu::getPlatformId, dto.getPlatformId());
        query.eq(SysMenu::getParentId, !StringUtils.isEmpty(dto.getParentId()) ? dto.getParentId() : "0");
        query.eq(SysMenu::getMenuName, dto.getMenuName());
        menu = getOne(query);
        if (isAdd) {
            if (menu != null) {
                return ResponseResult.error("菜单已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空");
            }
            SysMenu sysMenu = getById(dto.getId());
            if (sysMenu == null) {
                return ResponseResult.error("菜单不存在");
            } else {
                if (menu != null && !StringUtils.equals(menu.getMenuName(), sysMenu.getMenuName())) {
                    return ResponseResult.error("菜单已存在！");
                }
            }
        }
        return ResponseResult.success("校验成功");
    }


}
