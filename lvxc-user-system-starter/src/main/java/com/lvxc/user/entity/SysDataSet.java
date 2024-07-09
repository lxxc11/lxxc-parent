package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户中心-数据集表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_data_set")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_data_set对象", description = "用户中心-数据集表")
public class SysDataSet extends Entity {

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private String menuId;
    /**
     * 数据集名称
     */
    @ApiModelProperty(value = "数据集名称")
    private String dataName;
    /**
     * 数据集内容id
     */
    @ApiModelProperty(value = "数据集内容id")
    private String contentId;
    /**
     * 数据集内容
     */
    @ApiModelProperty(value = "数据集内容")
    private String content;


}

