package com.lvxc.user.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lvxc.mybatisplus.handler.JsonbTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 企业demo表
 * </p>
 *
 * @author lvxc
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
@TableName(value = "flw_demo_company",autoResultMap = true)
@ApiModel(value="DemoCompany对象", description="企业demo表")
public class DemoCompany extends MybatisEntity {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "企业名称")
    @ExcelProperty(index = 0,value = "企业名称")
    @TableField("company_name")
    private String companyName;

    @ApiModelProperty(value = "产业领域")
    @ExcelProperty(index = 1,value = "产业领域")
    @TableField(value = "industry_field",typeHandler = JsonbTypeHandler.class)
    private Object industryField;

    @ApiModelProperty(value = "企业资质")
    @ExcelProperty(index = 2,value = "企业资质")
    @TableField(value = "company_qualification",typeHandler = JsonbTypeHandler.class)
    private Object companyQualification;

    @ApiModelProperty(value = "注册资金")
    @ExcelProperty(index = 3,value = "注册资金")
    @TableField("register_capital")
    private Double registerCapital;

    @ApiModelProperty(value = "经营状态（0 正常 1 异常 2 注销）")
    @ExcelProperty(index = 4,value = "经营状态")
    @TableField("manage_state")
    private Integer manageState;

    @ApiModelProperty(value = "省")
    @ExcelProperty(index = 5,value = "省")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "省code")
    @TableField("province_code")
    private String provinceCode;

    @ApiModelProperty(value = "市")
    @ExcelProperty(index = 6,value = "市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "市code")
    @TableField("city_code")
    private String cityCode;

    @ApiModelProperty(value = "区")
    @ExcelProperty(index = 7,value = "区")
    @TableField("area")
    private String area;

    @ApiModelProperty(value = "区code")
    @TableField("area_code")
    private String areaCode;

    @ApiModelProperty(value = "成立日期")
    @TableField("build_date")
    @ExcelProperty(index = 8,value = "成立日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date buildDate;

    @ApiModelProperty(value = "资质有效期开始时间")
    @TableField("qualification_date_begin")
    @ExcelProperty(index = 9,value = "资质有效期开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date qualificationDateBegin;

    @ApiModelProperty(value = "资质有效期结束时间")
    @TableField("qualification_date_end")
    @ExcelProperty(index = 10,value = "资质有效期结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date qualificationDateEnd;

    @ApiModelProperty(value = "关联部门")
    @TableField("depart_name")
    @ExcelProperty(index = 11,value = "关联部门")
    private String departName;

    @ApiModelProperty(value = "企业介绍")
    @TableField("intro")
    @ExcelProperty(index = 12,value = "企业介绍")
    private String intro;

    @ApiModelProperty(value = "注册资金起始")
    @TableField(exist = false)
    private Double registerCapitalBegin;

    @ApiModelProperty(value = "注册资金结束")
    @TableField(exist = false)
    private Double registerCapitalEnd;

    @ApiModelProperty(value = "排序值，date_desc: 成立日期降序，date_asc :成立日期升序，capital_desc:注册资金降序 capital_asc:注册资金升序")
    @TableField(exist = false)
    private String orderValue;


    public static final String COMPANY_NAME = "company_name";

    public static final String INDUSTRY_FIELD = "industry_field";

    public static final String COMPANY_QUALIFICATION = "company_qualification";

    public static final String REGISTER_CAPITAL = "register_capital";

    public static final String MANAGE_STATE = "manage_state";

    public static final String PROVINCE = "province";

    public static final String PROVINCE_CODE = "province_code";

    public static final String CITY = "city";

    public static final String CITY_CODE = "city_code";

    public static final String AREA = "area";

    public static final String AREA_CODE = "area_code";

    public static final String BUILD_DATE = "build_date";

    public static final String QUALIFICATION_DATE_BEGIN = "qualification_date_begin";

    public static final String QUALIFICATION_DATE_END = "qualification_date_end";

    public static final String DEPART_NAME = "depart_name";

    public static final String INTRO = "intro";


}
