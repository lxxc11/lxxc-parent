package com.lvxc.common.annotation;


import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lvxc.common.serialize.DesensitizationSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationSerialize.class)
public @interface Desensitization {
    /**
     * 脱敏数据类型，只要在CUSTOMER的时候，startInclude和endExclude生效
     */
    DesensitizedUtil.DesensitizedType type() default DesensitizedUtil.DesensitizedType.ADDRESS;

    /**
     * 开始位置（包含）
     */
    int startInclude() default 0;

    /**
     * 结束位置（不包含）
     */
    int endExclude() default 0;
}
