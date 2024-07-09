package com.lvxc.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 行政区划表
 * </p>
 *
 * @author lvxc
 * @since 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dim_stds_administrative_division")
@ApiModel(value="AdministrativeDivision对象", description="行政区划表")
public class AdministrativeDivision extends MybatisEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否逻辑删除")
    @TableField("is_delete")
    private Integer isDelete;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "行政区编码")
    @TableField("district_code")
    private String districtCode;

    @ApiModelProperty(value = "行政区名称")
    @TableField("district_name")
    private String districtName;

    @ApiModelProperty(value = "省份")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "城市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "区县")
    @TableField("area")
    private String area;

    @ApiModelProperty(value = "上一级行政区编码")
    @TableField("upper_level_district_code")
    private String upperLevelDistrictCode;

    @ApiModelProperty(value = "电话区号")
    @TableField("phone_area_code")
    private String phoneAreaCode;

    @ApiModelProperty(value = "行政区政府驻地")
    @TableField("gov_residence")
    private String govResidence;

    @ApiModelProperty(value = "七大地理分区")
    @TableField("geo_division")
    private String geoDivision;

    @ApiModelProperty(value = "省份代码")
    @TableField("province_code")
    private String provinceCode;

    @ApiModelProperty(value = "城市代码")
    @TableField("city_code")
    private String cityCode;

    @ApiModelProperty(value = "区县代码")
    @TableField("area_code")
    private String areaCode;


    public static final String IS_DELETE = "is_delete";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DISTRICT_CODE = "district_code";

    public static final String DISTRICT_NAME = "district_name";

    public static final String PROVINCE = "province";

    public static final String CITY = "city";

    public static final String AREA = "area";

    public static final String UPPER_LEVEL_DISTRICT_CODE = "upper_level_district_code";

    public static final String PHONE_AREA_CODE = "phone_area_code";

    public static final String GOV_RESIDENCE = "gov_residence";

    public static final String GEO_DIVISION = "geo_division";

    public static final String PROVINCE_CODE = "province_code";

    public static final String CITY_CODE = "city_code";

    public static final String AREA_CODE = "area_code";

}
