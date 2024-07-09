package com.lvxc.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SysRoleVo {

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
     * 权限描述
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
    private Map<String, List<String>> menuIdMap;
    /**
     * 功能id
     */
    @ApiModelProperty(value = "功能id")
    private Map<String, List<String>> functionIdMap;
    /**
     * 数据集
     */
    @ApiModelProperty(value = "数据集")
    private Map<String, List<DateSetVo>> menuDataMap;

}
