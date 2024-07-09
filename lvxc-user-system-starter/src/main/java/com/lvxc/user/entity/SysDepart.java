package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户中心-部门表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_depart")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_depart对象", description = "用户中心-部门表")
public class SysDepart extends Entity {

    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private String parentId;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departName;
    /**
     * 类型（1地区 2部门）
     */
    @ApiModelProperty(value = "类型（1地区 2部门）")
    private Integer type;

    /**
     * 行政级别
     */
    @ApiModelProperty(value = "行政级别")
    private String districtLevel;



}
