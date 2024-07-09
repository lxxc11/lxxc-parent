package com.lvxc.doc.config.parser;


import java.util.*;

public class ServiceParser extends BaseParser {
    @Override
    public ServiceParser doParser(Collection<Class> list) {
        Map<String, Object> result = super.resultMap;
        List<Map<String, Object>> itemList = new ArrayList<>();
        for (Class aClass : list) {
            itemList.add(itemParser(aClass));
        }
        result.put("service", itemList);
        return this;
    }

    @Override
    public Map<String, Object> itemParser(Class clazz){
        Map<String, Object> itemMap = new HashMap<>();
        //获取包信息
        itemMap.put("package", clazz.getPackage().getName());
        itemMap.put("name", clazz.getSimpleName());
        //获取controller信息
        itemMap.put("nameCh", clazz.getSimpleName());
        //获取成员变量
        itemMap.put("methods", methodInfos(clazz));
        itemMap.put("fields", fieldInfos(clazz));
        return itemMap;
    }
}
