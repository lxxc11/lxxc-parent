package com.lvxc.doc.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 模块容器，moduleDocList数量代表对应系统模块数量
 */
public class SystemDoc<T extends SystemModuleBaseDoc> implements SystemBaseDoc<T> {
    private List<T> moduleDocList;
    private String name;

    public List<T> getModuleDocList() {
        return moduleDocList;
    }

    public String getName() {
        return name;
    }

    public SystemDoc<T> setName(String name) {
        this.name = name;
        return this;
    }

    public SystemDoc<T> addModule(T moduleDoc){
        if (moduleDocList == null){
            moduleDocList = new ArrayList<>();
        }
        moduleDocList.add(moduleDoc);
        return this;
    }

    public SystemDoc<T> addModules(T... list){
        if (moduleDocList == null){
            moduleDocList = new ArrayList<>();
        }
        if (list != null && list.length > 0){
            moduleDocList.addAll(Arrays.asList(list));
        }
        return this;
    }
}
