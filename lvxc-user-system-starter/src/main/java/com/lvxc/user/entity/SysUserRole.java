package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户中心-用户角色表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_user_role")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_user_role对象", description = "用户中心-用户角色表")
public class SysUserRole extends Entity {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private String roleId;

}
