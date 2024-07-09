package com.lvxc.user.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRoleVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;


}
