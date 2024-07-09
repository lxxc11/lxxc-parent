package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdatePasswordDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;
    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    private String newPassword;
    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;

}
