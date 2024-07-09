package com.lvxc.es.converter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.LinkedHashMap;

@ReadingConverter
public class MapToJSON implements Converter<LinkedHashMap<?, ?>, JSON> {
    @Override
    public JSON convert(LinkedHashMap source) {
        if (ObjectUtil.isNotEmpty(source)) {
            return new JSONObject(source);
        }
        return null;
    }
}
