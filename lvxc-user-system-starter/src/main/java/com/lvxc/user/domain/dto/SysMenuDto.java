package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysMenuDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    private String parentId;
    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private String platformId;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称")
    private String menuEnName;
    /**
     * 菜单类型（1目录 2菜单）
     */
    @ApiModelProperty(value = "菜单类型（1目录 2菜单）")
    private Integer type;
    /**
     * 菜单URL
     */
    @ApiModelProperty(value = "菜单URL")
    private String url;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;
    /**
     * 菜单状态（false停用 true启用）
     */
    @ApiModelProperty(value = "菜单状态（false停用 true启用）")
    private Boolean status;
    /**
     * 菜单ICON
     */
    @ApiModelProperty(value = "菜单ICON")
    private String icon;
    /**
     * 其他数据集
     */
    @ApiModelProperty(value = "其他数据集（前端所用字段，不涉及后端逻辑）")
    private String otherData;

}
