package com.lvxc.es.enums;
/**
 * @Description: 聚合函数枚举类
 * @author hel
 * @date
 */
public enum AggsFunEnum {
    FUN_SUM("sum"),
    FUN_AVG("avg"),
    FUN_MAX("max"),
    FUN_MIN("min"),
    FUN_COUNT("count");

    private String name;

    AggsFunEnum(String name) {
        this.name = name;
    }

    public static AggsFunEnum getValue(String name){
        AggsFunEnum[] values = AggsFunEnum.values();
        for (AggsFunEnum value : values) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
