package com.lvxc.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lvxc.user.entity.SysDepart;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class LoginUser {

    /**
     * 登录人id
     */
    @ApiModelProperty(value = "登录人id")
    private String id;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 性别(0-默认未知,1-男,2-女)
     */
    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
    private Integer sex;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contact;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String position;

    /**
     * 有效时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "有效时间")
    private Date effectiveTime;

    /**
     * 密码更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "密码更新时间")
    private Date passwordUpdateTime;

    /**
     * token
     */
    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private String departId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departName;

    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private String platformId;

    /**
     * 平台名称
     */
    @ApiModelProperty(value = "平台名称")
    private String platformName;

    /**
     * 是否是超级管理员
     */
    @ApiModelProperty(value = "是否是超级管理员")
    private Boolean superFlag;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headPicture;

    /**
     * 兼职部门
     */
    @ApiModelProperty(value = "兼职部门")
    private List<PartTimeDepartVo> partTimeDeparts;

    /**
     * 角色信息
     */
    @ApiModelProperty(value = "角色信息")
    private List<LoginRoleVo> roleList;

    /**
     * 平台信息
     */
    @ApiModelProperty(value = "平台信息")
    private List<String> platformIds;

    /**
     * 菜单信息
     */
    @ApiModelProperty(value = "菜单信息")
    private List<LoginMenu> menuList;

    /**
     * 最近一级区域信息
     */
    @ApiModelProperty(value = "最近一级区域信息")
    private SysDepart region;

    /**
     * 子区域信息
     */
    @ApiModelProperty(value = "子区域信息")
    private List<SysDepart> childRegion;

    @ApiModelProperty(value = "功能点权限集")
    private Set<String> funList;
}
