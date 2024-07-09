package com.lvxc.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGobject;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 周锐
 * @Date 2023 2023/12/5 15:43
 * @Version 1.0
 * @Description
 */
public class CompareDifferentUtils {

    /**
     * 存储上次修改和本次修改的记录
     *
     * @param o1         老数据
     * @param o2         新数据
     * @param weightJson 存储的上次修改和本次修改的记录
     * @return
     */
    public static PGobject getWeightJson(Object o1, Object o2, PGobject weightJson) {
        //比较简单数据不同
        JSONObject differentJSONObject = jsonCompare(o1, o2);
        //解析JSON
        JSONArray jsonArray;
        if (Objects.isNull(weightJson) || StringUtils.isBlank(weightJson.getValue())) {
            jsonArray = new JSONArray();
            weightJson = new PGobject();
            weightJson.setType("jsonb");
        } else {
            String json = weightJson.getValue();
            jsonArray = JSONArray.parseArray(json);
        }
        //属性map
        Map<String, JSONObject> wjMap = new HashMap<>(16);
        jsonArray.forEach(e -> {
            JSONObject j = (JSONObject) e;
            String field_name = (String) j.get("field_name");
            wjMap.put(field_name, j);
        });
        //比较json
        JSONArray differentFieldJSONArray = (JSONArray) differentJSONObject.get("differentField");
        JSONArray addValueFieldJSONArray = (JSONArray) differentJSONObject.get("addValueField");
        JSONObject oldValueJson = (JSONObject) differentJSONObject.get("oldValue");
        JSONObject newValueJson = (JSONObject) differentJSONObject.get("newValue");
        //写入weightJson
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        differentFieldJSONArray.forEach(
                e -> {
                    String field = (String) e;
                    field = camelTransformUnderline(field);
                    JSONObject j = wjMap.get(field);
                    if (Objects.nonNull(j)) {
                        j.put("source", "client");
                        j.put("field_name", camelTransformUnderline(field));
                        j.put("update_date", simpleDateFormat.format(new Date()));
                        j.put("value_before", null != oldValueJson.get(e) ? oldValueJson.get(e) : null);
                        j.put("value_after", newValueJson.get(e));
                    } else {
                        j = new JSONObject();
                        j.put("source", "client");
                        j.put("field_name", camelTransformUnderline(field));
                        j.put("update_date", simpleDateFormat.format(new Date()));
                        j.put("value_before", null != oldValueJson.get(e) ? oldValueJson.get(e) : null);
                        j.put("value_after", newValueJson.get(e));
                    }
                    wjMap.put(field, j);
                }
        );
        //新增属性
        addValueFieldJSONArray.forEach(
                e -> {
                    String field = (String) e;
                    field = camelTransformUnderline(field);
                    JSONObject j = new JSONObject();
                    j.put("source", "client");
                    j.put("field_name", field);
                    j.put("update_date", simpleDateFormat.format(new Date()));
                    j.put("value_before", "null");
                    j.put("value_after", newValueJson.get(e));
                    wjMap.put(field, j);
                }
        );
        final Collection<JSONObject> values = wjMap.values();
        JSONArray nowJSON = new JSONArray();
        values.forEach(
                e -> {
                    nowJSON.add(e);
                }
        );
        try {
            weightJson.setValue(nowJSON.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weightJson;
    }

    /**
     * 比较简单数据不同
     *
     * @param o1
     * @param o2
     * @return
     */
    public static JSONObject jsonCompare(Object o1, Object o2) {
        JSONObject different = new JSONObject();
        JSONObject oldJ = new JSONObject();
        JSONObject newJ = new JSONObject();
        JSONObject oldJson = JSONObject.parseObject(JSONObject.toJSONString(o1));
        JSONObject newJson = JSONObject.parseObject(JSONObject.toJSONString(o2));
        JSONArray differentField = new JSONArray();
        JSONArray addValueField = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        newJson.keySet().forEach(
                e -> {
                    if (oldJson.size() > 0) {
                        if (!"weightJson".equals(e)) {
                            Object oldValue = oldJson.get(e);
                            if (ObjectUtil.isNotNull(oldValue)) {
                                Object newValue = newJson.get(e);
                                if (!newValue.equals(oldValue)) {
                                    differentField.add(e);
                                    if (oldValue instanceof String) {
                                        if (((String) oldValue).endsWith("AM") || ((String) oldValue).endsWith("PM")) {
                                            try {
                                                oldValue = simpleDateFormat.format(simpleDateFormat.parse((String) oldValue));
                                            } catch (ParseException parseException) {
                                                parseException.printStackTrace();
                                            }
                                        }
                                    }
                                    if (newValue instanceof String) {
                                        if (((String) newValue).endsWith("AM") || ((String) newValue).endsWith("PM")) {
                                            try {
                                                newValue = simpleDateFormat.format(simpleDateFormat.parse((String) newValue));
                                            } catch (ParseException parseException) {
                                                parseException.printStackTrace();
                                            }
                                        }
                                    }
                                    oldJ.put(e, oldValue);
                                    newJ.put(e, newValue);
                                }
                            } else {
                                Object newValue = newJson.get(e);
                                if (newValue instanceof String) {
                                    if (((String) newValue).endsWith("AM") || ((String) newValue).endsWith("PM")) {
                                        try {
                                            newValue = simpleDateFormat.format(simpleDateFormat.parse((String) newValue));
                                        } catch (ParseException parseException) {
                                            parseException.printStackTrace();
                                        }
                                    }
                                }
                                addValueField.add(e);
                                newJ.put(e, newValue);
                            }
                        }
                    }
                }
        );
        different.put("oldValue", oldJ);
        different.put("newValue", newJ);
        different.put("differentField", differentField);
        different.put("addValueField", addValueField);
        return different;
    }

    /**
     * 大写字母转下划线
     *
     * @param param
     * @return
     */
    public static String camelTransformUnderline(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }
        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}