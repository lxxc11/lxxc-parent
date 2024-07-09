package com.lvxc.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysFunctionDto;
import com.lvxc.user.domain.vo.LoginFunction;
import com.lvxc.user.domain.vo.SysFunctionVo;
import com.lvxc.user.entity.SysFunction;

import java.util.List;

public interface ISysFunctionService extends IService<SysFunction> {

    /**
     * 菜单管理-列表查询
     *
     * @param page
     * @param menuId
     * @return
     */
    IPage<SysFunctionVo> getPageList(Page<SysFunction> page, String menuId);

    /**
     * 功能点管理-全量列表查询
     *
     * @param menuId
     * @return
     */
    ResponseResult getList(String menuId);

    /**
     * 菜单管理-添加
     *
     * @param dto
     * @return
     */
    ResponseResult add(SysFunctionDto dto);

    /**
     * 菜单管理-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult edit(SysFunctionDto dto);

    /**
     * 菜单管理-通过id删除
     *
     * @param id
     * @return
     */
    ResponseResult delete(String id);

    /**
     * 根据角色查询功能点
     *
     * @param roleIds
     * @return
     */
    List<LoginFunction> getFunctionListByRoleIds(List<String> roleIds);

}
