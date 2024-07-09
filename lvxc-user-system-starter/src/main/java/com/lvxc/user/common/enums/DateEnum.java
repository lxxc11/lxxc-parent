package com.lvxc.user.common.enums;

public enum DateEnum {

    DAY(1, "day"),
    WEEK(2, "WEEK"),
    MONTH(3, "month"),
    YEAR(4, "year");

    /**
     * code
     */
    private final Integer code;
    /**
     * 类型
     */
    private final String dec;

    DateEnum(Integer code, String dec) {
        this.code = code;
        this.dec = dec;
    }

    public Integer getCode() {
        return code;
    }

    public String getDec() {
        return dec;
    }

}
