package com.lvxc.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class SysUserVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
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
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private String departId;
    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String position;
    /**
     * 兼职部门id
     */
    @ApiModelProperty(value = "兼职部门id")
    private List<String> partTimeDepartIds;
    /**
     * 有效时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "有效时间")
    private Date effectiveTime;
    /**
     * 启用状态（false停用，true启用）
     */
    @ApiModelProperty(value = "启用状态（false停用，true启用）")
    private Boolean status;
    /**
     * 角色ids
     */
    @ApiModelProperty(value = "角色ids")
    private List<String> roleIds;
    /**
     * SM2私钥
     */
    @JsonIgnore
    @ApiModelProperty(value = "SM2私钥")
    private String privateKey;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String headPicture;


}
