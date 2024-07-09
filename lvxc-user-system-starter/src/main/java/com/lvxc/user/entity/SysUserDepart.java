package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户中心-用户部门表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_user_depart")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_user_depart对象", description = "用户中心-用户部门表")
public class SysUserDepart extends Entity {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;
    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private String departId;
    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门类型（0所属部门 1兼职部门）")
    private Integer type;

}
