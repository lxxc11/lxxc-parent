package com.lvxc.zwdd.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.xxpt.gateway.shared.api.request.*;
import com.alibaba.xxpt.gateway.shared.api.response.*;
import com.alibaba.xxpt.gateway.shared.client.http.*;
import com.alibaba.xxpt.gateway.shared.client.http.api.OapiSpResultContent;
import com.lvxc.zwdd.configuration.ZwddConfig;
import com.lvxc.zwdd.constant.ZwddConstant;
import com.lvxc.zwdd.domain.*;
import com.lvxc.zwdd.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhangl
 * @description: 浙政钉工具类
 * @date 2023/9/15
 */
@Slf4j
@Component
public class ZwddUtils {

    @Autowired
    ExecutableClient appExecutableClient;

    @Autowired
    private ZwddConfig zwddConfig;

    /**
     * PC端 - 获取用户信息【用于PC端的单点登录】
     *
     * @param authCode 前端传参authCode
     * @return
     */
    public ZwddUserInfo getPcUserInfo(String authCode) {
        log.info("############## PC端 getUserInfo 获取参数:" + authCode);
        GetClient getClient = appExecutableClient.newGetClient(ZwddConstant.GETTOKENAPI);
        String apiResult = getClient.get();
        String content = JSONObject.parseObject(apiResult).get("content").toString();
        ZwddResponse response = JSONObject.parseObject(content, ZwddResponse.class);
        ZwddAccessToken zwddAccessToken = JSONObject.parseObject(response.getData(), ZwddAccessToken.class);

        PostClient postClient = appExecutableClient.newPostClient(ZwddConstant.GETUSERINFOBYCODE);
        postClient.addParameter("access_token", zwddAccessToken.getAccessToken());
        postClient.addParameter("code", authCode);
        String post = postClient.post();
        log.info("&&&&&&&&&&&&&& PC端获取远程请求结果：{}", post);
        String userContent = JSONObject.parseObject(post).get("content").toString();
        ZwddResponse responseUser = JSONObject.parseObject(userContent, ZwddResponse.class);
        if (ZwddConstant.ZWDD_RESPONSE_CODE.equals(responseUser.getResponseCode())) {
            ZwddUserInfo zwddUserInfo = JSONObject.parseObject(responseUser.getData(), ZwddUserInfo.class);
            return zwddUserInfo;
        }
        return null;
    }

    /**
     * 移动端 - 获取用户信息【用于移动端的单点登录】
     *
     * @param authCode 前端传参authCode
     * @return
     */
    public ZwddUserInfo getAppUserInfos(String authCode) {
        log.info("############## 移动端 getAppUserInfos 获取参数:" + authCode);
        GetClient getClient = appExecutableClient.newGetClient(ZwddConstant.GETTOKENAPI);
        String apiResult = getClient.get();
        log.info(apiResult);
        String content = JSONObject.parseObject(apiResult).get("content").toString();
        ZwddResponse response = JSONObject.parseObject(content, ZwddResponse.class);
        ZwddAccessToken zwddAccessToken = JSONObject.parseObject(response.getData(), ZwddAccessToken.class);

        PostClient postClient = appExecutableClient.newPostClient(ZwddConstant.GETAPPUSERINFOBYCODE);
        postClient.addParameter("access_token", zwddAccessToken.getAccessToken());
        postClient.addParameter("auth_code", authCode);
        String post = postClient.post();
        String userContent = JSONObject.parseObject(post).get("content").toString();
        ZwddResponse responseUser = JSONObject.parseObject(userContent, ZwddResponse.class);
        if (ZwddConstant.ZWDD_RESPONSE_CODE.equals(responseUser.getResponseCode())) {
            ZwddUserInfo zwddUserInfo = JSONObject.parseObject(responseUser.getData(), ZwddUserInfo.class);
            return zwddUserInfo;
        }
        return null;
    }

