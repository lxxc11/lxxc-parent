package com.lvxc.encrypt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数加密注解
 *
 * @author 詹杨锋
 * @since 2023/11/29
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamEncrypt {
    boolean needDecrypt() default true;

    boolean digest() default true;

    int digestTimes() default -1;

    String digestSalt() default "";

    int digestSaltPosition() default -1;
}
