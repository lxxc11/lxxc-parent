package com.lvxc.user.common.shiro.realm;

import com.alibaba.fastjson.JSON;
import com.lvxc.user.common.HttpUtil;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.shiro.JwtToken;
import com.lvxc.user.domain.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Component
public class JwtUserRealm extends AuthorizingRealm {

    @Value(value = "${userCenter.url:http://localhost:8888}")
    private String userCentterUrl;

    private final  String getLoginUserByNameApi = "/user/getLoginUserByName";

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken || token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.debug("===============Shiro身份认证开始============doGetAuthenticationInfo==========");
        String tokenStr = (String) token.getCredentials();
        if (StringUtils.isEmpty(tokenStr)) {
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            log.info("————————身份认证失败——————————IP地址:  " + getIpAddrByRequest(req) + "，URL:" + req.getRequestURI());
            throw new AuthenticationException("token为空!");
        }
        LoginUser loginUser = null;
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("tokenStr", tokenStr);
            String url = userCentterUrl+getLoginUserByNameApi;
            ResponseResult result = HttpUtil.getRequestByUrlencoded(url, params, ResponseResult.class, 3);
            if (!result.isSuccess()) {
                log.error("验证失败："+result.getMessage());
                throw new AuthenticationException(result.getMessage());
            }
            loginUser = JSON.parseObject(JSON.toJSONString(result.getData()), LoginUser.class);
        } catch (InterruptedException e) {
            throw new AuthenticationException(e);
        }
        log.info("登录的用户:" + loginUser.getUserName());
        return new SimpleAuthenticationInfo(loginUser, tokenStr, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // 角色列表
        Set<String> roles = new HashSet<String>();
        // 功能列表
        Set<String> menus = new HashSet<String>();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (loginUser.getSuperFlag()) {
            info.addRole("admin");
            info.addStringPermission("*:*");
            info.addStringPermission("*:*:*");
            info.addStringPermission("*:*:*:*");
            info.addStringPermission("*:*:*:*:*");
        } else {
            //TODO 权限菜单
//            roles = roleService.selectRoleKeys(user.getUserId());
//            menus = menuService.selectPermsByUserId(user.getUserId());
            // 角色加入AuthorizationInfo认证对象
            info.setRoles(roles);
            // 权限加入AuthorizationInfo认证对象
            info.setStringPermissions(loginUser.getFunList());
        }
        return info;
    }

    public static String getIpAddrByRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
