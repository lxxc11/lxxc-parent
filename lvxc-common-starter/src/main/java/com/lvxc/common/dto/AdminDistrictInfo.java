package com.lvxc.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AdminDistrictInfo
 * @Description 行政区信息
 * @Author Roger
 * @Date 2023/9/10
 **/
@Data
public class AdminDistrictInfo {

    @ApiModelProperty("国家")
    private String country;
    @ApiModelProperty("省份")
    private String province;
    @ApiModelProperty("城市")
    private String city;
}
