package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SysRoleDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    /**
     * 描述
     */
    @ApiModelProperty(value = "权限描述")
    private String description;
    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private List<String> platformIds;
    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private List<String> menuIds;
    /**
     * 功能id
     */
    @ApiModelProperty(value = "功能id")
    private List<String> functionIds;
    /**
     * 数据权限配置
     */
    @ApiModelProperty(value = "数据权限配置")
    private List<MenuDataDto> menuDatas;

    private String keyWord;

}
