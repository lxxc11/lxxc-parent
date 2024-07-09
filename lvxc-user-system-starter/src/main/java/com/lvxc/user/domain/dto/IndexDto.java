package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IndexDto {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 数据集id
     */
    @ApiModelProperty(value = "数据集id")
    private String dataSetId;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
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
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

}
