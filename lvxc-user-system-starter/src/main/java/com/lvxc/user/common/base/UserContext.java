package com.lvxc.user.common.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: tongsx
 * @Description:
 * @Date: Create in 13:46 2021/1/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserContext", description = "UserContext")
public class UserContext {

    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "Id")
    private Long userId;
    @ApiModelProperty(value = "用户登录名称")
    private String loginName;
    @ApiModelProperty(value = "用户真实姓名")
    private String realName;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireTime;
    @ApiModelProperty(value = "禁用flag")
    private Boolean disableFlag;
    @ApiModelProperty(value = "用户类型")
    private Integer userType;
    @ApiModelProperty(value = "属地")
    private String address;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "角色Id列表")
    private List<Integer> roleIdList;
    @ApiModelProperty(value = "角色名称列表")
    private List<String> roleNameList;
    @ApiModelProperty(value = "菜单列表")
    private List<Integer> resourceIdList;
    @ApiModelProperty(value = "菜单列表")
    private List<String> resourceKeyList;
    private List<Integer> resourceKeyIdList;
    @ApiModelProperty(value = "菜单按钮权限")
    private List<String> resourceButtonFlag;
    @ApiModelProperty(value = "用户当前部门Id")
    private Integer deptId;
    @ApiModelProperty(value = "用户当前部门")
    private String deptName;
    @ApiModelProperty(value = "县市区名称")
    private String regionName;
    @ApiModelProperty(value = "用户子部门Id列表")
    private List<Integer> childDeptIds;
    @ApiModelProperty("用户类型")
    private Boolean superFlag;

    @ApiModelProperty("部门范围")
    private String deptRange;

    @ApiModelProperty("联络员")
    private Boolean liaisonFlag;

    @ApiModelProperty("部门禁用")
    private Boolean deptDisableFlag;
    @ApiModelProperty("角色禁用")
    private Boolean roleDisableFlag;

}
