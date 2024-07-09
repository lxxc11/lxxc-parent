package com.lvxc.zwdd.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZwddUserInfo implements Serializable {

    /**
     * 	账号id
     */
    private Long accountId ;
    /**
     * 姓名
     */
    private String lastName ;
    /**
     * 应用标识
     */
    private String clientId ;
    /**
     * 租户id
     */
    private Long realmId ;
    /**
     * 租户名
     */
    private String tenantName ;
    /**
     * 租户名
     */
    private String realmName ;
    /**
     * 账号类型标识
     */
    private String namespace ;
    /**
     * 租户id
     */
    private Long tenantId ;
    /**
     * 昵称
     */
    private String nickNameCn ;
    /**
     * 员工在当前企业内的唯一标识
     */
    private String tenantUserId ;
    /**
     * 登录账号
     */
    private String account ;
    /**
     * 人员code(如果是超管账号调用扫码登入返回结果中无employeeCode，普通账号调用扫码登入接口有。)
     */
    private String employeeCode ;

    /**
     * 杭州智理的token
     */
    private String token;
}
