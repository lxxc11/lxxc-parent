package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@TableName("tb_user")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TbUser对象", description = "用户表")
public class TbUser {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键assign_id")
    private Long userId;

    @ApiModelProperty("用户真实姓名")
    @TableField(value = "real_name")
    private String realName;

    @ApiModelProperty("用户登录名称")
    @TableField(value = "login_name")
    private String loginName;

    @ApiModelProperty("密文密码")
    @TableField(value = "login_password")
    private String loginPassword;

    @ApiModelProperty("手机号")
    @TableField(value = "mobile")
    private String mobile;

    @ApiModelProperty("部门id")
    @TableField(value = "dept_id")
    private Integer deptId;

    @ApiModelProperty("部门范围")
    @TableField(value = "dept_range")
    private String deptRange;

    @ApiModelProperty("联络员")
    @TableField(value = "liaison_flag")
    private Boolean liaisonFlag;

    @ApiModelProperty("是否超级管理员")
    @TableField(value = "super_flag")
    private Boolean superFlag;

    @ApiModelProperty("禁用flag")
    @TableField(value = "disable_flag")
    private Boolean disableFlag;

    @ApiModelProperty("添加人id")
    @TableField(value = "add_by_user_id", fill = FieldFill.INSERT)
    private Long addByUserId;

    @ApiModelProperty("修改人id")
    @TableField(value = "update_by_user_id", fill = FieldFill.INSERT_UPDATE)
    private Long updateByUserId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("添加时间")
    @TableField(value = "add_time", fill = FieldFill.INSERT)
    private LocalDateTime addTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("删除flag")
    @TableField(value = "delete_flag")
    private Boolean deleteFlag;

    @ApiModelProperty("浙政钉用户ID")
    @TableField(value = "zzd_user_id")
    private String zzdUserId;
    @ApiModelProperty("浙政钉部门ID")
    @TableField(value = "zzd_dept_id")
    private String zzdDeptId;
    @ApiModelProperty("职务")
    @TableField(value = "post")
    private String post;
    private String salt;
}