    /**
     * 根据组织code获取组织下的用户列表
     *
     * @param organizationCode
     * @return
     */
    public String searchOrgEmployeeList(String organizationCode) {
        log.info("******************* 组织code： " + organizationCode);
        IntelligentGetClient intelligentGetClient = appExecutableClient.newIntelligentGetClient("/mozi/organization/pageOrganizationEmployeePositions");
        OapiMoziOrganizationPageOrganizationEmployeePositionsRequest oapiMoziOrganizationPageOrganizationEmployeePositionsRequest = new OapiMoziOrganizationPageOrganizationEmployeePositionsRequest();
        //是否请求总数，默认是false
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setReturnTotalSize(true);
        //分页大小，默认是20，最大100
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setPageSize(100);
        //员工状态，A为有效，F为无效，默认是所有
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setEmployeeStatus("A");
        //组织code
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setOrganizationCode(organizationCode);
        //请求起始页，默认是1
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setPageNo(1);
        //租户id
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setTenantId(Long.valueOf(zwddConfig.getTenantid()));
        //获取结果
        OapiMoziOrganizationPageOrganizationEmployeePositionsResponse apiResult = intelligentGetClient.get(oapiMoziOrganizationPageOrganizationEmployeePositionsRequest);
        log.info("********* 获取用户组织结果code： {}", apiResult.getCode());
        log.info("********* 获取用户组织结果message： {}", apiResult.getMessage());
        log.info("********* 获取用户组织结果content： {}", apiResult.getContent());
        log.info("********* 获取用户信息响应内容ResponseCode： {}", apiResult.getContent().getResponseCode());
        log.info("********* 获取用户信息响应内容ResponseMessage： {}", apiResult.getContent().getResponseMessage());
        return apiResult.getContent().getData();
    }


    //   参考杭州产业智理平台的更新组织和更新用户的步骤
    //  *************************************** 用于组织更新【demo要参考这个：com.lvxc.index.service.zwdd.impl.ZwddOrganizationServiceImpl#updateZwddOrganization】 ***********
    //  ######################### 组织更新相关接口开始 #########################
    //  1 ********************* 获取通讯录的权限范围【获取有权限的组织】 ***************************

    /**
     * 1.获取通讯录的部门权限范围-【企业授权的部门编码列表。返回值为授权部门编码的并集 (设置“全部员工”时，返回授权的部门编码为 ***根部门ID*** )】
     *
     * @return
     */
    public List<String> getAddressBookDeptPermissions() {
        //获取通讯录权限范围【**************  获取通讯录用户信息中的 deptVisibleScopes 部门范围列表  *************】
        IntelligentGetClient intelligentGetClient = appExecutableClient.newIntelligentGetClient("/auth/scopesV2");
        OapiAuthScopesV2Request oapiAuthScopesV2Request = new OapiAuthScopesV2Request();
        oapiAuthScopesV2Request.setTenantId(Long.valueOf(Long.valueOf(zwddConfig.getTenantid())));
        OapiAuthScopesV2Response apiResult = intelligentGetClient.get(oapiAuthScopesV2Request);
        String content = apiResult.getContent();
        String deptVisibleScopes = JSONObject.parseObject(content).get("deptVisibleScopes").toString();
        List<String> list = JSONObject.parseObject(deptVisibleScopes, List.class);
        return list;
    }

    //  2 ********************* 根据组织code，获取组织详情信息 ***************************

    /**
     * 2.根据组织code列表，获取对应的所有组织的详情，并给组织列表赋值 - 父组织code
     *
     * @param pCode       组织列表的父组织code
     * @param orgCodeList 组织code列表
     * @return
     */
    public List<ZwddOrganization> getOrgDetailByOrgCodes(String pCode, List<String> orgCodeList) {
        //批量根据组织code获取详情
        IntelligentPostClient intelligentPostClient = appExecutableClient.newIntelligentPostClient("/mozi/organization/listOrganizationsByCodes");
        OapiMoziOrganizationListOrganizationsByCodesRequest oapiMoziOrganizationListOrganizationsByCodesRequest = new OapiMoziOrganizationListOrganizationsByCodesRequest();
        oapiMoziOrganizationListOrganizationsByCodesRequest.setOrganizationCodes(orgCodeList);
        oapiMoziOrganizationListOrganizationsByCodesRequest.setTenantId(Long.valueOf(zwddConfig.getTenantid()));
        //获取结果
        OapiMoziOrganizationListOrganizationsByCodesResponse apiResult = intelligentPostClient.post(oapiMoziOrganizationListOrganizationsByCodesRequest);
        String data = apiResult.getContent().getData();
        List<ZwddOrganization> listDetails = JSONObject.parseArray(data, ZwddOrganization.class);
        // 给当前组织列表赋值 父组织code
        listDetails.forEach(f -> {
            f.setParentCode(pCode);
        });
        return listDetails;
    }

    //  3 ********************* 根据组织code，获取子组织详情信息 ***************************

