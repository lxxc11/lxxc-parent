package com.lvxc.es.configuration;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Configuration
public class OrikaConfig {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Bean(value = "copyNullMapperFacade")
    public MapperFacade getCopyNullMapperFacade() {
        return buildMapper(true).getMapperFacade();
    }

    @Bean(value = "notCopyNullMapperFacade")
    public MapperFacade getNotCopyNullMapperFacade() {
        return buildMapper(false).getMapperFacade();
    }


    private static DefaultMapperFactory buildMapper(Boolean copyNullFlag) {
        DefaultMapperFactory mapperFactory = new DefaultMapperFactory.Builder().mapNulls(copyNullFlag).build();
        //        Object 复制
        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<Object, Object>() {
                    @Override
                    public Object convertTo(Object source, Type<Object> destinationType, MappingContext mappingContext) {
                        try {
                            return objectMapper.readValue(objectMapper.writeValueAsString(source), source.getClass());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public Object convertFrom(Object source, Type<Object> destinationType, MappingContext mappingContext) {
                        try {
                            return objectMapper.readValue(objectMapper.writeValueAsString(source), source.getClass());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
        );
        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<JSON, JSONArray>() {

                    @Override
                    public JSONArray convertTo(JSON source, Type<JSONArray> destinationType, MappingContext mappingContext) {
                        return JSONArray.parseArray(source.toJSONString());
                    }

                    @Override
                    public JSON convertFrom(JSONArray source, Type<JSON> destinationType, MappingContext mappingContext) {
                        return JSONArray.parseArray(source.toJSONString());
                    }
                }
        );



        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<JSON, JSONObject>() {

                    @Override
                    public JSONObject convertTo(JSON source, Type<JSONObject> destinationType, MappingContext mappingContext) {
                        return JSONObject.parseObject(source.toJSONString());
                    }

                    @Override
                    public JSON convertFrom(JSONObject source, Type<JSON> destinationType, MappingContext mappingContext) {
                        return JSONObject.parseObject(source.toJSONString());
                    }
                }
        );

        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<JSONObject, JSONObject>() {

                    @Override
                    public JSONObject convertTo(JSONObject source, Type<JSONObject> destinationType, MappingContext mappingContext) {
                        return ObjectUtil.isNotEmpty(source) ? source : null;
                    }

                    @Override
                    public JSONObject convertFrom(JSONObject source, Type<JSONObject> destinationType, MappingContext mappingContext) {
                        return ObjectUtil.isNotEmpty(source) ? source : null;
                    }
                }
        );
        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<JSON, String>() {

                    @Override
                    public String convertTo(JSON source, Type<String> destinationType, MappingContext mappingContext) {
                        return source.toJSONString();
                    }

                    @Override
                    public JSON convertFrom(String json, Type<JSON> destinationType, MappingContext mappingContext) {
                        return stringToJson(json);
                    }
                }
        );

        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<LocalDateTime, String>() {

                    @Override
                    public String convertTo(LocalDateTime source, Type<String> destinationType, MappingContext mappingContext) {
                        return LocalDateTimeUtil.format(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    }

                    @Override
                    public LocalDateTime convertFrom(String source, Type<LocalDateTime> destinationType, MappingContext mappingContext) {
                        if (source.contains(":")) {
                            return LocalDateTimeUtil.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        } else {
                            return LocalDateTimeUtil.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
                        }
                    }
                }
        );

        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<LocalDateTime, LocalDate>() {

                    @Override
                    public LocalDate convertTo(LocalDateTime source, Type<LocalDate> destinationType, MappingContext mappingContext) {
                        return source.toLocalDate();
                    }

                    @Override
                    public LocalDateTime convertFrom(LocalDate source, Type<LocalDateTime> destinationType, MappingContext mappingContext) {
                        return source.atStartOfDay();
                    }
                }
        );

        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<JSONArray, List<?>>() {

                    @Override
                    public List<?> convertTo(JSONArray source, Type<List<?>> destinationType, MappingContext mappingContext) {
                        return source;
                    }

                    @Override
                    public JSONArray convertFrom(List<?> source, Type<JSONArray> destinationType, MappingContext mappingContext) {
                        JSONArray jsonArray = new JSONArray();
                        for (Object o : source) {
                            jsonArray.add(o);
                        }
                        return jsonArray;
                    }
                }
        );


        mapperFactory.getConverterFactory().registerConverter(
                new BidirectionalConverter<JSON, List<?>>() {

                    @Override
                    public List<?> convertTo(JSON source, Type<List<?>> destinationType, MappingContext mappingContext) {
                        if (source instanceof JSONArray) {
                            return (List<?>) source;
                        }
                        return null;
                    }

                    @Override
                    public JSON convertFrom(List<?> source, Type<JSON> destinationType, MappingContext mappingContext) {
                        JSONArray jsonArray = new JSONArray();
                        for (Object o : source) {
                            jsonArray.add(o);
                        }
                        return jsonArray;
                    }
                }
        );




        return mapperFactory;
    }


    private static JSON stringToJson(String json) {
        JSON result = null;
        char c = json.charAt(0);
        if (c == '{') {
            result = JSON.parseObject(json);
        } else if (c == '[') {
            result = JSON.parseArray(json);
        } else {
        }
        return result;
    }


    private static JSONArray stringToJSONArray(String source) {
        String[] arr = source.split("\r\n");
        JSONArray jsonArray = new JSONArray();
        Arrays.stream(arr).forEach(
                e -> jsonArray.add(e)
        );
        return jsonArray;
    }

    private static String jsonToExcel(JSON source) {
        StringBuilder sb = new StringBuilder();
        if (source.getClass().equals(JSONObject.class)) {
            ((JSONObject) source).forEach(
                    (k, v) -> {
                        sb.append(k);
                        sb.append(":");
                        sb.append(v);
                        sb.append("\r\n");
                    }
            );
        }
        if (source.getClass().equals(JSONArray.class)) {
            ((JSONArray) source).forEach(
                    e -> {
                        sb.append(e);
                        sb.append("\r\n");
                    }
            );
        }
        return sb.toString();
    }
}
