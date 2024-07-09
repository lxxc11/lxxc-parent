package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("sys_function")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_function对象", description = "功能表")
public class SysFunction extends Entity {

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

    @ApiModelProperty(value = "功能点名称")
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
