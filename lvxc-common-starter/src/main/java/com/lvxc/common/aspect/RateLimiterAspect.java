package com.lvxc.common.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.ServiceException;
import com.lvxc.common.annotation.RateLimiter;
import com.lvxc.common.enums.LimitType;
import com.lvxc.common.utils.IpUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author cc
 *
 * SpringBoot + Redis 实现接口限流
 */
@Aspect
@Component
public class RateLimiterAspect {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Around("@annotation(rateLimiter)")
    public void around(JoinPoint point, RateLimiter rateLimiter) {
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String doc = rateLimiter.doc();

        String combineKey = getCombineKey(rateLimiter, point);
        try {
            Object result = redisTemplate.opsForValue().get(combineKey);
            Integer number = 0;
            if(ObjectUtil.isEmpty(result)){
                redisTemplate.opsForValue().set(combineKey,number+1,time, TimeUnit.SECONDS);
            }else {
                number = Integer.parseInt(result.toString());
                check(number, count, doc, combineKey);
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), combineKey);
        } catch (Exception e) {
            throw new RuntimeException(doc);
        }
    }

    private void check(Integer number, int count, String doc, String combineKey) {
        if(number >= count){
            throw new ServiceException(doc);
        }else {
            Long expire = redisTemplate.getExpire(combineKey);
            redisTemplate.opsForValue().set(combineKey, number +1,expire,TimeUnit.SECONDS);
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            stringBuffer.append(IpUtil.getIpAddress()).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
