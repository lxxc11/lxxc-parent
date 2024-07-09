package com.lvxc.doc.domain;

import java.util.List;

public interface SystemModuleBaseDoc extends BaseDoc{
    SystemModuleBaseDoc addControllers(Class... list);
    SystemModuleBaseDoc addControllersByPackage(String... packagePath);
    /**
     * 根据现有的controller解析获取servcie
     * @return
     */
    SystemModuleBaseDoc autoAddServices();
    SystemModuleBaseDoc addServices(Class... list);
    SystemModuleBaseDoc autoAddMappers();
    SystemModuleBaseDoc addMappers(Class... list);
    SystemModuleBaseDoc addMappersByPackage(String... packagePath);
    SystemModuleBaseDoc autoAddEntities();
    SystemModuleBaseDoc addEntities(Class... list);
    SystemModuleBaseDoc addEntitiesByPackage(String... packagePath);
    List<Class> getControllers();
    List<Class> getServices();
    List<Class> getMappers();
    List<Class> getEntities();
}
