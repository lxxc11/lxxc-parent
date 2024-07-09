package com.lvxc.doc.config.parser;

import cn.hutool.core.util.ClassUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * doc解析抽象类，controller、service等解析需要继承他
 */
public abstract class BaseParser {
    protected Map<String, Object> resultMap = new HashMap<>();
    abstract BaseParser doParser(Collection<Class> list);
    abstract Map<String, Object> itemParser(Class itemClass);
    protected List<Map<String, Object>> methodInfos(Class itemClass){
        //获取方法
        List<Map<String, Object>> methods = new ArrayList<>();
        Map<String, Object> method = null;
        Method[] declaredMethods = itemClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            method = new HashMap<>();
            method.put("name", declaredMethod.getName());
            //解析controller方法备注用ApiOperation
            ApiOperation declaredAnnotation1 = declaredMethod.getDeclaredAnnotation(ApiOperation.class);
            if (declaredAnnotation1 != null){
                method.put("nameCh", declaredAnnotation1.value());
            }else {
                method.put("nameCh", declaredMethod.getName());
            }
            Class<?> returnType = declaredMethod.getReturnType();
            method.put("returnType", returnType.getName());
            Parameter[] parameters = declaredMethod.getParameters();
            StringJoiner joiner1 = new StringJoiner("\n");
            StringJoiner joiner2 = new StringJoiner("\n");
            for (Parameter parameter : parameters) {
                joiner1.add(parameter.getName());
                joiner2.add(parameter.getType().getSimpleName());
            }
            method.put("paramName", joiner1.toString());
            method.put("paramType",joiner2.toString());
            methods.add(method);
//            method.put("name", )
        }
        return methods;
    }
    protected List<Map<String, Object>> fieldInfos(Class itemClass){
        if (itemClass.isInterface()){
            Set<Class<?>> classes = ClassUtil.scanPackageBySuper(itemClass.getPackage().getName(), itemClass);
            Iterator<Class<?>> iterator = classes.iterator();
            if (iterator.hasNext()){
                return fieldInfos(iterator.next());
            }
            return new ArrayList<>();
        }
        List<Map<String, Object>> fields = new ArrayList<>();
        Map<String, Object> field = null;
        Field[] declaredFields = itemClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            field = new HashMap<>();
            field.put("fieldName", declaredField.getName());
            field.put("fieldType", declaredField.getType().getSimpleName());
            ApiModelProperty apiModelProperty = declaredField.getDeclaredAnnotation(ApiModelProperty.class);
            if (apiModelProperty != null){
                field.put("fieldNameCh", apiModelProperty.value());
            }else {
                field.put("fieldNameCh", declaredField.getName());
            }
            fields.add(field);
        }
        return fields;
    }
}
