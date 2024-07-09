package com.lvxc.flowable.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.lvxc.user.common.KeyUtils;
import com.lvxc.user.common.Sm2Util;
import com.lvxc.user.entity.SysUser;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.model.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
public class FlowableUserController {

    @Value(value = "${userCenter.url:http://localhost:8888}")
    private String userCentterUrl;

    @GetMapping(value = "/lvxc/rest/editor-users")
    public ResultListDataRepresentation getUsers(@RequestParam(value = "filter", required = false) String filter) {
        String getUserApi = "/user/ext/getUserByFilter";
        String url = userCentterUrl +getUserApi + "?filter=" + filter;
        HttpRequest get = HttpUtil.createGet(url);
        String body = get.execute().body();
        String data = JSONUtil.parseObj(body).get("data").toString();
        List<SysUser> listRemote = JSONUtil.toList(data, SysUser.class);
        List<UserRepresentation> userRepresentations = new ArrayList<>(listRemote.size());
        for(SysUser user : listRemote){
            UserRepresentation userRep = new UserRepresentation();
            PrivateKey privateKey = KeyUtils.createPrivateKey(user.getPrivateKey());
            userRep.setId(user.getId());
            String realName = new String(Sm2Util.decrypt(Base64.getDecoder().decode(user.getRealName()), privateKey));
            userRep.setFullName(realName);
            userRep.setFirstName(user.getUserName());
            userRep.setLastName(realName);
            userRepresentations.add(userRep);
        }
        return new ResultListDataRepresentation(userRepresentations);
    }
}
