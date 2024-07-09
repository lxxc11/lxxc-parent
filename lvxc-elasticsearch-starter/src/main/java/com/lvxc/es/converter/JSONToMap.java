package com.lvxc.es.converter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.LinkedHashMap;

@WritingConverter
public class JSONToMap implements Converter<JSON, LinkedHashMap<?, ?>> {
    @Override
    public LinkedHashMap convert(JSON source) {
        if (ObjectUtil.isNotEmpty(source)) {
            String json = source.toJSONString();
            if (json.startsWith("{")) {
                return new LinkedHashMap((JSONObject) source);
            }
        }
        return null;
    }
}
