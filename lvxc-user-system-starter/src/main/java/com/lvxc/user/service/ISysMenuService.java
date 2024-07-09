package com.lvxc.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysMenuDto;
import com.lvxc.user.domain.vo.LoginMenu;
import com.lvxc.user.domain.vo.SysMenuVo;
import com.lvxc.user.entity.SysMenu;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 菜单管理-列表查询
     *
     * @return
     */
    List<SysMenuVo> getList(String platformId);

    /**
     * 菜单管理-新增
     *
     * @param dto
     * @return
     */
    ResponseResult add(SysMenuDto dto);

    /**
     * 菜单管理-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult edit(SysMenuDto dto);

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    ResponseResult delete(String id);

    /**
     * 校验菜单
     *
     * @param platformId
     * @param menuName
     * @return
     */
    ResponseResult checkMenu(String platformId, String menuName);

    /**
     * 菜单管理-获取当前用户菜单列表
     *
     * @return
     */
    ResponseResult getMenuList();

    /**
     * 菜单管理-根据平台id获取菜单信息
     *
     * @param platformId
     * @return
     */
    List<SysMenuVo> getListByPlatformId(String platformId);

    /**
     * 根据用户角色获取菜单权限
     *
     * @param roleIds
     * @return
     */
    List<LoginMenu> getMenuListByRoleIds(List<String> roleIds, String platformId);

}
