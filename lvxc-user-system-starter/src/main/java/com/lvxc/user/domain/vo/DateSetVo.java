package com.lvxc.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DateSetVo {

    /**
     * 数据集id
     */
    @ApiModelProperty(value = "数据集id")
    private String dataSetId;

    /**
     * 指标
     */
    @ApiModelProperty(value = "指标")
    private List<String> indexList;

}
