package com.lvxc.host.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 云存储域名替换注解
 * @author 詹杨锋
 * @since 2022/9/10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Host {
    /**
     * 替换Host的值
     */
    String value() default "";
}
