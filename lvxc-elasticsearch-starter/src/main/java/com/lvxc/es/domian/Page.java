package com.lvxc.es.domian;

import java.util.ArrayList;
import java.util.List;

public class Page<T> implements IPage<T>{
    private static final long serialVersionUID = 7114555180704103831L;
    private Long total;
    private List<T> list;

    public Page() {
        this.total = 0L;
        this.list = new ArrayList<>();
    }

    public Long getTotal() {
        return this.total;
    }
    public List<T> getList() {
        return this.list;
    }

    public Page<T> setTotal(Long total) {
        this.total = total;
        return this;
    }
    public Page<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public Page(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}
