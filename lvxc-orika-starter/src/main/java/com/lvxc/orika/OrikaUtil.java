package com.lvxc.orika;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.MappingStrategy;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class OrikaUtil {


    public static <S, D> D map(S sourceObject, Class<D> destinationClass) {
        return OrikaConfig.m1.map(sourceObject, destinationClass);
    }

    public static <S, D> D map(S sourceObject, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m1.map(sourceObject, destinationClass, context);
    }

    public static <S, D> void map(S sourceObject, D destinationObject) {
        OrikaConfig.m1.map(sourceObject, destinationObject);
    }

    public static <S, D> void map(S sourceObject, D destinationObject, MappingContext context) {
        OrikaConfig.m1.map(sourceObject, destinationObject, context);
    }

    public static <S, D> void map(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType) {
        OrikaConfig.m1.map(sourceObject, destinationObject, sourceType, destinationType);
    }

    public static <S, D> void map(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        OrikaConfig.m1.map(sourceObject, destinationObject, sourceType, destinationType, context);
    }

    public static <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass) {
        return OrikaConfig.m1.mapAsSet(source, destinationClass);
    }

    public static <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m1.mapAsSet(source, destinationClass, context);
    }

    public static <S, D> Set<D> mapAsSet(S[] source, Class<D> destinationClass) {
        return OrikaConfig.m1.mapAsSet(source, destinationClass);
    }

    public static <S, D> Set<D> mapAsSet(S[] source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m1.mapAsSet(source, destinationClass, context);
    }

    public static <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass) {
        return OrikaConfig.m1.mapAsList(source, destinationClass);
    }

    public static <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m1.mapAsList(source, destinationClass, context);
    }

    public static <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass) {
        return OrikaConfig.m1.mapAsList(source, destinationClass);
    }

    public static <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m1.mapAsList(source, destinationClass, context);
    }

    public static <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass) {
        return OrikaConfig.m1.mapAsArray(destination, source, destinationClass);
    }

    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Class<D> destinationClass) {
        return OrikaConfig.m1.mapAsArray(destination, source, destinationClass);
    }

    public static <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m1.mapAsArray(destination, source, destinationClass, context);
    }

    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m1.mapAsArray(destination, source, destinationClass, context);
    }

    public static <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass) {
        OrikaConfig.m1.mapAsCollection(source, destination, destinationClass);
    }

    public static <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        OrikaConfig.m1.mapAsCollection(source, destination, destinationClass, context);
    }

    public static <S, D> void mapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass) {
        OrikaConfig.m1.mapAsCollection(source, destination, destinationClass);
    }

    public static <S, D> void mapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        OrikaConfig.m1.mapAsCollection(source, destination, destinationClass, context);
    }

    public static <S, D> D map(S sourceObject, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.map(sourceObject, sourceType, destinationType);
    }

    public static <S, D> D map(S sourceObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.map(sourceObject, sourceType, destinationType, context);
    }

    public static <S, D> Set<D> mapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsSet(source, sourceType, destinationType);
    }

    public static <S, D> Set<D> mapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsSet(source, sourceType, destinationType, context);
    }

    public static <S, D> Set<D> mapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsSet(source, sourceType, destinationType);
    }

    public static <S, D> Set<D> mapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsSet(source, sourceType, destinationType, context);
    }

    public static <S, D> List<D> mapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsList(source, sourceType, destinationType);
    }

    public static <S, D> List<D> mapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsList(source, sourceType, destinationType, context);
    }

    public static <S, D> List<D> mapAsList(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsList(source, sourceType, destinationType);
    }

    public static <S, D> List<D> mapAsList(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsList(source, sourceType, destinationType, context);
    }

    public static <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public static <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        OrikaConfig.m1.mapAsCollection(source, destination, sourceType, destinationType);
    }

    public static <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        OrikaConfig.m1.mapAsCollection(source, destination, sourceType, destinationType, context);
    }

    public static <S, D> void mapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        OrikaConfig.m1.mapAsCollection(source, destination, sourceType, destinationType);
    }

    public static <S, D> void mapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        OrikaConfig.m1.mapAsCollection(source, destination, sourceType, destinationType, context);
    }

    public static <S, D> D convert(S source, Class<D> destinationClass, String converterId, MappingContext mappingContext) {
        return OrikaConfig.m1.convert(source, destinationClass, converterId, mappingContext);
    }

    public static <S, D> D convert(S source, Type<S> sourceType, Type<D> destinationType, String converterId, MappingContext mappingContext) {
        return OrikaConfig.m1.convert(source, sourceType, destinationType, converterId, mappingContext);
    }

    public static <Sk, Sv, Dk, Dv> Map<Dk, Dv> mapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return OrikaConfig.m1.mapAsMap(source, sourceType, destinationType);
    }

    public static <Sk, Sv, Dk, Dv> Map<Dk, Dv> mapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsMap(source, sourceType, destinationType, context);
    }

    public static <S, Dk, Dv> Map<Dk, Dv> mapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return OrikaConfig.m1.mapAsMap(source, sourceType, destinationType);
    }

    public static <S, Dk, Dv> Map<Dk, Dv> mapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsMap(source, sourceType, destinationType, context);
    }

    public static <S, Dk, Dv> Map<Dk, Dv> mapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return OrikaConfig.m1.mapAsMap(source, sourceType, destinationType);
    }

    public static <S, Dk, Dv> Map<Dk, Dv> mapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsMap(source, sourceType, destinationType, context);
    }

    public static <Sk, Sv, D> List<D> mapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsList(source, sourceType, destinationType);
    }

    public static <Sk, Sv, D> List<D> mapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsList(source, sourceType, destinationType, context);
    }

    public static <Sk, Sv, D> Set<D> mapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsSet(source, sourceType, destinationType);
    }

    public static <Sk, Sv, D> Set<D> mapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsSet(source, sourceType, destinationType, context);
    }

    public static <Sk, Sv, D> D[] mapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return OrikaConfig.m1.mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <Sk, Sv, D> D[] mapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m1.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public static <S, D> D newObject(S source, Type<? extends D> destinationType, MappingContext context) {
        return OrikaConfig.m1.newObject(source, destinationType, context);
    }

    public static <S, D> MappingStrategy resolveMappingStrategy(S sourceObject, java.lang.reflect.Type sourceType, java.lang.reflect.Type destinationType, boolean mapInPlace, MappingContext context) {
        return OrikaConfig.m1.resolveMappingStrategy(sourceObject, sourceType, destinationType, mapInPlace, context);
    }

    public static void factoryModified(MapperFactory factory) {
        OrikaConfig.m1.factoryModified(factory);
    }


    public static <S, D> D nullMap(S sourceObject, Class<D> destinationClass) {
        return OrikaConfig.m2.map(sourceObject, destinationClass);
    }

    public static <S, D> D nullMap(S sourceObject, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m2.map(sourceObject, destinationClass, context);
    }

    public static <S, D> void nullMap(S sourceObject, D destinationObject) {
        OrikaConfig.m2.map(sourceObject, destinationObject);
    }

    public static <S, D> void nullMap(S sourceObject, D destinationObject, MappingContext context) {
        OrikaConfig.m2.map(sourceObject, destinationObject, context);
    }

    public static <S, D> void nullMap(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType) {
        OrikaConfig.m2.map(sourceObject, destinationObject, sourceType, destinationType);
    }

    public static <S, D> void nullMap(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        OrikaConfig.m2.map(sourceObject, destinationObject, sourceType, destinationType, context);
    }

    public static <S, D> Set<D> nullMapAsSet(Iterable<S> source, Class<D> destinationClass) {
        return OrikaConfig.m2.mapAsSet(source, destinationClass);
    }

    public static <S, D> Set<D> nullMapAsSet(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m2.mapAsSet(source, destinationClass, context);
    }

    public static <S, D> Set<D> nullMapAsSet(S[] source, Class<D> destinationClass) {
        return OrikaConfig.m2.mapAsSet(source, destinationClass);
    }

    public static <S, D> Set<D> nullMapAsSet(S[] source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m2.mapAsSet(source, destinationClass, context);
    }

    public static <S, D> List<D> nullMapAsList(Iterable<S> source, Class<D> destinationClass) {
        return OrikaConfig.m2.mapAsList(source, destinationClass);
    }

    public static <S, D> List<D> nullMapAsList(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m2.mapAsList(source, destinationClass, context);
    }

    public static <S, D> List<D> nullMapAsList(S[] source, Class<D> destinationClass) {
        return OrikaConfig.m2.mapAsList(source, destinationClass);
    }

    public static <S, D> List<D> nullMapAsList(S[] source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m2.mapAsList(source, destinationClass, context);
    }

    public static <S, D> D[] nullMapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass) {
        return OrikaConfig.m2.mapAsArray(destination, source, destinationClass);
    }

    public static <S, D> D[] nullMapAsArray(D[] destination, S[] source, Class<D> destinationClass) {
        return OrikaConfig.m2.mapAsArray(destination, source, destinationClass);
    }

    public static <S, D> D[] nullMapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m2.mapAsArray(destination, source, destinationClass, context);
    }

    public static <S, D> D[] nullMapAsArray(D[] destination, S[] source, Class<D> destinationClass, MappingContext context) {
        return OrikaConfig.m2.mapAsArray(destination, source, destinationClass, context);
    }

    public static <S, D> void nullMapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass) {
        OrikaConfig.m2.mapAsCollection(source, destination, destinationClass);
    }

    public static <S, D> void nullMapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        OrikaConfig.m2.mapAsCollection(source, destination, destinationClass, context);
    }

    public static <S, D> void nullMapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass) {
        OrikaConfig.m2.mapAsCollection(source, destination, destinationClass);
    }

    public static <S, D> void nullMapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        OrikaConfig.m2.mapAsCollection(source, destination, destinationClass, context);
    }

    public static <S, D> D nullMap(S sourceObject, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.map(sourceObject, sourceType, destinationType);
    }

    public static <S, D> D nullMap(S sourceObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.map(sourceObject, sourceType, destinationType, context);
    }

    public static <S, D> Set<D> nullMapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsSet(source, sourceType, destinationType);
    }

    public static <S, D> Set<D> nullMapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsSet(source, sourceType, destinationType, context);
    }

    public static <S, D> Set<D> nullMapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsSet(source, sourceType, destinationType);
    }

    public static <S, D> Set<D> nullMapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsSet(source, sourceType, destinationType, context);
    }

    public static <S, D> List<D> nullMapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsList(source, sourceType, destinationType);
    }

    public static <S, D> List<D> nullMapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsList(source, sourceType, destinationType, context);
    }

    public static <S, D> List<D> nullMapAsList(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsList(source, sourceType, destinationType);
    }

    public static <S, D> List<D> nullMapAsList(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsList(source, sourceType, destinationType, context);
    }

    public static <S, D> D[] nullMapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <S, D> D[] nullMapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <S, D> D[] nullMapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public static <S, D> D[] nullMapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public static <S, D> void nullMapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        OrikaConfig.m2.mapAsCollection(source, destination, sourceType, destinationType);
    }

    public static <S, D> void nullMapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        OrikaConfig.m2.mapAsCollection(source, destination, sourceType, destinationType, context);
    }

    public static <S, D> void nullMapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        OrikaConfig.m2.mapAsCollection(source, destination, sourceType, destinationType);
    }

    public static <S, D> void nullMapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        OrikaConfig.m2.mapAsCollection(source, destination, sourceType, destinationType, context);
    }

    public static <S, D> D nullConvert(S source, Class<D> destinationClass, String converterId, MappingContext mappingContext) {
        return OrikaConfig.m2.convert(source, destinationClass, converterId, mappingContext);
    }

    public static <S, D> D nullConvert(S source, Type<S> sourceType, Type<D> destinationType, String converterId, MappingContext mappingContext) {
        return OrikaConfig.m2.convert(source, sourceType, destinationType, converterId, mappingContext);
    }

    public static <Sk, Sv, Dk, Dv> Map<Dk, Dv> nullMapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return OrikaConfig.m2.mapAsMap(source, sourceType, destinationType);
    }

    public static <Sk, Sv, Dk, Dv> Map<Dk, Dv> nullMapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsMap(source, sourceType, destinationType, context);
    }

    public static <S, Dk, Dv> Map<Dk, Dv> nullMapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return OrikaConfig.m2.mapAsMap(source, sourceType, destinationType);
    }

    public static <S, Dk, Dv> Map<Dk, Dv> nullMapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsMap(source, sourceType, destinationType, context);
    }

    public static <S, Dk, Dv> Map<Dk, Dv> nullMapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return OrikaConfig.m2.mapAsMap(source, sourceType, destinationType);
    }

    public static <S, Dk, Dv> Map<Dk, Dv> nullMapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsMap(source, sourceType, destinationType, context);
    }

    public static <Sk, Sv, D> List<D> nullMapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsList(source, sourceType, destinationType);
    }

    public static <Sk, Sv, D> List<D> nullMapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsList(source, sourceType, destinationType, context);
    }

    public static <Sk, Sv, D> Set<D> nullMapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsSet(source, sourceType, destinationType);
    }

    public static <Sk, Sv, D> Set<D> nullMapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsSet(source, sourceType, destinationType, context);
    }

    public static <Sk, Sv, D> D[] nullMapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return OrikaConfig.m2.mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <Sk, Sv, D> D[] nullMapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return OrikaConfig.m2.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public static <S, D> D nullNewObject(S source, Type<? extends D> destinationType, MappingContext context) {
        return OrikaConfig.m2.newObject(source, destinationType, context);
    }

    public static <S, D> MappingStrategy nullResolveMappingStrategy(S sourceObject, java.lang.reflect.Type sourceType, java.lang.reflect.Type destinationType, boolean mapInPlace, MappingContext context) {
        return OrikaConfig.m2.resolveMappingStrategy(sourceObject, sourceType, destinationType, mapInPlace, context);
    }

    public static void nullFactoryModified(MapperFactory factory) {
        OrikaConfig.m2.factoryModified(factory);
    }


    public static class OrikaConfig {

        private static ObjectMapper objectMapper = new ObjectMapper();

        public static MapperFacade m1 = buildMapper(true).getMapperFacade();
        public static MapperFacade m2 = buildMapper(false).getMapperFacade();


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
                            return source.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        }

                        @Override
                        public LocalDateTime convertFrom(String source, Type<LocalDateTime> destinationType, MappingContext mappingContext) {
                            if (source.contains(":")) {
                                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            } else {
                                return LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE);
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
                log.warn("json 转换失败，数据为非 json 类型");
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


}
