package com.lvxc.es.converter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.ArrayList;

@WritingConverter
public class JSONToList implements Converter<JSON, ArrayList<?>> {

    @Override
    public ArrayList convert(JSON source) {
        if (ObjectUtil.isNotEmpty(source)) {
            String json = source.toJSONString();
            if (json.startsWith("[")) {
                return new ArrayList((JSONArray) source);
            }
        }
        return null;
    }

}
