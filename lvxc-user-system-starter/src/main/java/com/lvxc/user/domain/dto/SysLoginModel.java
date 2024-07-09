package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "登录对象", description = "登录对象")
public class SysLoginModel {

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String captcha;

    /**
     * 验证码key
     */
    @ApiModelProperty(value = "验证码key")
    private String checkKey;

    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private String platformId;

    private String smsCode;

    private String smsMobile;

}
