package com.lvxc.zwdd.domain;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 浙政钉用户表
 * </p>
 *
 * @author lvxc
 * @since 2022-11-07
 */
@Data
public class ZwddUser {

    /**
     * 员工Code
     */
    private String employeeCode;

    /**
     * 员工名称
     */
    private String employeeName;

    /**
     * 员工创建时间
     */
    private Date gmtCreate;

    /**
     * 员工性别(男性 1 女性 2 未说明的性别 9 未知的性别 0)
     */
    private String empGender;

    /**
     * 政治面貌
     */
    private String empPoliticalStatusCode;

    /**
     * 职级
     */
    private String empJobLevelCode;

    /**
     * 编制
     */
    private String empBudgetedPostCode;

    /**
     * 人员状态，A 在职； F 离职
     */
    private String status;

    /**
     * 组织范围下的任职信息
     */
    private String govEmployeePositions;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 办公号码
     */
    private String govEmpPosPhoneNo ;

    /**
     * 组织code
     */
    private String organizationCode ;

    /**
     * 职位
     */
    private String govEmpPosJob ;

    /**
     * 组织name
     */
    private String organizationName ;

    /**
     * 组织汇报线
     */
    private String organizationLines;

}
