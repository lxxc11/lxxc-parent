package com.lvxc.user.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * demo表结构
 * </p>
 *
 * @author lvxc
 * @since 2023-08-16
 */
@Getter
@Setter
@TableName("flw_demo_table")
@ApiModel(value = "DemoTable对象", description = "demo表结构")
@ExcelIgnoreUnannotated
public class DemoTable extends MybatisEntity{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("文本")
    @TableField("text_content")
    @ExcelProperty(index = 0,value = "文本")
    private String textContent;

    @ApiModelProperty("密码")
    @TableField("pwd_content")
    @ExcelProperty(index = 1,value = "密码")
    private String pwdContent;

    @ApiModelProperty("手机号")
    @TableField("cell_content")
    @ExcelProperty(index = 2,value = "手机号")
    private String cellContent;

    @ApiModelProperty("数字")
    @ExcelProperty(index = 3,value = "数字")
    @TableField("num_content")
    private Float numContent;

    @ApiModelProperty("复选框")
    @TableField("checkbox_content")
    @ExcelProperty(index = 4,value = "复选框")
    private String checkboxContent;

    @ApiModelProperty("单选框")
    @TableField("radiobox_content")
    @ExcelProperty(index = 5,value = "单选框")
    private String radioboxContent;

    @ApiModelProperty("输入框")
    @TableField("inputfield_content")
    @ExcelProperty(index = 6,value = "输入框")
    private String inputfieldContent;

    @ApiModelProperty("日期选择器开始时间")
    @TableField("begin_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(index = 7,value = "日期选择器开始时间")
    private Date beginTime;

    @ApiModelProperty("日期选择器结束时间")
    @TableField("end_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(index = 8,value = "日期选择器结束时间")
    private Date endTime;

    @ApiModelProperty("富文本")
    @TableField("richtext_content")
    @ExcelProperty(index = 9,value = "富文本")
    private String richtextContent;

    @ApiModelProperty("日期选择器")
    @TableField("time_content")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(index = 10,value = "日期选择器")
    private Date timeContent;

    public static final String ID = "id";

    public static final String TEXT_CONTENT = "text_content";

    public static final String PWD_CONTENT = "pwd_content";

    public static final String CELL_CONTENT = "cell_content";

    public static final String NUM_CONTENT = "num_content";

    public static final String CHECKBOX_CONTENT = "checkbox_content";

    public static final String RADIOBOX_CONTENT = "radiobox_content";

    public static final String INPUTFIELD_CONTENT = "inputfield_content";

    public static final String BEGIN_TIME = "begin_time";

    public static final String END_TIME = "end_time";

    public static final String RICHTEXT_CONTENT = "richtext_content";

    public static final String TIME_CONTENT = "time_content";
}
