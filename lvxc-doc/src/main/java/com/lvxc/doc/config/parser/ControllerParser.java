package com.lvxc.doc.config.parser;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.util.*;

public class ControllerParser extends BaseParser{

    @Override
    public ControllerParser doParser(Collection<Class> list) {
        Map<String, Object> result = super.resultMap;
        List<Map<String, Object>> itemList = new ArrayList<>();
        for (Class aClass : list) {
            itemList.add(itemParser(aClass));
        }
        result.put("controller", itemList);
        return this;
    }

    @Override
    public Map<String, Object> itemParser(Class clazz){
        Map<String, Object> itemMap = new HashMap<>();
        //获取包信息
        itemMap.put("package", clazz.getPackage().getName());
        Annotation declaredAnnotation = clazz.getDeclaredAnnotation(Api.class);
        Annotation declaredAnnotation1 = clazz.getDeclaredAnnotation(RequestMapping.class);
        itemMap.put("name", clazz.getSimpleName());
        //获取controller信息
        if (declaredAnnotation == null){
            itemMap.put("nameCh", clazz.getSimpleName());
        }else {
            itemMap.put("nameCh", ((Api)declaredAnnotation).tags()[0]);
        }
        //获取controller requestMapping 路径信息
        if (declaredAnnotation1 != null){
            itemMap.put("path", ((RequestMapping)declaredAnnotation1).value()[0]);
        }
        //获取成员变量
        itemMap.put("methods", methodInfos(clazz));
        itemMap.put("fields", fieldInfos(clazz));
        return itemMap;
    }
}
