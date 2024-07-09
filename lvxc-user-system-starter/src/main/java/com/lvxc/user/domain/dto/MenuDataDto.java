package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuDataDto {

    /**
     * 数据集id
     */
    @ApiModelProperty(value = "数据集id")
    private String dataSetId;
    /**
     * 数据集指标id
     */
    @ApiModelProperty(value = "数据集指标id")
    private List<String> dataSetIndexIds;

}
