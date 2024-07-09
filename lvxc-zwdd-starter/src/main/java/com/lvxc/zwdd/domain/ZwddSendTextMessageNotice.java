package com.lvxc.zwdd.domain;

import lombok.Data;

/**
 * @author zhangl
 * @description: 浙政钉发送普通消息通知的类
 * @date 2023/12/27
 */
@Data
public class ZwddSendTextMessageNotice {

    /**
     * 接收者的部门id列表， 接收者是部门id下(包括子部门下)的所有用户，与receiverIds都为空时不发送，最大长度列表跟receiverIds加起来不大于1000
     */
    private String organizationCodes;

    /**
     * 接收人用户ID(accountId)， 多个人时使用半角逗号分隔，与organizationCodes都为空时不发送，最大长度列表跟organizationCodes加起来不大于1000
     */
    private String receiverIds;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 业务消息id，自定义，有去重功能 调用者的业务数据ID，同样的ID调用多次会提示'重复'错误-可以使用uuid
     */
    private String bizMsgId;

//    /**
//     * 消息标题【比如：发送消息的平台名称】
//     */
//    private String title;

    /**
     * 消息内容
     */
    private String msg;

//    /**
//     * 资讯消息的详情地址
//     */
//    private String detailUrl;

}
