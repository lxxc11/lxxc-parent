package com.lvxc.es.util;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.MappingStrategy;
import ma.glasnost.orika.metadata.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class OrikaUtil {

    @Qualifier("notCopyNullMapperFacade")
    @Autowired
    private MapperFacade m1;

    @Qualifier("copyNullMapperFacade")
    @Autowired
    private MapperFacade m2;

    public <S, D> D map(S sourceObject, Class<D> destinationClass) {
        return m1.map(sourceObject, destinationClass);
    }

    public <S, D> D map(S sourceObject, Class<D> destinationClass, MappingContext context) {
        return m1.map(sourceObject, destinationClass, context);
    }

    public <S, D> void map(S sourceObject, D destinationObject) {
        m1.map(sourceObject, destinationObject);
    }

    public <S, D> void map(S sourceObject, D destinationObject, MappingContext context) {
        m1.map(sourceObject, destinationObject, context);
    }

    public <S, D> void map(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType) {
        m1.map(sourceObject, destinationObject, sourceType, destinationType);
    }

    public <S, D> void map(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        m1.map(sourceObject, destinationObject, sourceType, destinationType, context);
    }

    public <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass) {
        return m1.mapAsSet(source, destinationClass);
    }

    public <S, D> Set<D> mapAsSet(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return m1.mapAsSet(source, destinationClass, context);
    }

    public <S, D> Set<D> mapAsSet(S[] source, Class<D> destinationClass) {
        return m1.mapAsSet(source, destinationClass);
    }

    public <S, D> Set<D> mapAsSet(S[] source, Class<D> destinationClass, MappingContext context) {
        return m1.mapAsSet(source, destinationClass, context);
    }

    public <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass) {
        return m1.mapAsList(source, destinationClass);
    }

    public <S, D> List<D> mapAsList(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return m1.mapAsList(source, destinationClass, context);
    }

    public <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass) {
        return m1.mapAsList(source, destinationClass);
    }

    public <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass, MappingContext context) {
        return m1.mapAsList(source, destinationClass, context);
    }

    public <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass) {
        return m1.mapAsArray(destination, source, destinationClass);
    }

    public <S, D> D[] mapAsArray(D[] destination, S[] source, Class<D> destinationClass) {
        return m1.mapAsArray(destination, source, destinationClass);
    }

    public <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return m1.mapAsArray(destination, source, destinationClass, context);
    }

    public <S, D> D[] mapAsArray(D[] destination, S[] source, Class<D> destinationClass, MappingContext context) {
        return m1.mapAsArray(destination, source, destinationClass, context);
    }

    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass) {
        m1.mapAsCollection(source, destination, destinationClass);
    }

    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        m1.mapAsCollection(source, destination, destinationClass, context);
    }

    public <S, D> void mapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass) {
        m1.mapAsCollection(source, destination, destinationClass);
    }

    public <S, D> void mapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        m1.mapAsCollection(source, destination, destinationClass, context);
    }

    public <S, D> D map(S sourceObject, Type<S> sourceType, Type<D> destinationType) {
        return m1.map(sourceObject, sourceType, destinationType);
    }

    public <S, D> D map(S sourceObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.map(sourceObject, sourceType, destinationType, context);
    }

    public <S, D> Set<D> mapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return m1.mapAsSet(source, sourceType, destinationType);
    }

    public <S, D> Set<D> mapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsSet(source, sourceType, destinationType, context);
    }

    public <S, D> Set<D> mapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return m1.mapAsSet(source, sourceType, destinationType);
    }

    public <S, D> Set<D> mapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsSet(source, sourceType, destinationType, context);
    }

    public <S, D> List<D> mapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return m1.mapAsList(source, sourceType, destinationType);
    }

    public <S, D> List<D> mapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsList(source, sourceType, destinationType, context);
    }

    public <S, D> List<D> mapAsList(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return m1.mapAsList(source, sourceType, destinationType);
    }

    public <S, D> List<D> mapAsList(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsList(source, sourceType, destinationType, context);
    }

    public <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return m1.mapAsArray(destination, source, sourceType, destinationType);
    }

    public <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return m1.mapAsArray(destination, source, sourceType, destinationType);
    }

    public <S, D> D[] mapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        m1.mapAsCollection(source, destination, sourceType, destinationType);
    }

    public <S, D> void mapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        m1.mapAsCollection(source, destination, sourceType, destinationType, context);
    }

    public <S, D> void mapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        m1.mapAsCollection(source, destination, sourceType, destinationType);
    }

    public <S, D> void mapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        m1.mapAsCollection(source, destination, sourceType, destinationType, context);
    }

    public <S, D> D convert(S source, Class<D> destinationClass, String converterId, MappingContext mappingContext) {
        return m1.convert(source, destinationClass, converterId, mappingContext);
    }

    public <S, D> D convert(S source, Type<S> sourceType, Type<D> destinationType, String converterId, MappingContext mappingContext) {
        return m1.convert(source, sourceType, destinationType, converterId, mappingContext);
    }

    public <Sk, Sv, Dk, Dv> Map<Dk, Dv> mapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return m1.mapAsMap(source, sourceType, destinationType);
    }

    public <Sk, Sv, Dk, Dv> Map<Dk, Dv> mapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return m1.mapAsMap(source, sourceType, destinationType, context);
    }

    public <S, Dk, Dv> Map<Dk, Dv> mapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return m1.mapAsMap(source, sourceType, destinationType);
    }

    public <S, Dk, Dv> Map<Dk, Dv> mapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return m1.mapAsMap(source, sourceType, destinationType, context);
    }

    public <S, Dk, Dv> Map<Dk, Dv> mapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return m1.mapAsMap(source, sourceType, destinationType);
    }

    public <S, Dk, Dv> Map<Dk, Dv> mapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return m1.mapAsMap(source, sourceType, destinationType, context);
    }

    public <Sk, Sv, D> List<D> mapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return m1.mapAsList(source, sourceType, destinationType);
    }

    public <Sk, Sv, D> List<D> mapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsList(source, sourceType, destinationType, context);
    }

    public <Sk, Sv, D> Set<D> mapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return m1.mapAsSet(source, sourceType, destinationType);
    }

    public <Sk, Sv, D> Set<D> mapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsSet(source, sourceType, destinationType, context);
    }

    public <Sk, Sv, D> D[] mapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return m1.mapAsArray(destination, source, sourceType, destinationType);
    }

    public <Sk, Sv, D> D[] mapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return m1.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public <S, D> D newObject(S source, Type<? extends D> destinationType, MappingContext context) {
        return m1.newObject(source, destinationType, context);
    }

    public <S, D> MappingStrategy resolveMappingStrategy(S sourceObject, java.lang.reflect.Type sourceType, java.lang.reflect.Type destinationType, boolean mapInPlace, MappingContext context) {
        return m1.resolveMappingStrategy(sourceObject, sourceType, destinationType, mapInPlace, context);
    }

    public void factoryModified(MapperFactory factory) {
        m1.factoryModified(factory);
    }


    public <S, D> D nullMap(S sourceObject, Class<D> destinationClass) {
        return m2.map(sourceObject, destinationClass);
    }

    public <S, D> D nullMap(S sourceObject, Class<D> destinationClass, MappingContext context) {
        return m2.map(sourceObject, destinationClass, context);
    }

    public <S, D> void nullMap(S sourceObject, D destinationObject) {
        m2.map(sourceObject, destinationObject);
    }

    public <S, D> void nullMap(S sourceObject, D destinationObject, MappingContext context) {
        m2.map(sourceObject, destinationObject, context);
    }

    public <S, D> void nullMap(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType) {
        m2.map(sourceObject, destinationObject, sourceType, destinationType);
    }

    public <S, D> void nullMap(S sourceObject, D destinationObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        m2.map(sourceObject, destinationObject, sourceType, destinationType, context);
    }

    public <S, D> Set<D> nullMapAsSet(Iterable<S> source, Class<D> destinationClass) {
        return m2.mapAsSet(source, destinationClass);
    }

    public <S, D> Set<D> nullMapAsSet(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return m2.mapAsSet(source, destinationClass, context);
    }

    public <S, D> Set<D> nullMapAsSet(S[] source, Class<D> destinationClass) {
        return m2.mapAsSet(source, destinationClass);
    }

    public <S, D> Set<D> nullMapAsSet(S[] source, Class<D> destinationClass, MappingContext context) {
        return m2.mapAsSet(source, destinationClass, context);
    }

    public <S, D> List<D> nullMapAsList(Iterable<S> source, Class<D> destinationClass) {
        return m2.mapAsList(source, destinationClass);
    }

    public <S, D> List<D> nullMapAsList(Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return m2.mapAsList(source, destinationClass, context);
    }

    public <S, D> List<D> nullMapAsList(S[] source, Class<D> destinationClass) {
        return m2.mapAsList(source, destinationClass);
    }

    public <S, D> List<D> nullMapAsList(S[] source, Class<D> destinationClass, MappingContext context) {
        return m2.mapAsList(source, destinationClass, context);
    }

    public <S, D> D[] nullMapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass) {
        return m2.mapAsArray(destination, source, destinationClass);
    }

    public <S, D> D[] nullMapAsArray(D[] destination, S[] source, Class<D> destinationClass) {
        return m2.mapAsArray(destination, source, destinationClass);
    }

    public <S, D> D[] nullMapAsArray(D[] destination, Iterable<S> source, Class<D> destinationClass, MappingContext context) {
        return m2.mapAsArray(destination, source, destinationClass, context);
    }

    public <S, D> D[] nullMapAsArray(D[] destination, S[] source, Class<D> destinationClass, MappingContext context) {
        return m2.mapAsArray(destination, source, destinationClass, context);
    }

    public <S, D> void nullMapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass) {
        m2.mapAsCollection(source, destination, destinationClass);
    }

    public <S, D> void nullMapAsCollection(Iterable<S> source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        m2.mapAsCollection(source, destination, destinationClass, context);
    }

    public <S, D> void nullMapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass) {
        m2.mapAsCollection(source, destination, destinationClass);
    }

    public <S, D> void nullMapAsCollection(S[] source, Collection<D> destination, Class<D> destinationClass, MappingContext context) {
        m2.mapAsCollection(source, destination, destinationClass, context);
    }

    public <S, D> D nullMap(S sourceObject, Type<S> sourceType, Type<D> destinationType) {
        return m2.map(sourceObject, sourceType, destinationType);
    }

    public <S, D> D nullMap(S sourceObject, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.map(sourceObject, sourceType, destinationType, context);
    }

    public <S, D> Set<D> nullMapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return m2.mapAsSet(source, sourceType, destinationType);
    }

    public <S, D> Set<D> nullMapAsSet(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsSet(source, sourceType, destinationType, context);
    }

    public <S, D> Set<D> nullMapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return m2.mapAsSet(source, sourceType, destinationType);
    }

    public <S, D> Set<D> nullMapAsSet(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsSet(source, sourceType, destinationType, context);
    }

    public <S, D> List<D> nullMapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return m2.mapAsList(source, sourceType, destinationType);
    }

    public <S, D> List<D> nullMapAsList(Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsList(source, sourceType, destinationType, context);
    }

    public <S, D> List<D> nullMapAsList(S[] source, Type<S> sourceType, Type<D> destinationType) {
        return m2.mapAsList(source, sourceType, destinationType);
    }

    public <S, D> List<D> nullMapAsList(S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsList(source, sourceType, destinationType, context);
    }

    public <S, D> D[] nullMapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType) {
        return m2.mapAsArray(destination, source, sourceType, destinationType);
    }

    public <S, D> D[] nullMapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return m2.mapAsArray(destination, source, sourceType, destinationType);
    }

    public <S, D> D[] nullMapAsArray(D[] destination, Iterable<S> source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public <S, D> D[] nullMapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public <S, D> void nullMapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        m2.mapAsCollection(source, destination, sourceType, destinationType);
    }

    public <S, D> void nullMapAsCollection(Iterable<S> source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        m2.mapAsCollection(source, destination, sourceType, destinationType, context);
    }

    public <S, D> void nullMapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType) {
        m2.mapAsCollection(source, destination, sourceType, destinationType);
    }

    public <S, D> void nullMapAsCollection(S[] source, Collection<D> destination, Type<S> sourceType, Type<D> destinationType, MappingContext context) {
        m2.mapAsCollection(source, destination, sourceType, destinationType, context);
    }

    public <S, D> D nullConvert(S source, Class<D> destinationClass, String converterId, MappingContext mappingContext) {
        return m2.convert(source, destinationClass, converterId, mappingContext);
    }

    public <S, D> D nullConvert(S source, Type<S> sourceType, Type<D> destinationType, String converterId, MappingContext mappingContext) {
        return m2.convert(source, sourceType, destinationType, converterId, mappingContext);
    }

    public <Sk, Sv, Dk, Dv> Map<Dk, Dv> nullMapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return m2.mapAsMap(source, sourceType, destinationType);
    }

    public <Sk, Sv, Dk, Dv> Map<Dk, Dv> nullMapAsMap(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return m2.mapAsMap(source, sourceType, destinationType, context);
    }

    public <S, Dk, Dv> Map<Dk, Dv> nullMapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return m2.mapAsMap(source, sourceType, destinationType);
    }

    public <S, Dk, Dv> Map<Dk, Dv> nullMapAsMap(Iterable<S> source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return m2.mapAsMap(source, sourceType, destinationType, context);
    }

    public <S, Dk, Dv> Map<Dk, Dv> nullMapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType) {
        return m2.mapAsMap(source, sourceType, destinationType);
    }

    public <S, Dk, Dv> Map<Dk, Dv> nullMapAsMap(S[] source, Type<S> sourceType, Type<? extends Map<Dk, Dv>> destinationType, MappingContext context) {
        return m2.mapAsMap(source, sourceType, destinationType, context);
    }

    public <Sk, Sv, D> List<D> nullMapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return m2.mapAsList(source, sourceType, destinationType);
    }

    public <Sk, Sv, D> List<D> nullMapAsList(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsList(source, sourceType, destinationType, context);
    }

    public <Sk, Sv, D> Set<D> nullMapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return m2.mapAsSet(source, sourceType, destinationType);
    }

    public <Sk, Sv, D> Set<D> nullMapAsSet(Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsSet(source, sourceType, destinationType, context);
    }

    public <Sk, Sv, D> D[] nullMapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType) {
        return m2.mapAsArray(destination, source, sourceType, destinationType);
    }

    public <Sk, Sv, D> D[] nullMapAsArray(D[] destination, Map<Sk, Sv> source, Type<? extends Map<Sk, Sv>> sourceType, Type<D> destinationType, MappingContext context) {
        return m2.mapAsArray(destination, source, sourceType, destinationType, context);
    }

    public <S, D> D nullNewObject(S source, Type<? extends D> destinationType, MappingContext context) {
        return m2.newObject(source, destinationType, context);
    }

    public <S, D> MappingStrategy nullResolveMappingStrategy(S sourceObject, java.lang.reflect.Type sourceType, java.lang.reflect.Type destinationType, boolean mapInPlace, MappingContext context) {
        return m2.resolveMappingStrategy(sourceObject, sourceType, destinationType, mapInPlace, context);
    }

    public void nullFactoryModified(MapperFactory factory) {
        m2.factoryModified(factory);
    }
}
