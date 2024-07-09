package com.lvxc.doc.domain;

import cn.hutool.core.util.ClassUtil;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 模块详情，包含控制层、业务层、持久层、实体类等信息
 */
public class SystemModuleDoc implements SystemModuleBaseDoc {

    private List<Class> controllers;

    private List<Class> services;

    private List<Class> mappers;

    private List<Class> entities;

    private String name;

    public SystemModuleDoc() {
    }

    public SystemModuleDoc addControllers(Class... list){
        if (controllers == null){
            controllers = new ArrayList<>();
        }
        if (list != null){
            controllers.addAll(Arrays.asList(list));
        }
        controllers = distinct(controllers);
        return this;
    }

    @Override
    public SystemModuleDoc addControllersByPackage(String... packagePath) {
        if (controllers == null){
            controllers = new ArrayList<>();
        }
        scanPackage(packagePath,controllers);
        controllers = distinct(controllers);
        return this;
    }


    @Override
    public SystemModuleDoc autoAddServices() {
        if (CollectionUtils.isEmpty(controllers)){
            return this;
        }
        if (CollectionUtils.isEmpty(services)){
            services = new ArrayList<>();
        }
        for (Class controller : controllers) {
            Field[] declaredFields = controller.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Class<?> type = declaredField.getType();
                boolean service = type.getSimpleName().toLowerCase().contains("service");
                if (service){
                    services.add(type);
                }
            }
        }
        services = distinct(services);
        return this;
    }

    public SystemModuleDoc addServices(Class... list){
        if (services == null){
            services = new ArrayList<>();
        }
        if (list != null){
            services.addAll(Arrays.asList(list));
        }
        services = distinct(services);
        return this;
    }

    @Override
    public SystemModuleDoc autoAddMappers() {
        //根据已知service获取mapper,service分为两种，实现mybatis Iservice的，和没有实现的
        List<Class> list = new ArrayList<>();
        for (Class service : this.services) {
            Class service1 = service;
            if (service1.isInterface()){
                //获取接口实现类,默认实现类在同一个包路径下
                Set<Class<?>> classes = ClassUtil.scanPackageBySuper(service1.getPackage().getName(), service);
                Iterator<Class<?>> iterator = classes.iterator();
                if (iterator.hasNext()){
                    service1 = iterator.next();
                }else {
                    //没有找到实现类则跳过
                    continue;
                }
            }
            //获取service中的mapper,暂不考虑service中还有service
            Field[] declaredFields = service1.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getName().contains("Mapper")){
                    list.add(declaredField.getType());
                }
            }
            //额外的泛型中的
            Type type = service1.getGenericSuperclass();
            if (!Objects.equals(type.getTypeName(),Object.class.getName())){
                //public class XxxServiceImpl extends ServiceImpl<XxxMapper, Xxx> implements XxxService
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                Class<?> aClass = (Class<?>)actualTypeArgument;
                list.add(aClass);
            }
        }
        if (!CollectionUtils.isEmpty(list)){
            this.addMappers(list.toArray(new Class[0]));
        }
        return this;
    }

    @Override
    public SystemModuleDoc addMappersByPackage(String... packagePath) {
        if (mappers == null){
            mappers = new ArrayList<>();
        }
        scanPackage(packagePath,mappers);
        mappers = distinct(mappers);
        return this;
    }

    @Override
    public SystemModuleDoc autoAddEntities() {
        //只考虑该情况XxxMapper extends BaseMapper<Xxx>
        List<Class> list = new ArrayList<>();
        for (Class mapper : this.mappers) {
            if (mapper.isInterface()) {
                //额外的泛型中的对象
                Type[] type = mapper.getGenericInterfaces();
                if (!Objects.equals(type[0].getTypeName(), Object.class.getName())) {
                    ParameterizedType parameterizedType = (ParameterizedType) type[0];
                    Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                    Class<?> aClass = (Class<?>) actualTypeArgument;
                    list.add(aClass);
                }
            }
        }
        if (!CollectionUtils.isEmpty(list)){
            this.addEntities(list.toArray(new Class[0]));
        }
        return this;
    }

    @Override
    public SystemModuleDoc addEntitiesByPackage(String... packagePath) {
        if (entities == null){
            entities = new ArrayList<>();
        }
        scanPackage(packagePath,entities);
        entities = distinct(entities);
        return this;
    }

    public SystemModuleDoc addMappers(Class... list){
        if (mappers == null){
            mappers = new ArrayList<>();
        }
        if (list != null){
            mappers.addAll(Arrays.asList(list));
        }
        mappers = distinct(mappers);
        return this;
    }

    public SystemModuleDoc addEntities(Class... list){
        if (entities == null){
            entities = new ArrayList<>();
        }
        if (list != null){
            entities.addAll(Arrays.asList(list));
        }
        entities = distinct(entities);
        return this;
    }

    public SystemModuleDoc setName(String name){
        this.name = name;
        return this;
    }

    public List<Class> getControllers() {
        return controllers;
    }

    public List<Class> getServices() {
        return services;
    }

    public List<Class> getMappers() {
        return mappers;
    }

    public List<Class> getEntities() {
        return entities;
    }

    public String getName() {
        return name;
    }

    private List<Class> distinct(List<Class> classes){
        if (CollectionUtils.isEmpty(classes)){
            return classes;
        }
        Set<String> set  = new HashSet();
        List<Class> newList = new ArrayList<>();
        for (Class aClass : classes) {
            if (set.contains(aClass.getName())){
                continue;
            }
            set.add(aClass.getName());
            newList.add(aClass);
        }
        set.clear();
        classes.clear();
        return newList;
    }
    private void scanPackage(String[] packagePath,List<Class> targetList) {
        if (packagePath != null && packagePath.length > 0){
            for (String s : packagePath) {
                Set<Class<?>> classes = ClassUtil.scanPackage(s);
                if (classes!=null){
                    targetList.addAll(classes);
                }
            }
        }
    }
}
