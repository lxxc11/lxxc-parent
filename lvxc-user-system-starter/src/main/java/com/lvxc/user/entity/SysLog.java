package com.lvxc.user.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lvxc.user.common.easyexcel.converter.LogOperateConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("sys_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "sys_log对象", description = "日志管理")
public class SysLog {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键")
    private String id;

    @ExcelProperty(index = 0,value = "序号")
    @TableField(exist = false)
    private Integer serialNumber;
    /**
     * 平台id
     */
    @ApiModelProperty(value = "平台id")
    private String platformId;

    @ApiModelProperty(value = "平台名称")
    @TableField(exist = false)
    @ExcelProperty(index = 3,value = "系统名称")
    private String platformName;
    /**
     * 日志类型（1登录日志，2操作日志）
     */
    @ApiModelProperty(value = "日志类型（1登录日志，2操作日志）")
    private Integer logType;
    /**
     * 日志内容
     */
    @ApiModelProperty(value = "日志内容")
    private String logContent;
    /**
     * 操作类型（1查询，2添加，3修改，4删除，5导入，6导出，7登录，8退出，9审核）
     */
    @ApiModelProperty(value = "操作类型（1查询，2添加，3修改，4删除，5导入，6导出，7登录，8退出，9审核）")
    @ExcelProperty(index = 6,value = "操作类型",converter = LogOperateConverter.class)
    private Integer operateType;
    /**
     * 操作用户账号
     */
    @ApiModelProperty(value = "操作用户账号")
    @ExcelProperty(index = 7,value = "操作者账号")
    private String userName;
    /**
     * 操作用户名称
     */
    @ApiModelProperty(value = "操作用户名称")
    private String realName;
    /**
     * ip
     */
    @ApiModelProperty(value = "ip")
    @ExcelProperty(index = 8,value = "操作者IP")
    private String ip;
    /**
     * 请求java方法
     */
    @ApiModelProperty(value = "请求java方法")
    @ExcelProperty(index = 4,value = "请求服务类方法")
    private String method;
    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String requestParam;
    /**
     * 请求类型
     */
    @ApiModelProperty(value = "请求类型")
    @ExcelProperty(index = 5,value = "请求类型")
    private String requestType;
    /**
     * 耗时（毫秒 ms）
     */
    @ApiModelProperty(value = "耗时（毫秒 ms）")
    @ExcelProperty(index = 2,value = "耗时（ms）")
    private Long costTime;
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
    @ExcelProperty(index = 1,value = "操作时间")
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

}
