package com.lvxc.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.lvxc.common.annotation.DecimalPrecision;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @ClassName DecimalPrecisionUtil
 * @Description 小数精度工具类
 * @Author Roger
 * @Date 2023/9/20
 **/
public class DecimalPrecisionUtil {

    /**
     * 格式化对象的Double、BigDecimal、Float类型字段
     *
     * @param object    保留小数位的对象
     * @param precision 小数位精度，必须>=0
     * @Author Roger
     * @Description 格式化对象的Double、BigDecimal、Float类型字段
     * @Date 2023/9/20
     **/
    public static void formatFields(Object object, int precision) {
        if (precision < 0) {
            throw new IllegalArgumentException("precision不能为负数");
        }
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder patternBuilder = new StringBuilder("#.");
        for (int i = 0; i < precision; i++) {
            patternBuilder.append("0");
        }
        String pattern = patternBuilder.toString();
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        try {
            //遍历对象属性保留小数位
            for (Field field : fields) {
                if (field.isAnnotationPresent(DecimalPrecision.class)) {
                    field.setAccessible(true);
                    if (field.getType() == Double.class) {
                        Double value = (Double) field.get(object);
                        if (ObjectUtil.isNotEmpty(value) && !value.isNaN()) {
                            double formattedValue = Double.parseDouble(decimalFormat.format(value));
                            field.set(object, formattedValue);
                        }
                    }
                    if (field.getType() == BigDecimal.class) {
                        BigDecimal value = (BigDecimal) field.get(object);
                        if (ObjectUtil.isNotEmpty(value)) {
                            BigDecimal formattedValue = value.setScale(precision, RoundingMode.HALF_UP);
                            field.set(object, formattedValue);
                        }
                    }
                    if (field.getType() == Float.class) {
                        Float value = (Float) field.get(object);
                        if (ObjectUtil.isNotEmpty(value) && !value.isNaN()) {
                            double formattedValue = Double.parseDouble(decimalFormat.format(value.doubleValue()));
                            field.set(object, formattedValue);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
