package com.lvxc.zwdd.constant;

public class ZwddConstant {
    /**
     * 获取应用 access_token
     */
    public static final String GETTOKENAPI = "/gettoken.json";

    /**
     * 服务端通过临时授权码获取授权用户的个人信息
     */
    public static final String GETUSERINFOBYCODE = "/rpc/oauth2/getuserinfo_bycode.json";

    /**
     * 根据authCode换取用户信息
     */
    public static final String GETAPPUSERINFOBYCODE = "/rpc/oauth2/dingtalk_app_user.json";

    /**
     * 查询人员属性字段
     */
    public static final String GETEMPLOYEEPROPERTIESAPI = "/mozi/fusion/getEmployeeProperties";

    /**
     * 请求成功code
     */
    public static final String ZWDD_RESPONSE_CODE = "0";
}
