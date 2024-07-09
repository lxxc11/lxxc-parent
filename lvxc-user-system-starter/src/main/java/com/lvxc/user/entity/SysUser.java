package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 用户表
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_user对象", description = "用户表")
public class SysUser extends Entity {

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    private String userName;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * md5密码盐
     */
    @ApiModelProperty(value = "md5密码盐")
    private String salt;
    /**
     * 性别(0-默认未知,1-男,2-女)
     */
    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
    private Integer sex;
    /**
     * 启用状态（false停用，true启用）
     */
    @ApiModelProperty(value = "启用状态（false停用，true启用）")
    private Boolean status;
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
     * 历史密码
     */
    @ApiModelProperty(value = "历史密码（默认保存历史五次的密码）")
    private String historyPassword;
    /**
     * SM2公钥
     */
    @ApiModelProperty(value = "SM2公钥")
    private String publicKey;
    /**
     * SM2私钥
     */
    @ApiModelProperty(value = "SM2私钥")
    private String privateKey;
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

}
