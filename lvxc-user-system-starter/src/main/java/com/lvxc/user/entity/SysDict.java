package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 用户中心-字典表
 * @Author: mengy
 * @Date: 2023-05-04
 * @Version: V1.0
 */
@Data
@TableName("sys_dict")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_dict对象", description = "用户中心-字典表")
public class SysDict extends Entity {

    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private String platformId;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private String parentId;
    /**
     * 字典key
     */
    @ApiModelProperty(value = "字典key")
    private String key;
    /**
     * 字典label
     */
    @ApiModelProperty(value = "字典label")
    private String label;
    /**
     * 字典value
     */
    @ApiModelProperty(value = "字典value")
    private String value;
    /**
     * 菜单状态（false停用 true启用）
     */
    @ApiModelProperty(value = "菜单状态（false停用 true启用）")
    private Boolean status;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

}
