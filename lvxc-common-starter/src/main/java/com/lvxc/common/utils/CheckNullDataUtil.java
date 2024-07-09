package com.lvxc.common.utils;

import cn.hutool.core.util.ObjectUtil;
import com.lvxc.web.common.base.HsServerException;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.util.List;

import static com.lvxc.web.common.base.ResultEnum.EXECUTE_FAIL;

/**
 * @Author caoyq
 * @Date 2023/12/28 14:29
 * @PackageName:com.lvxc.common.utils
 * @ClassName: CheckNullDataUtil
 * @Description: 校验指定字段是否为空
 * @Version 1.0
 */
public class CheckNullDataUtil {
    /**
     * 检查对象的指定字段非空
     *
     * @param obj
     * @param noEmptyFields 必填字段列表
     */
    public static void getCheckNullData(Object obj, List<String> noEmptyFields) {
        if (ObjectUtil.isEmpty(noEmptyFields)) {
            throw new HsServerException(EXECUTE_FAIL.getCode(), "必填字段列表不能为空！");
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 获取属性名称，设置操作权限
            String name = field.getName();
            field.setAccessible(Boolean.TRUE);
            try {
                // 如果字段属于必填字段：根据类型判断其是不是空
                if (noEmptyFields.contains(name)) {
                    // 获取指定对象obj上field对应的值
                    Object o = field.get(obj);
                    if (ObjectUtil.isEmpty(o)) {
                        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                        if (annotation != null) {
                            String value = annotation.value();
                            throw new HsServerException(EXECUTE_FAIL.getCode(), "必填项" + value + "为空！");
                        } else {
                            throw new HsServerException(EXECUTE_FAIL.getCode(), "必填项为空，请检查");
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
