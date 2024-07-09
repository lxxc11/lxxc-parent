package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 实体类父类
 * @Author: mengy
 * @Date: 2023-04-07
 * @Version: V1.0
 */
@Data
public class Entity implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 添加人的用户id
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "添加人的用户id")
    private String createBy;
    /**
     * 添加时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
    private Date createTime;
    /**
     * 最后一个人更新的id
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后一个人更新的id")
    private String updateBy;
    /**
     * 最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;
    /**
     * 1删除  0未删除
     */
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "t删除  f未删除")
    private Boolean delFlag;

}
