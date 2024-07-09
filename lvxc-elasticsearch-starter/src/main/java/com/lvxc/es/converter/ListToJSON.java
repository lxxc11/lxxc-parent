package com.lvxc.es.converter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.ArrayList;

@ReadingConverter
public class ListToJSON implements Converter<ArrayList<?>, JSON> {
    @Override
    public JSON convert(ArrayList source) {
        if (ObjectUtil.isNotEmpty(source)) {
            return new JSONArray(source);
        }
        return null;
    }
}
