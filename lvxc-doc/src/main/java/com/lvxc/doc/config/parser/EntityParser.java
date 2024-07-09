package com.lvxc.doc.config.parser;

import io.swagger.annotations.ApiModel;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Entity解析类
 */
public class EntityParser extends BaseParser {
    @Override
    public EntityParser doParser(Collection<Class> list) {
        Map<String, Object> result = super.resultMap;
        List<Map<String, Object>> itemList = new ArrayList<>();
        for (Class aClass : list) {
            itemList.add(itemParser(aClass));
        }
        result.put("entity", itemList);
        return this;
    }

    @Override
    public Map<String, Object> itemParser(Class itemClass) {
        Map<String, Object> itemMap = new HashMap<>();
        //获取包信息
        itemMap.put("package", itemClass.getPackage().getName());
        itemMap.put("name", itemClass.getSimpleName());
        //获取成员变量
        itemMap.put("field", fieldInfos(itemClass));
        Annotation declaredAnnotation = itemClass.getDeclaredAnnotation(ApiModel.class);
        if (declaredAnnotation != null){
            itemMap.put("nameCh", ((ApiModel)declaredAnnotation).value());
        }
        return itemMap;
    }
}
