package com.lvxc.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.lvxc.common.dto.AdminDistrictInfo;
import lombok.extern.slf4j.Slf4j;
import org.joor.Reflect;


import java.util.Map;


/**
 * @ClassName ReflectUtil
 * @Description 对象属性对比工具类
 * @Author GaoP
 * @Date 2023/9/25
 **/

@Slf4j
public class ReflectUtil {


    /**
     *  对比两对象相同属性的值是否相同，并输出值不同的属性，及old对象中对应的值
     *  (常用于操作记录，编辑前数据对比)
     * @param now
     * @param old
     * @param <T>
     * @return
     */
    public static <T> JSONObject getOldDiff(T now, T old) {
        JSONObject diff = new JSONObject();
        Map<String, Reflect> fields = Reflect.on(old).fields();
        Reflect nowRef = Reflect.on(now);
        for (Map.Entry<String, Reflect> field : fields.entrySet()) {
            String fieldName = field.getKey();
            Reflect fieldValue = field.getValue();
            // 检查 nowRef 中是否存在 fieldName 字段
            if (nowRef.fields().containsKey(fieldName) &&
                    ObjectUtil.notEqual(nowRef.get(fieldName), fieldValue.get())) {
                diff.put(fieldName, fieldValue.get());
            }
        }
        return diff;
    }

    public static void main(String[] args) {
        // 示例用法
        AdminDistrictInfo nowInfo = new AdminDistrictInfo();
        nowInfo.setCity("杭州市");
        nowInfo.setCountry("中国");
        nowInfo.setProvince("浙江省");

        AdminDistrictInfo oldInfo = new AdminDistrictInfo();
        oldInfo.setCity("临沂市");
        oldInfo.setCountry("中国");
        oldInfo.setProvince("山东省");

        JSONObject info = getOldDiff(nowInfo,oldInfo);
        log.info(JSONUtil.toJsonStr(info));
    }
}
