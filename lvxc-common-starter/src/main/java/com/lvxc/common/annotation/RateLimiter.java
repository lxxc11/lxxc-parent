package com.lvxc.common.annotation;

import com.lvxc.common.enums.LimitType;

import java.lang.annotation.*;

/**
 * @author cc
 *
 * SpringBoot + Redis 实现接口限流
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
     /**
     * 限流key
     */
    String key() default "rate_limit:";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;

    /**
     * 提示信息
     */
    String doc() default "访问过于频繁，请稍候再试:";
}
