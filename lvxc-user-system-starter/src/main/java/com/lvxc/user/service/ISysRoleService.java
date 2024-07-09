package com.lvxc.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysRoleDto;
import com.lvxc.user.domain.vo.SysRolePageVo;
import com.lvxc.user.entity.SysRole;

import java.util.List;

public interface ISysRoleService extends IService<SysRole> {

    /**
     * 角色管理-分页列表查询
     *
     * @param page
     * @param keyWord
     * @return
     */
    IPage<SysRolePageVo> getPageList(Page<SysRole> page, String keyWord);

    /**
     * 用户中心-角色表-列表查询
     *
     * @return
     */
    List<SysRolePageVo> getList();

    /**
     * 用户中心-角色表-添加
     *
     * @param dto
     * @return
     */
    ResponseResult add(SysRoleDto dto);

    /**
     * 用户中心-角色表-通过id查询
     *
     * @param id
     * @return
     */
    ResponseResult queryById(String id);

    /**
     * 用户中心-角色表-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult edit(SysRoleDto dto);

    /**
     * 用户中心-角色表-通过id删除
     *
     * @param id
     * @return
     */
    ResponseResult delete(String id);

    /**
     * 根据用户id查询角色
     *
     * @param userId
     * @return
     */
    List<SysRole> getRoleByUserId(String userId);

}
