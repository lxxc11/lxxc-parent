package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户中心-角色-菜单-功能表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_role_menu_function")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_role_menu_function对象", description = "用户中心-角色-菜单-功能表")
public class SysRoleMenuFunction extends Entity {

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
     * 功能id
     */
    @ApiModelProperty(value = "功能id")
    private String functionId;

}
