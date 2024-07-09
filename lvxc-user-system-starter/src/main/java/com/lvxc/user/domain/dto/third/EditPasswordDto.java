package com.lvxc.user.domain.dto.third;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EditPasswordDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String contact;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String smsCode;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

}
