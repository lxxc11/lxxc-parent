package com.lvxc.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginFunction {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private String menuId;

    /**
     * 功能点名称
     */
    @ApiModelProperty(value = "功能点名称")
    private String functionName;

    /**
     * 功能点标识
     */
    @ApiModelProperty(value = "功能点标识")
    private String functionTag;

    /**
     * 功能点URL
     */
    @ApiModelProperty(value = "功能点URL")
    private String url;

    /**
     * 功能点状态（false停用 true启用）
     */
    @ApiModelProperty(value = "功能点状态（false停用 true启用）")
    private Boolean status;

    /**
     * 功能页面ICON
     */
    @ApiModelProperty(value = "功能页面ICON")
    private String icon;

}
