package com.lvxc.user.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SysUserPageDto {

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    private String userName;
    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private List<String> departIds;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private List<String> departNames;
    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态（false停用，true启用）")
    private Boolean status;


    @ApiModelProperty(value = "用户类型（1.平台类型。2.业务类型）")
    private String type;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private List<String> roleIds;
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private List<String> roleNames;

    @ApiModelProperty(value = "平台id")
    private List<String> platformIds;

}
