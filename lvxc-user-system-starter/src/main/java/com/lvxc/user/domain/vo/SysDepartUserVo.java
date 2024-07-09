package com.lvxc.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysDepartUserVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private String departId;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * SM2私钥
     */
    @JsonIgnore
    @ApiModelProperty(value = "SM2私钥")
    private String privateKey;

}
