package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysDepartDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
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
