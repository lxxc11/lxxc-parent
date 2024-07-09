package com.lvxc.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PartTimeDepartVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departName;

}
