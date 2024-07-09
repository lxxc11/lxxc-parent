package com.lvxc.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lvxc.common.serialize.DecimalPrecisionSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * @InterfaceName DecimalPrecision
 * @Description 小数精度处理，支持BigDecimal、Double、Float类型的字段处理小数精度
 * @Author hux
 * @Date 2023/9/20
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(
        using = DecimalPrecisionSerializer.class
)
public @interface DecimalPrecision {

    //保留的小数位，默认2位
    int precision() default 2;

    //BigDecimal类的舍入模式
    RoundingMode roundingMode() default RoundingMode.HALF_UP;

}
