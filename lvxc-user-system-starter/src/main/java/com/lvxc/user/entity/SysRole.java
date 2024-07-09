package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户中心-角色表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_role")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_role对象", description = "用户中心-角色表")
public class SysRole extends Entity {

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

}
