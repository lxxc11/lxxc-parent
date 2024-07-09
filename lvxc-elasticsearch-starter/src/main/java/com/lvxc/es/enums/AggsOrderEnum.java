package com.lvxc.es.enums;
/**
 * @Description: 聚合排序枚举类
 * @author hel
 * @date
 */
public enum AggsOrderEnum {
    KEY_ASC("key", true),
    KEY_DESC("key",false),
    COUNT_ASC("count", true),
    COUNT_DESC("count", false);



    private String order;
    private Boolean sort;

    AggsOrderEnum(String order, Boolean sort){
        this.order = order;
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public Boolean getSort() {
        return sort;
    }

}

