package com.lvxc.user.domain.dto.third;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserEditDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contact;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String position;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headPicture;

}
