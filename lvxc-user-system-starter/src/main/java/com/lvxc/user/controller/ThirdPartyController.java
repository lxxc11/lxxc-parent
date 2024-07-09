package com.lvxc.user.controller;

import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.constants.CommonConstant;
import com.lvxc.user.domain.dto.UpdatePasswordDto;
import com.lvxc.user.domain.dto.third.EditPasswordDto;
import com.lvxc.user.domain.dto.third.UserEditDto;
import com.lvxc.user.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 第三方接口
 * @Author: mengy
 * @Date: 2023-05-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "第三方接口")
@RestController
@RequestMapping("/thirdParty")
public class ThirdPartyController {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 用户中心-第三方-编辑
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-第三方-编辑", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-第三方-编辑", notes = "用户中心-第三方-编辑")
    @PostMapping(value = "/user/edit")
    public ResponseResult<?> userEdit(@RequestBody UserEditDto dto) throws Exception {
        return sysUserService.userEdit(dto);
    }

    /**
     * 用户中心-第三方-手机验证码修改密码
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-第三方-手机验证码修改密码", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-第三方-手机验证码修改密码", notes = "用户中心-第三方-手机验证码修改密码")
    @PostMapping(value = "/edit/password")
    public ResponseResult<?> editPassword(@RequestBody EditPasswordDto dto) {
        return sysUserService.editPassword(dto);
    }

    /**
     * 用户中心-第三方-原密码修改密码
     *
     * @param dto
     * @return
     */
    @AutoLog(value = "用户中心-第三方-原密码修改密码", operateType = CommonConstant.OPERATE_TYPE_3)
    @ApiOperation(value = "用户中心-第三方-原密码修改密码", notes = "用户中心-第三方-原密码修改密码")
    @PostMapping(value = "/update/password")
    public ResponseResult<?> updatePassword(@RequestBody UpdatePasswordDto dto) {
        return sysUserService.updatePassword(dto);
    }

}
