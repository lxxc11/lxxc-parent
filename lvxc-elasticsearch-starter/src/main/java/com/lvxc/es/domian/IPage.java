package com.lvxc.es.domian;

import java.io.Serializable;
import java.util.List;

public interface IPage<T> extends Serializable {
    Long getTotal();
    List<T> getList();
    IPage<T> setTotal(Long total);
    IPage<T> setList(List<T> list);

}
