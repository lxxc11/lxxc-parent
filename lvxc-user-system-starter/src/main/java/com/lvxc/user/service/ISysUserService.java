package com.lvxc.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.domain.dto.SysUserDto;
import com.lvxc.user.domain.dto.SysUserPageDto;
import com.lvxc.user.domain.dto.UpdatePasswordDto;
import com.lvxc.user.domain.dto.third.EditPasswordDto;
import com.lvxc.user.domain.dto.third.UserEditDto;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.domain.vo.SysUserPageVo;
import com.lvxc.user.entity.SysUser;

import java.util.List;

public interface ISysUserService extends IService<SysUser> {

    /**
     * 用户中心-用户表-分页列表查询
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<SysUserPageVo> getPageList(Page<SysUserPageVo> page, SysUserPageDto dto);

    /**
     * 用户中心-用户表-添加
     *
     * @param dto
     * @return
     */
    ResponseResult add(SysUserDto dto) throws Exception;

    /**
     * 用户中心-用户表-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult edit(SysUserDto dto) throws Exception;

    /**
     * 用户中心-用户表-通过id删除
     *
     * @param id
     * @return
     */
    ResponseResult delete(String id);

    /**
     * 用户中心-用户表-通过id查询
     *
     * @param id
     * @return
     */
    ResponseResult queryById(String id);


    /**
     * 用户中心-用户表-重置密码
     *
     * @param id
     * @return
     */
    ResponseResult resetPassword(String id);

    /**
     * 用户中心-用户表-原密码修改密码
     *
     * @param dto
     * @return
     */
    ResponseResult updatePassword(UpdatePasswordDto dto);


    /**
     * 用户中心-用户表-用户中心-用户表-修改用户状态
     *
     * @param id
     * @return
     */
    ResponseResult updateStatus(String id);

    /**
     * 用户中心-用户表-校验密码
     *
     * @return
     */
    ResponseResult checkPassword();

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @param platformId
     * @return
     */
    ResponseResult checkUserIsEffective(SysUser sysUser, String platformId);

    /**
     * 获取登录用户信息
     *
     * @param sysUser
     * @return
     */
    LoginUser getLoginUser(SysUser sysUser, String platformId);

    /**
     * 根据用户账号获取用户的登录信息(第三方接口)
     *
     * @param tokenStr
     * @return
     */
    ResponseResult getLoginUserByName(String tokenStr);

    /**
     * 用户中心-第三方-编辑
     *
     * @param dto
     * @return
     */
    ResponseResult userEdit(UserEditDto dto) throws Exception;

    /**
     * 用户中心-第三方-手机验证码修改密码
     *
     * @param dto
     * @return
     */
    ResponseResult editPassword(EditPasswordDto dto);

    ResponseResult<?> getLoginUserByNameNoCheck(String tokenStr);

    List<SysUser> getUserByIds(String ids);
}
