package com.lvxc.doc.config.parser;

import java.util.*;

/**
 * Mapper解析类
 */
public class MapperParser extends BaseParser {
    @Override
    public MapperParser doParser(Collection<Class> list) {
        Map<String, Object> result = super.resultMap;
        List<Map<String, Object>> itemList = new ArrayList<>();
        for (Class aClass : list) {
            itemList.add(itemParser(aClass));
        }
        result.put("mapper", itemList);
        return this;
    }

    @Override
    public Map<String, Object> itemParser(Class itemClass) {
        Map<String, Object> itemMap = new HashMap<>();
        //获取包信息
        itemMap.put("package", itemClass.getPackage().getName());
        itemMap.put("name", itemClass.getSimpleName());
        //获取成员变量
        itemMap.put("method", methodInfos(itemClass));
        return itemMap;
    }
}
