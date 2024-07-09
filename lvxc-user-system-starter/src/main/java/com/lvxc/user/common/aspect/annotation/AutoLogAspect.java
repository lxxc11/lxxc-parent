package com.lvxc.user.common.aspect.annotation;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.lvxc.user.common.SpringContextUtils;
import com.lvxc.user.common.aspect.AutoLog;
import com.lvxc.user.domain.vo.LoginUser;
import com.lvxc.user.entity.SysLog;
import com.lvxc.user.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Slf4j
@Aspect
@Component
public class AutoLogAspect {

    @Resource
    private ISysLogService sysLogService;


    @Pointcut("@annotation(com.lvxc.user.common.aspect.AutoLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        saveSysLog(point, time, result);
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, Object obj) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLog syslog = new SysLog();
        AutoLog autoLog = method.getAnnotation(AutoLog.class);
        if (autoLog != null) {
            // 注解上的描述,操作日志内容
            syslog.setLogType(autoLog.logType());
            syslog.setLogContent(autoLog.value());
            syslog.setOperateType(autoLog.operateType());
        }

        // 获取登录用户信息
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (loginUser != null) {
            syslog.setPlatformId(loginUser.getPlatformId());
            syslog.setUserName(loginUser.getUserName());
            syslog.setRealName(loginUser.getRealName());
            syslog.setCreateBy(loginUser.getId());
            syslog.setUpdateBy(loginUser.getId());
        }

        // 获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        // 设置IP地址
        syslog.setIp(getIpAddr(request));

        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        syslog.setMethod(className + "." + methodName + "()");

        // 请求的参数
        syslog.setRequestParam(getReqestParams(request, joinPoint));

        // 请求类型
        syslog.setRequestType(request.getMethod());

        // 耗时
        syslog.setCostTime(time);
        syslog.setCreateTime(new Date());
        syslog.setUpdateTime(new Date());
        // 保存系统日志
        sysLogService.save(syslog);
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        return ip;
    }

    /**
     * @param request:   request
     * @param joinPoint: joinPoint
     * @Description: 获取请求参数
     * @author: scott
     * @date: 2020/4/16 0:10
     * @Return: java.lang.String
     */
    private String getReqestParams(HttpServletRequest request, JoinPoint joinPoint) {
        String httpMethod = request.getMethod();
        String params = "";
        if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
            Object[] paramsArray = joinPoint.getArgs();
            // java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
            //  https://my.oschina.net/mengzhang6/blog/2395893
            Object[] arguments = new Object[paramsArray.length];
            for (int i = 0; i < paramsArray.length; i++) {
                if (paramsArray[i] instanceof BindingResult || paramsArray[i] instanceof ServletRequest || paramsArray[i] instanceof ServletResponse || paramsArray[i] instanceof MultipartFile) {
                    //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                    //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = paramsArray[i];
            }
            //update-begin-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
            PropertyFilter profilter = new PropertyFilter() {
                @Override
                public boolean apply(Object o, String name, Object value) {
                    if (value != null && value.toString().length() > 1000) {
                        return false;
                    }
                    return true;
                }
            };
            params = JSONObject.toJSONString(arguments, profilter);
            //update-end-author:taoyan date:20200724 for:日志数据太长的直接过滤掉
        } else {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            // 请求的方法参数值
            Object[] args = joinPoint.getArgs();
            // 请求的方法参数名称
            LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
            String[] paramNames = u.getParameterNames(method);
            if (args != null && paramNames != null) {
                for (int i = 0; i < args.length; i++) {
                    params += "  " + paramNames[i] + ": " + args[i];
                }
            }
        }
        return params;
    }

}
