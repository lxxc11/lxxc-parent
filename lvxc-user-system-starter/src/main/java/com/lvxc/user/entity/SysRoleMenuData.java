package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户中心-角色-菜单-数据表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_role_menu_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_role_menu_data对象", description = "用户中心-角色-菜单-数据表")
public class SysRoleMenuData extends Entity {

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private String roleId;
    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private String menuId;
    /**
     * 数据集id
     */
    @ApiModelProperty(value = "数据集id")
    private String dataSetId;
    /**
     * 数据集指标id
     */
    @ApiModelProperty(value = "数据集指标id")
    private String dataSetIndexId;


}