    /**
     * 3.获取当前组织的下级组织code列表
     *
     * @param orgCode 当前组织
     * @return 下级组织的code列表
     */
    public List<String> getSubOrgCodes(String orgCode) {
        IntelligentGetClient intelligentGetClient2 = appExecutableClient.newIntelligentGetClient("/mozi/organization/pageSubOrganizationCodes");
        OapiMoziOrganizationPageSubOrganizationCodesRequest oapiMoziOrganizationPageSubOrganizationCodesRequest = new OapiMoziOrganizationPageSubOrganizationCodesRequest();
        oapiMoziOrganizationPageSubOrganizationCodesRequest.setReturnTotalSize(true);
        oapiMoziOrganizationPageSubOrganizationCodesRequest.setPageSize(100);
        oapiMoziOrganizationPageSubOrganizationCodesRequest.setOrganizationCode(orgCode);
        oapiMoziOrganizationPageSubOrganizationCodesRequest.setPageNo(1);
//            //查询有效数据
//            oapiMoziOrganizationPageSubOrganizationCodesRequest.setStatus("A");
        oapiMoziOrganizationPageSubOrganizationCodesRequest.setTenantId(Long.valueOf(zwddConfig.getTenantid()));
        //获取结果
        OapiMoziOrganizationPageSubOrganizationCodesResponse apiResult2 = intelligentGetClient2.get(oapiMoziOrganizationPageSubOrganizationCodesRequest);
        log.info("请求结果解析code： {}", apiResult2.getCode());
        log.info("请求结果解析message： {}", apiResult2.getMessage());
        log.info("请求结果解析contentResponseCode： {}", apiResult2.getContent().getResponseCode());
        log.info("请求结果解析contentResponseMessage： {}", apiResult2.getContent().getResponseMessage());
        String data = apiResult2.getContent().getData();
        List<String> subList = JSONObject.parseObject(data, List.class);
        return subList;
    }
    //  ######################### 组织更新相关接口结束 #########################


    //   *************************************** 用于用户更新 【demo参考：com.lvxc.index.service.zwdd.impl.ZwddUserServiceImpl#updateZwddUser】***********************************************
    //   1 ********************* 根据组织code查询组织下的人员详情信息【包含职位信息等】 ***********************

