package com.lvxc.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SysRolePageVo {

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

    /**
     * 描述
     */
    @ApiModelProperty(value = "权限描述")
    private String description;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createName;

    /**
     * 添加时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
    private Date createTime;

}
