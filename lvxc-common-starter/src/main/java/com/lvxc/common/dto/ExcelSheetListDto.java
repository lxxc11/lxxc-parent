package com.lvxc.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ExcelSheetListDto<T> {

    @ApiModelProperty("名称")
    private String sheetName;
    @ApiModelProperty("导出实体类(@ExcelProperty)")
    private Class<T> tClass;
    @ApiModelProperty("导出数据")
    private List<T> dataList;

}
