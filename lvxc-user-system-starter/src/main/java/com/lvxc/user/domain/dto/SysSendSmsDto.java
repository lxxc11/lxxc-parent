package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysSendSmsDto {

    @ApiModelProperty(value = "平台id")
    private String platformId;

    private String smsMobile;
}