    /**
     * 获取组织下人员的详情信息 - 包含职位信息
     *
     * @param organizationCode
     * @param organizationName
     * @return
     */
    public List<ZwddUser> getUsersDetailByOrgCode(String organizationCode, String organizationName) {
        log.info("********************************** 根据组织获取用户信息参数&&&&&&&&： {}", organizationName, organizationCode);
        IntelligentGetClient intelligentGetClient = appExecutableClient.newIntelligentGetClient("/mozi/organization/pageOrganizationEmployeePositions");
        OapiMoziOrganizationPageOrganizationEmployeePositionsRequest oapiMoziOrganizationPageOrganizationEmployeePositionsRequest = new OapiMoziOrganizationPageOrganizationEmployeePositionsRequest();
        //是否请求总数，默认是false
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setReturnTotalSize(true);
        //分页大小，默认是20，最大100
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setPageSize(100);
        //员工状态，A为有效，F为无效，默认是所有
//        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setEmployeeStatus("A");
        //组织code
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setOrganizationCode(organizationCode);
        //请求起始页，默认是1
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setPageNo(1);
        //租户id
        oapiMoziOrganizationPageOrganizationEmployeePositionsRequest.setTenantId(Long.valueOf(zwddConfig.getTenantid()));
        //获取结果
        OapiMoziOrganizationPageOrganizationEmployeePositionsResponse apiResult = intelligentGetClient.get(oapiMoziOrganizationPageOrganizationEmployeePositionsRequest);
        // todo 日志打印
        log.info("********* 获取用户组织结果code： {}", apiResult.getCode());
        log.info("********* 获取用户组织结果message： {}", apiResult.getMessage());
        log.info("********* 获取用户信息响应内容ResponseCode： {}", apiResult.getContent().getResponseCode());
        log.info("********* 获取用户信息响应内容ResponseMessage： {}", apiResult.getContent().getResponseMessage());
        List<ZwddUser> result = new ArrayList<>();
        List<ZwddUser> listDetails = JSONObject.parseArray(apiResult.getContent().getData(), ZwddUser.class);
        if (!CollectionUtils.isEmpty(listDetails)) {
            List<String> collect = listDetails.stream().map(ZwddUser::getEmployeeCode).collect(Collectors.toList());
            IntelligentGetClient intelligentGetClientDetail = appExecutableClient.newIntelligentGetClient("/mozi/employee/listOrgEmployeePositionsByCodes");
            OapiMoziEmployeeListOrgEmployeePositionsByCodesRequest oapiMoziEmployeeListOrgEmployeePositionsByCodesRequest = new OapiMoziEmployeeListOrgEmployeePositionsByCodesRequest();
            oapiMoziEmployeeListOrgEmployeePositionsByCodesRequest.setEmployeeCodes(collect);
            oapiMoziEmployeeListOrgEmployeePositionsByCodesRequest.setOrganizationCode(organizationCode);
            oapiMoziEmployeeListOrgEmployeePositionsByCodesRequest.setTenantId(Long.valueOf(zwddConfig.getTenantid()));
            //获取结果
            OapiMoziEmployeeListOrgEmployeePositionsByCodesResponse apiResultDatail = intelligentGetClientDetail.get(oapiMoziEmployeeListOrgEmployeePositionsByCodesRequest);
            OapiSpResultContent content = apiResultDatail.getContent();
            if (Objects.isNull(content)) {
                return new ArrayList<>();
            }
            String data = content.getData();
            List<ZwddUser> details = JSONObject.parseArray(data, ZwddUser.class);
            Map<String, ZwddUser> collect1 = details.stream().collect(Collectors.toMap(ZwddUser::getEmployeeCode, employeeDetail -> employeeDetail));
            for (ZwddUser employeeDetail : listDetails) {
                List<EmployeePositions> employeePositions1 = JSONObject.parseArray(employeeDetail.getGovEmployeePositions(), EmployeePositions.class);
                if (CollectionUtils.isEmpty(employeePositions1)) {
                    continue;
                }
                //主职列表
                List<EmployeePositions> collect3 = employeePositions1.stream().filter(l -> l.getMainJob() != null && l.getMainJob()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(collect3)) {
                    continue;
                }
                //只更新主职职务
                employeeDetail.setOrganizationCode(organizationCode);
                employeeDetail.setOrganizationName(organizationName);
                employeeDetail.setGovEmpPosPhoneNo(collect1.get(employeeDetail.getEmployeeCode()) != null ? collect1.get(employeeDetail.getEmployeeCode()).getGovEmpPosPhoneNo() : "");
                List<String> collect2 = employeePositions1.stream().filter(employeePositions -> employeePositions.getGovEmpPosJob() != null).map(EmployeePositions::getGovEmpPosJob).collect(Collectors.toList());
                employeeDetail.setGovEmpPosJob(CollectionUtils.isEmpty(collect2) ? "" : String.join(";", collect2));
                result.add(employeeDetail);
            }
        }
        return result;
    }

    /**
     * 普通文本消息通知发送
     *
     * @param messageNotice 消息传参
     * @return
     */
    public OapiMessageWorkNotificationResponse sendTextMessageNotice(ZwddSendTextMessageNotice messageNotice) {
        log.info("############################## 发送普通文本消息通知开始 {}", messageNotice);
        textCheck(messageNotice);
        //executableClient保证单例
        IntelligentGetClient intelligentGetClient = appExecutableClient.newIntelligentGetClient("/message/workNotification");
        OapiMessageWorkNotificationRequest oapiMessageWorkNotificationRequest = new OapiMessageWorkNotificationRequest();
        // 接收者的部门id列表  OrganizationCodes和ReceiverIds 存在一个即可
        if (StringUtils.hasLength(messageNotice.getOrganizationCodes())) {
            oapiMessageWorkNotificationRequest.setOrganizationCodes(messageNotice.getOrganizationCodes());
        }
        //接收人用户ID
        if (StringUtils.hasLength(messageNotice.getReceiverIds())) {
            oapiMessageWorkNotificationRequest.setReceiverIds(messageNotice.getReceiverIds());
        }
        //租户ID
        oapiMessageWorkNotificationRequest.setTenantId(messageNotice.getTenantId());
        //业务消息id
        oapiMessageWorkNotificationRequest.setBizMsgId(messageNotice.getBizMsgId());
        //消息对象
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        object.put("msgtype","text");
        object1.put("content",messageNotice.getMsg());
        object.put("text",object1);
        oapiMessageWorkNotificationRequest.setMsg(JSONObject.toJSONString(object));
        //获取结果
        OapiMessageWorkNotificationResponse apiResult = intelligentGetClient.get(oapiMessageWorkNotificationRequest);
        log.info("############################## 发送普通文本消息结束 {}",apiResult);
        //消息对象
        return apiResult;
    }

