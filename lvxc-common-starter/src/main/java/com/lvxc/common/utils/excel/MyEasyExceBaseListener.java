package com.lvxc.common.utils.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyEasyExceBaseListener<T> extends AnalysisEventListener<T> {

    private List<T> list = new ArrayList<>();

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        this.list.add(t);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
