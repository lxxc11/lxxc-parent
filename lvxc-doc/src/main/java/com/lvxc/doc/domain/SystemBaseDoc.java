package com.lvxc.doc.domain;

import java.util.List;

public interface SystemBaseDoc<T extends SystemModuleBaseDoc> extends BaseDoc{
    SystemBaseDoc<T> addModule(T moduleDoc);
    SystemBaseDoc<T> addModules(T... list);
    List<T> getModuleDocList();
}
