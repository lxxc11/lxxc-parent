package com.lvxc.user.common.shiro.filter;

import com.lvxc.user.common.base.ResultEnum;
import com.lvxc.user.common.shiro.JwtToken;
import com.lvxc.user.common.shiro.constants.ShiroConstants;
import com.lvxc.user.common.shiro.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 鉴权登录拦截器
 * @author: Feng
 * @date: 2022年04月20日 18:17
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    private PathMatcher pathMatcher = new AntPathMatcher();
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    /**
     * 默认开启跨域设置（使用单体）
     * 微服务情况下，此属性设置为false
     */
    private boolean allowOrigin = true;

    public JwtFilter() {
    }

    public JwtFilter(boolean allowOrigin) {
        this.allowOrigin = allowOrigin;
    }

    /**
     * 执行登录认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            executeLogin(request, response);
            return true;
        } catch (Exception e) {
            log.error("executeLogin 发生异常!!" + e.getMessage(), e);
            JwtUtil.responseError(response, ResultEnum.TOKEN_IS_INVALID_MSG);
            return false;
        }
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(ShiroConstants.TOKEN);
//        String loginType = httpServletRequest.getHeader(ShiroConstants.LOGIN_TYPE);
        if (StringUtils.isEmpty(token)) {
            token = httpServletRequest.getParameter(ShiroConstants.TOKEN);
        }
//        if (Objects.equals(ShiroConstants.EXPERT, loginType)) {
//            ExpertToken expertToken = new ExpertToken(token);
//            getSubject(request, response).login(expertToken);
//        } else {
            JwtToken jwtToken = new JwtToken(token);
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            getSubject(request, response).login(jwtToken);
//        }
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (allowOrigin) {
            httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
            //update-begin-author:scott date:20200907 for:issues/I1TAAP 前后端分离，shiro过滤器配置引起的跨域问题
            // 是否允许发送Cookie，默认Cookie不包括在CORS请求之中。设为true时，表示服务器允许Cookie包含在请求中。
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        }
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equalsIgnoreCase(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}