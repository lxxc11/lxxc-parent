package com.lvxc.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class SysDepartVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 父id
     */
    @ApiModelProperty(value = "父id")
    private String parentId;
    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departName;
    /**
     * 类型（1地区 2部门）
     */
    @ApiModelProperty(value = "类型（1地区 2部门）")
    private Integer type;
    /**
     * 添加时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
    /**
     * 添加人的用户id
     */
    @ApiModelProperty(value = "添加人的用户id")
    private String createBy;
    /**
     * 添加人的用户名称
     */
    @ApiModelProperty(value = "添加人的用户名称")
    private String createName;
    /**
     * 用户列表
     */
    @ApiModelProperty(value = "用户列表")
    private List<SysDepartUserVo> userList;
    /**
     * 子部门列表
     */
    @ApiModelProperty(value = "子部门列表")
    private List<SysDepartVo> children;

    /**
     * 行政级别
     */
    @ApiModelProperty(value = "行政级别")
    private String districtLevel;


}
