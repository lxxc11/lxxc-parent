package com.lvxc.zwdd.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 浙政钉组织机构数据
 * </p>
 *
 * @author lvxc
 * @since 2022-11-07
 */
@Data
public class ZwddOrganization {

    private static final long serialVersionUID = 1L;

    /**
     * 组织code
     */
    private String organizationCode;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 排序
     */
    private Long displayOrder;

    /**
     * 父组织Code
     */
    private String parentCode;

    /**
     * 父组织Name
     */
    private String parentName;

    /**
     * 组织状态
     */
    private String status;

    /**
     * 组织类型code
     */
    private String typeCode;

    /**
     * 组织类型名称
     */
    private String typeName;

    /**
     * 组织创建时间
     */
    private Date gmtCreate;

    /**
     * 负责人code，|拼接多个
     */
    private String responsibleEmployeeCodes;

    /**
     * 条线codes,多个用|拼接
     */
    private String businessStripCodes;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系人code
     */
    private String contactEmployeeCode;

    /**
     * 联系人号码
     */
    private String contactNumber;

    /**
     * 行政区划code
     */
    private String divisionCode;

    /**
     * 组织全称
     */
    private String shortName;

    /**
     * 组织机构代码
     */
    private String institutionCode;

    /**
     * 规范化简称
     */
    private String otherName;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;

    /**
     * 机构/单位级别
     */
    private String institutionLevelCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private List<ZwddOrganization> childrens;

    private Boolean isChildren = false;


}