    /**
     * 发送资讯消息通知
     *
     * @param messageNotice
     * @return
     */
    public OapiMessageWorkNotificationResponse sendInfoMessageNotice(ZwddSendInfoMessageNotice messageNotice) {
        infoCheck(messageNotice);
        log.info("############################## 发送资讯类消息通知开始 {}", messageNotice);
        //executableClient保证单例
        IntelligentGetClient intelligentGetClient = appExecutableClient.newIntelligentGetClient("/message/workNotification");
        OapiMessageWorkNotificationRequest oapiMessageWorkNotificationRequest = new OapiMessageWorkNotificationRequest();
        // 接收者的部门id列表  OrganizationCodes和ReceiverIds 存在一个即可
        if (StringUtils.hasLength(messageNotice.getOrganizationCodes())) {
            oapiMessageWorkNotificationRequest.setOrganizationCodes(messageNotice.getOrganizationCodes());
        }
        //接收人用户ID
        if (StringUtils.hasLength(messageNotice.getReceiverIds())) {
            oapiMessageWorkNotificationRequest.setReceiverIds(messageNotice.getReceiverIds());
        }
        //租户ID
        oapiMessageWorkNotificationRequest.setTenantId(messageNotice.getTenantId());
        //业务消息id
        oapiMessageWorkNotificationRequest.setBizMsgId(messageNotice.getBizMsgId());
        //消息对象
        JSONObject object = new JSONObject();
        JSONObject object1 = new JSONObject();
        object1.put("title",messageNotice.getTitle());
        object1.put("markdown",messageNotice.getMsg());
        object1.put("btn_orientation","1");

        if (StringUtils.hasLength(messageNotice.getDetailUrl())) {
            JSONArray array = new JSONArray();
            JSONObject obj2 = new JSONObject();
            obj2.put("title", "查看详情");
            obj2.put("action_url", messageNotice.getDetailUrl());
            array.add(obj2);
            object1.put("btn_json_list",array);
        }

        object.put("msgtype","action_card");
        object.put("action_card",object1);
        oapiMessageWorkNotificationRequest.setMsg(JSONObject.toJSONString(object));

        OapiMessageWorkNotificationResponse apiResult = intelligentGetClient.get(oapiMessageWorkNotificationRequest);
        log.info("############################## 发送资讯类消息通知结束 {}", apiResult);
        return apiResult;
    }

    private void infoCheck(ZwddSendInfoMessageNotice messageNotice) {
        if (StringUtils.hasLength(messageNotice.getOrganizationCodes()) && StringUtils.hasLength(messageNotice.getReceiverIds())) {
            throw new RuntimeException("接收部门和接收人不能同时有值！");
        }
        if (!StringUtils.hasLength(messageNotice.getOrganizationCodes()) && !StringUtils.hasLength(messageNotice.getReceiverIds())) {
            throw new RuntimeException("接收部门和接收人不能同时为空！");
        }
        if (!StringUtils.hasLength(messageNotice.getTenantId())) {
            throw new RuntimeException("tenantId租户id不能是空！");
        }
        if (!StringUtils.hasLength(messageNotice.getBizMsgId())) {
            throw new RuntimeException("bizMsgId消息id不能是空！");
        }
        if (!StringUtils.hasLength(messageNotice.getTitle())) {
            throw new RuntimeException("title消息标题不能是空！");
        }
        if (!StringUtils.hasLength(messageNotice.getMsg())) {
            throw new RuntimeException("msg消息内容不能是空！");
        }
    }

    private void textCheck(ZwddSendTextMessageNotice messageNotice) {
        if (StringUtils.hasLength(messageNotice.getOrganizationCodes()) && StringUtils.hasLength(messageNotice.getReceiverIds())) {
            throw new RuntimeException("接收部门和接收人不能同时有值！");
        }
        if (!StringUtils.hasLength(messageNotice.getOrganizationCodes()) && !StringUtils.hasLength(messageNotice.getReceiverIds())) {
            throw new RuntimeException("接收部门和接收人不能同时为空！");
        }
        if (!StringUtils.hasLength(messageNotice.getTenantId())) {
            throw new RuntimeException("tenantId租户id不能是空！");
        }
        if (!StringUtils.hasLength(messageNotice.getBizMsgId())) {
            throw new RuntimeException("bizMsgId消息id不能是空！");
        }
        if (!StringUtils.hasLength(messageNotice.getMsg())) {
            throw new RuntimeException("msg消息内容不能是空！");
        }
    }

}
