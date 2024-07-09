package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: tongsx
 * @Description:
 * @Date: Create in 10:06 2021/1/6
 */
@Data
@ApiModel(value = "LoginDTO", description = "LoginDTO")
public class LoginDTO {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String loginName;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "图片验证码ID")
    private String pictureCaptchaId;
    @ApiModelProperty(value = "图片验证码内容")
    private String code;
    @ApiModelProperty(value = "单点token")
    private String token;

}
