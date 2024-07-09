package com.lvxc.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lvxc.user.common.RedisUtil;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.converter.SysRoleConverter;
import com.lvxc.user.domain.dto.MenuDataDto;
import com.lvxc.user.domain.vo.*;
import com.lvxc.user.domain.dto.SysRoleDto;
import com.lvxc.user.entity.*;
import com.lvxc.user.mapper.flw.SysRoleMapper;
import com.lvxc.user.service.*;
import com.lvxc.user.domain.vo.DateSetVo;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.domain.vo.SysRolePageVo;
import com.lvxc.user.domain.vo.SysRoleVo;
import com.lvxc.user.entity.*;
import com.lvxc.user.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleConverter sysRoleConverter;

    @Autowired
    private ISysRolePlatformService sysRolePlatformService;

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    private ISysRoleMenuDataService sysRoleMenuDataService;

    @Autowired
    private ISysRoleMenuFunctionService sysRoleMenuFunctionService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysFunctionService sysFunctionService;

    @Autowired
    private ISysDataSetService sysDataSetService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public IPage<SysRolePageVo> getPageList(Page<SysRole> page, String keyWord) {
//        LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<>();
//        query.eq(SysRole::getDelFlag, false);
//        if (!StringUtils.isEmpty(keyWord)) {
//            query.and(i -> i.like(SysRole::getRoleName, keyWord).or().like(SysRole::getDescription, keyWord));
//        }
//        query.orderByDesc(SysRole::getCreateTime);
        SysRoleDto sysRoleDto = new SysRoleDto();
        sysRoleDto.setKeyWord(keyWord);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        sysRoleDto.setPlatformIds(loginUser.getSuperFlag()?new ArrayList<>():loginUser.getPlatformIds());
        Page<SysRole> pageList = baseMapper.getPageList(page, sysRoleDto);
        Page<SysRolePageVo> sysRolePageVoPage = sysRoleConverter.sysRolePageDaoToVo(pageList);
        // 获取创建人
        Map allUserMap = redisUtil.get("_allUserMap", Map.class);
        for (SysRolePageVo record : sysRolePageVoPage.getRecords()) {
            if (!StringUtils.isEmpty(record.getCreateBy()) && allUserMap.containsKey(record.getCreateBy())) {
                SysUser sysUser = JSONObject.parseObject(allUserMap.get(record.getCreateBy()).toString(), SysUser.class);
                record.setCreateName(sysUser.getRealName());
            }
        }
        return sysRolePageVoPage;
    }

    @Override
    public List<SysRolePageVo> getList() {
        LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<>();
        query.eq(SysRole::getDelFlag, false);
        List<SysRole> list = list(query);
        List<SysRolePageVo> sysRolePageVos = sysRoleConverter.sysRoleListDaoToVo(list);
        return sysRolePageVos;
    }

    @Override
    @Transactional
    public ResponseResult add(SysRoleDto dto) {
        // 校验
        ResponseResult result = check(dto, true);
        if (!result.isSuccess()) {
            return result;
        }
        SysRole sysRole = sysRoleConverter.sysRoleDtoToDao(dto);
        // 添加角色表
        save(sysRole);
        // 添加角色菜单表
        addRoleMenu(dto, sysRole.getId());
        return ResponseResult.success("添加成功！");
    }

    @Override
    public ResponseResult queryById(String id) {
        if (StringUtils.isEmpty(id)) {
            return ResponseResult.error("id不能为空！");
        }
        SysRole role = getById(id);
        if (role == null) {
            return ResponseResult.error("角色不存在！");
        }
        SysRoleVo sysRoleVo = sysRoleConverter.sysRoleDaoToVo(role);
        // 查询角色平台表
        LambdaQueryWrapper<SysRolePlatform> rolePlatformQuery = new LambdaQueryWrapper<>();
        rolePlatformQuery.eq(SysRolePlatform::getDelFlag, false);
        rolePlatformQuery.eq(SysRolePlatform::getRoleId, id);
        List<SysRolePlatform> rolePlatformList = sysRolePlatformService.list(rolePlatformQuery);
        List<String> platformIds = rolePlatformList.stream().map(SysRolePlatform::getPlatformId).collect(Collectors.toList());
        sysRoleVo.setPlatformIds(platformIds);
        // 查询角色菜单表
        Map<String, List<String>> menuIdMap = new HashMap<>();
        LambdaQueryWrapper<SysRoleMenu> roleMenuQuery = new LambdaQueryWrapper<>();
        roleMenuQuery.eq(SysRoleMenu::getDelFlag, false);
        roleMenuQuery.eq(SysRoleMenu::getRoleId, id);
        List<SysRoleMenu> roleMenuList = sysRoleMenuService.list(roleMenuQuery);
        if (!CollectionUtils.isEmpty(roleMenuList)) {
            List<String> menuIds = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
            LambdaQueryWrapper<SysMenu> menuQuery = new LambdaQueryWrapper<>();
            menuQuery.eq(SysMenu::getDelFlag, false);
            menuQuery.in(SysMenu::getId, menuIds);
            List<SysMenu> menuList = sysMenuService.list(menuQuery);
            Map<String, List<SysMenu>> menuMap = menuList.stream().collect(Collectors.groupingBy(SysMenu::getPlatformId));
            menuMap.keySet().forEach(platformId -> menuIdMap.put(platformId, menuMap.get(platformId).stream().map(SysMenu::getId).collect(Collectors.toList())));
            sysRoleVo.setMenuIdMap(menuIdMap);
            // 查询角色菜单功能表
            Map<String, List<String>> functionIdMap = new HashMap<>();
            LambdaQueryWrapper<SysRoleMenuFunction> functionQuery = new LambdaQueryWrapper<>();
            functionQuery.eq(SysRoleMenuFunction::getDelFlag, false);
            functionQuery.eq(SysRoleMenuFunction::getRoleId, id);
            functionQuery.in(SysRoleMenuFunction::getMenuId, menuIds);
            List<SysRoleMenuFunction> roleMenuFunctionList = sysRoleMenuFunctionService.list(functionQuery);
            if (!CollectionUtils.isEmpty(roleMenuFunctionList)) {
                Map<String, List<SysRoleMenuFunction>> roleMenuFunctionMap = roleMenuFunctionList.stream().collect(Collectors.groupingBy(SysRoleMenuFunction::getMenuId));
                roleMenuFunctionMap.keySet().forEach(menuId -> functionIdMap.put(menuId, roleMenuFunctionMap.get(menuId).stream().map(SysRoleMenuFunction::getFunctionId).collect(Collectors.toList())));
            }
            sysRoleVo.setFunctionIdMap(functionIdMap);
            // 查询角色菜单数据集
            Map<String, List<DateSetVo>> menuDataMap = new HashMap<>();
            LambdaQueryWrapper<SysRoleMenuData> dataQuery = new LambdaQueryWrapper<>();
            dataQuery.eq(SysRoleMenuData::getDelFlag, false);
            dataQuery.eq(SysRoleMenuData::getRoleId, id);
            dataQuery.in(SysRoleMenuData::getMenuId, menuIds);
            List<SysRoleMenuData> roleMenuDataList = sysRoleMenuDataService.list(dataQuery);
            // 获取数据集
            if (!CollectionUtils.isEmpty(roleMenuDataList)) {
                Map<String, Map<String, List<SysRoleMenuData>>> roleMenuDataMap = roleMenuDataList.stream().collect(Collectors.groupingBy(SysRoleMenuData::getMenuId, Collectors.groupingBy(SysRoleMenuData::getDataSetId)));
                for (String menuId : roleMenuDataMap.keySet()) {
                    List<DateSetVo> dateSetVoList = new ArrayList<>();
                    for (String dataSetId : roleMenuDataMap.get(menuId).keySet()) {
                        DateSetVo dateSetVo = new DateSetVo();
                        dateSetVo.setDataSetId(dataSetId);
                        List<String> indexList = roleMenuDataMap.get(menuId).get(dataSetId).stream().map(SysRoleMenuData::getDataSetIndexId).collect(Collectors.toList());
                        dateSetVo.setIndexList(indexList);
                        dateSetVoList.add(dateSetVo);
                    }
                    menuDataMap.put(menuId, dateSetVoList);
                }
            }
            sysRoleVo.setMenuDataMap(menuDataMap);
        }
        return ResponseResult.success(sysRoleVo);
    }

    @Override
    @Transactional
    public ResponseResult edit(SysRoleDto dto) {
        // 校验
        ResponseResult result = check(dto, false);
        if (!result.isSuccess()) {
            return result;
        }
        SysRole role = getById(dto.getId());
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        // 修改角色表
        updateById(role);
        // 修改角色菜单表, 角色菜单功能表, 角色菜单数据表
        removeRoleMenu(dto.getId());
        addRoleMenu(dto, dto.getId());
        return ResponseResult.success("修改成功！");
    }

    @Override
    @Transactional
    public ResponseResult delete(String id) {
        // 删除角色
        SysRole role = getById(id);
        if (role == null) {
            return ResponseResult.error("角色不存在！");
        }
        LambdaQueryWrapper<SysUserRole> query = new LambdaQueryWrapper<>();
        query.eq(SysUserRole::getDelFlag, false);
        query.in(SysUserRole::getRoleId, id);
        List<SysUserRole> list = sysUserRoleService.list(query);
        if (!CollectionUtils.isEmpty(list)) {
            return ResponseResult.error("角色下存在用户，不可删除！");
        }
        role.setDelFlag(true);
        updateById(role);
        // 删除角色菜单表, 角色菜单功能表, 角色菜单数据表
        removeRoleMenu(id);
        return ResponseResult.success("删除成功！");
    }

    @Override
    public List<SysRole> getRoleByUserId(String userId) {
        return baseMapper.getRoleByUserId(userId);
    }

    public ResponseResult check(SysRoleDto dto, boolean isAdd) {
        if (StringUtils.isEmpty(dto.getRoleName())) {
            return ResponseResult.error("角色名称不能为空！");
        }
        if (CollectionUtils.isEmpty(dto.getPlatformIds())) {
            return ResponseResult.error("平台权限不能为空！");
        }
        if (CollectionUtils.isEmpty(dto.getMenuIds())) {
            return ResponseResult.error("菜单权限不能为空！");
        }
        LambdaQueryWrapper<SysRole> query = new LambdaQueryWrapper<>();
        query.eq(SysRole::getDelFlag, false);
        query.eq(SysRole::getRoleName, dto.getRoleName());
        SysRole role = getOne(query);
        if (isAdd) {
            if (role != null) {
                return ResponseResult.error("角色已存在！");
            }
        } else {
            if (StringUtils.isEmpty(dto.getId())) {
                return ResponseResult.error("id不能为空！");
            }
            SysRole sysRole = getById(dto.getId());
            if (sysRole == null) {
                return ResponseResult.error("角色不存在！");
            }
            if (role != null && !StringUtils.equals(sysRole.getRoleName(), dto.getRoleName())) {
                return ResponseResult.error("角色已存在！");
            }
        }
        return ResponseResult.success("校验成功！");
    }


    /**
     * 添加角色菜单表，角色菜单功能表，角色菜单数据表
     *
     * @param dto
     * @param roleId
     */
    public void addRoleMenu(SysRoleDto dto, String roleId) {
        // 添加角色平台表
        List<SysRolePlatform> rolePlatformList = new ArrayList<>();
        for (String platformId : dto.getPlatformIds()) {
            SysRolePlatform sysRolePlatform = new SysRolePlatform();
            sysRolePlatform.setRoleId(roleId);
            sysRolePlatform.setPlatformId(platformId);
            rolePlatformList.add(sysRolePlatform);
        }
        if (!CollectionUtils.isEmpty(rolePlatformList)) {
            sysRolePlatformService.saveBatch(rolePlatformList);
        }
        // 添加角色菜单表
        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        for (String menuId : dto.getMenuIds()) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            roleMenuList.add(sysRoleMenu);
        }
        if (!CollectionUtils.isEmpty(roleMenuList)) {
            sysRoleMenuService.saveBatch(roleMenuList);
        }
        // 添加角色菜单功能表
        List<SysRoleMenuFunction> roleMenuFunctionList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dto.getFunctionIds())) {
            LambdaQueryWrapper<SysFunction> query = new LambdaQueryWrapper<>();
            query.eq(SysFunction::getDelFlag, false);
            query.in(SysFunction::getMenuId, dto.getMenuIds());
            List<SysFunction> functionList = sysFunctionService.list(query);
            Map<String, SysFunction> functionMap = functionList.stream().collect(Collectors.toMap(SysFunction::getId, Function.identity()));
            for (String functionId : dto.getFunctionIds()) {
                if (!functionMap.containsKey(functionId)) {
                    continue;
                }
                SysRoleMenuFunction roleMenuFunction = new SysRoleMenuFunction();
                roleMenuFunction.setRoleId(roleId);
                roleMenuFunction.setMenuId(functionMap.get(functionId).getMenuId());
                roleMenuFunction.setFunctionId(functionId);
                roleMenuFunctionList.add(roleMenuFunction);
            }
        }
        if (!CollectionUtils.isEmpty(roleMenuFunctionList)) {
            sysRoleMenuFunctionService.saveBatch(roleMenuFunctionList);
        }
        // 添加角色菜单数据表
        List<SysRoleMenuData> roleMenuDataList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dto.getMenuDatas())) {
            List<String> dataSetIds = dto.getMenuDatas().stream().map(MenuDataDto::getDataSetId).collect(Collectors.toList());
            LambdaQueryWrapper<SysDataSet> query = new LambdaQueryWrapper<>();
            query.eq(SysDataSet::getDelFlag, false);
            query.in(SysDataSet::getId, dataSetIds);
            List<SysDataSet> dataSetList = sysDataSetService.list(query);
            Map<String, MenuDataDto> menuDataMap = dto.getMenuDatas().stream().collect(Collectors.toMap(MenuDataDto::getDataSetId, Function.identity()));
            for (SysDataSet sysDataSet : dataSetList) {
                MenuDataDto menuDataDto = menuDataMap.get(sysDataSet.getId());
                for (String dataSetIndexId : menuDataDto.getDataSetIndexIds()) {
                    SysRoleMenuData sysRoleMenuData = new SysRoleMenuData();
                    sysRoleMenuData.setRoleId(roleId);
                    sysRoleMenuData.setMenuId(sysDataSet.getMenuId());
                    sysRoleMenuData.setDataSetId(sysDataSet.getId());
                    sysRoleMenuData.setDataSetIndexId(dataSetIndexId);
                    roleMenuDataList.add(sysRoleMenuData);
                }
            }
        }
        if (!CollectionUtils.isEmpty(roleMenuDataList)) {
            sysRoleMenuDataService.saveBatch(roleMenuDataList);
        }
    }

    /**
     * 删除角色菜单表，角色菜单功能表，角色菜单数据表
     *
     * @param roleId
     */
    public void removeRoleMenu(String roleId) {
        // 删除角色平台表
        LambdaQueryWrapper<SysRolePlatform> platformQuery = new LambdaQueryWrapper<>();
        platformQuery.eq(SysRolePlatform::getDelFlag, false);
        platformQuery.eq(SysRolePlatform::getRoleId, roleId);
        sysRolePlatformService.remove(platformQuery);
        // 删除角色菜单表
        LambdaQueryWrapper<SysRoleMenu> menuQuery = new LambdaQueryWrapper<>();
        menuQuery.eq(SysRoleMenu::getDelFlag, false);
        menuQuery.eq(SysRoleMenu::getRoleId, roleId);
        sysRoleMenuService.remove(menuQuery);
        // 删除角色菜单功能表
        LambdaQueryWrapper<SysRoleMenuFunction> functionQuery = new LambdaQueryWrapper<>();
        functionQuery.eq(SysRoleMenuFunction::getDelFlag, false);
        functionQuery.eq(SysRoleMenuFunction::getRoleId, roleId);
        sysRoleMenuFunctionService.remove(functionQuery);
        // 删除角色菜单数据表
        LambdaQueryWrapper<SysRoleMenuData> dataQuery = new LambdaQueryWrapper<>();
        dataQuery.eq(SysRoleMenuData::getDelFlag, false);
        dataQuery.eq(SysRoleMenuData::getRoleId, roleId);
        sysRoleMenuDataService.remove(dataQuery);
    }

}
