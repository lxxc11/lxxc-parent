package com.lvxc.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamUtils<T> {

    /**
     * 操作的集合
     */
    private List<T> list;

    /**
     * 构造方法
     *
     * @param list 操作的集合
     */
    private StreamUtils(List<T> list) {
        this.list = list;
    }

    /**
     * 获取StreamUtils
     *
     * @param list 操作的集合
     * @param <T>  泛型
     * @return StreamUtils
     */
    public static <T> StreamUtils<T> from(List<T> list) {
        return new StreamUtils<>(list);
    }

    /**
     * 封装Stream流filter方法
     *
     * @param predicate 条件
     * @return
     */
    public StreamUtils<T> filter(Predicate<T> predicate) {
        list = list.stream().filter(predicate).collect(Collectors.toList());
        return this;
    }

    /**
     * 取出某个属性的值
     *
     * @param mapper
     * @param <R>
     * @return
     */
    public <R> StreamUtils<R> map(Function<T, R> mapper) {
        List<R> result = list.stream().map(mapper).collect(Collectors.toList());
        return new StreamUtils<>(result);
    }

    /**
     * 集合转到map中，以对象其中的一个属性为key
     *
     * @param keyMapper
     * @param valueMapper
     * @return
     */
    public <R> Map<R, R> toMap(Function<T, R> keyMapper, Function<T, R> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * 根据一个字段分组
     *
     * @param keyMapper
     * @return
     */
    public <R> Map<R, List<T>> group(Function<T, R> keyMapper) {
        return list.stream().collect(Collectors.groupingBy(keyMapper));
    }

    /**
     * 对集合去重
     *
     * @return
     */
    public StreamUtils<T> distinct() {
        list = list.stream().distinct().collect(Collectors.toList());
        return this;
    }

    /**
     * 取最大
     *
     * @param comparator
     * @return
     */
    public Optional<T> max(Comparator<T> comparator) {
        return list.stream().max(comparator);
    }

    /**
     * 取最小
     *
     * @param comparator
     * @return
     */
    public Optional<T> min(Comparator<T> comparator) {
        return list.stream().min(comparator);
    }

    /**
     * 等于
     *
     * @param keyExtractor
     * @param value
     * @return
     */
    public StreamUtils<T> eq(Function<T, Object> keyExtractor, Object value) {
        return filter(obj -> Objects.equals(keyExtractor.apply(obj), value));
    }

    /**
     * 等于
     *
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param value
     * @return
     */
    public StreamUtils<T> eq(boolean condition, Function<T, Object> keyExtractor, Object value) {
        return condition ? filter(obj -> Objects.equals(keyExtractor.apply(obj), value)) : this;
    }

    /**
     * 是否为null
     *
     * @return
     */
    public StreamUtils<T> isNull(Function<T, Object> keyExtractor) {
        return filter(obj -> Objects.isNull(keyExtractor.apply(obj)));
    }

    /**
     * 是否为null
     *
     * @param condition 判断是否参与查询的条件
     * @return
     */
    public StreamUtils<T> isNull(boolean condition, Function<T, Object> keyExtractor) {
        return condition ? filter(obj -> Objects.isNull(keyExtractor.apply(obj))) : this;
    }

    /**
     * 是否为空
     *
     * @return
     */
    public StreamUtils<T> isEmpty(Function<T, Object> keyExtractor) {
        return filter(obj -> ObjectUtil.isEmpty(keyExtractor.apply(obj)));
    }

    /**
     * 是否为空
     *
     * @param condition 判断是否参与查询的条件
     * @return
     */
    public StreamUtils<T> isEmpty(boolean condition, Function<T, Object> keyExtractor) {
        return condition ? filter(obj -> ObjectUtil.isEmpty(keyExtractor.apply(obj))) : this;
    }

    /**
     * 是否不为null
     *
     * @return
     */
    public StreamUtils<T> isNotNull(Function<T, Object> keyExtractor) {
        return filter(obj -> Objects.nonNull(keyExtractor.apply(obj)));
    }

    /**
     * 是否不为null
     *
     * @param condition 判断是否参与查询的条件
     * @return
     */
    public StreamUtils<T> isNotNull(boolean condition, Function<T, Object> keyExtractor) {
        return condition ? filter(obj -> Objects.nonNull(keyExtractor.apply(obj))) : this;
    }

    /**
     * 是否不为空
     *
     * @return
     */
    public StreamUtils<T> isNotEmpty(Function<T, Object> keyExtractor) {
        return filter(obj -> ObjectUtil.isNotEmpty(keyExtractor.apply(obj)));
    }

    /**
     * 是否不为空
     *
     * @param condition 判断是否参与查询的条件
     * @return
     */
    public StreamUtils<T> isNotEmpty(boolean condition, Function<T, Object> keyExtractor) {
        return condition ? filter(obj -> ObjectUtil.isNotEmpty(keyExtractor.apply(obj))) : this;
    }

    /**
     * 不等于
     *
     * @param keyExtractor
     * @param value
     * @return
     */
    public StreamUtils<T> ne(Function<T, Object> keyExtractor, Object value) {
        return filter(obj -> !Objects.equals(keyExtractor.apply(obj), value));
    }

    /**
     * 不等于
     *
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param value
     * @return
     */
    public StreamUtils<T> ne(boolean condition, Function<T, Object> keyExtractor, Object value) {
        return condition ? filter(obj -> !Objects.equals(keyExtractor.apply(obj), value)) : this;
    }

    /**
     * 大于等于
     *
     * @param keyExtractor
     * @param threshold
     * @return
     */
    public <U extends Comparable<U>> StreamUtils<T> ge(Function<T, U> keyExtractor, U threshold) {
        return filter(obj -> {
            U value = keyExtractor.apply(obj);
            return null != value && value.compareTo(threshold) >= 0;
        });
    }

    /**
     * 大于等于
     *
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param threshold
     * @return
     */
    public <U extends Comparable<U>> StreamUtils<T> ge(boolean condition, Function<T, U> keyExtractor, U threshold) {
        return condition ? filter(obj -> {
            U value = keyExtractor.apply(obj);
            return null != value && value.compareTo(threshold) >= 0;
        }) : this;
    }

    /**
     * 大于
     *
     * @param keyExtractor
     * @param threshold
     * @return
     */
    public <U extends Comparable<U>> StreamUtils<T> gt(Function<T, U> keyExtractor, U threshold) {
        // return filter(obj -> keyExtractor.apply(obj).compareTo(threshold) > 0);
        return filter(obj -> {
            U value = keyExtractor.apply(obj);
            return value != null && value.compareTo(threshold) > 0;
        });
    }

    /**
     * 大于
     *
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param threshold
     * @return
     */
    public <U extends Comparable<U>> StreamUtils<T> gt(boolean condition, Function<T, U> keyExtractor, U threshold) {
        return condition ? filter(obj -> {
            U value = keyExtractor.apply(obj);
            return null != value && value.compareTo(threshold) > 0;
        }) : this;
    }

    /**
     * 小于等于
     *
     * @param keyExtractor
     * @param threshold
     * @return
     */
    public <U extends Comparable<U>> StreamUtils<T> le(Function<T, U> keyExtractor, U threshold) {
        return filter(obj -> {
            U value = keyExtractor.apply(obj);
            return null != value && value.compareTo(threshold) <= 0;
        });
    }

    /**
     * 小于等于
     *
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param threshold
     * @return
     */
    public <U extends Comparable<U>> StreamUtils<T> le(boolean condition, Function<T, U> keyExtractor, U threshold) {
        return condition ? filter(obj -> {
            U value = keyExtractor.apply(obj);
            return null != value && value.compareTo(threshold) <= 0;
        }) : this;
    }

    /**
     * 小于
     *
     * @param keyExtractor
     * @param threshold
     * @return
     */
    public <U extends Comparable<U>> StreamUtils<T> lt(Function<T, U> keyExtractor, U threshold) {
        // return filter(obj -> keyExtractor.apply(obj).compareTo(threshold) < 0);
        return filter( obj -> {
            U value = keyExtractor.apply(obj);
            return value != null && value.compareTo(threshold) < 0;
        });
    }

    /**
     * 小于
     *
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param threshold
     * @return
     */
    public <U extends Comparable<U>> StreamUtils<T> lt(boolean condition, Function<T, U> keyExtractor, U threshold) {
        return condition ? filter(obj -> {
            U value = keyExtractor.apply(obj);
            return value != null && value.compareTo(threshold) < 0;
        }) : this;
    }

    /**
     * 模糊查询：%like%
     *
     * @param keyExtractor
     * @param pattern
     * @return
     */
    public StreamUtils<T> like(Function<T, String> keyExtractor, String pattern) {
        return filter(obj -> {
            String fieldValue = keyExtractor.apply(obj);
            return fieldValue != null && fieldValue.contains(pattern);
        });
    }

    /**
     * 模糊查询：%like%
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param pattern
     * @return
     */
    public StreamUtils<T> like(boolean condition, Function<T, String> keyExtractor, String pattern) {
        return condition ? filter(obj -> {
            String fieldValue = keyExtractor.apply(obj);
            return fieldValue != null && fieldValue.contains(pattern);
        }) : this;
    }

    /**
     * in：包含
     *
     * @param keyExtractor
     * @param values
     * @return
     */
    public StreamUtils<T> in(Function<T, Object> keyExtractor, Collection<?> values) {
        return filter(obj -> {
            Object fieldValue = keyExtractor.apply(obj);
            return CollUtil.isNotEmpty(values) && values.contains(fieldValue);
        });
    }

    /**
     * in：包含
     *
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param values
     * @return
     */
    public StreamUtils<T> in(boolean condition, Function<T, Object> keyExtractor, Collection<?> values) {
        return condition ? filter(obj ->
        {Object fieldValue = keyExtractor.apply(obj);
            return CollUtil.isNotEmpty(values) && values.contains(fieldValue);}) : this;
    }

    /**
     * notIn：不包含
     *
     * @param keyExtractor
     * @param values
     * @return
     */
    public StreamUtils<T> notIn(Function<T, Object> keyExtractor, Collection<?> values) {
        return filter(obj -> {
            Object fieldValue = keyExtractor.apply(obj);
            return !(CollUtil.isNotEmpty(values) && values.contains(fieldValue));
        });
    }

    /**
     * notIn：不包含
     *
     * @param condition 判断是否参与查询的条件
     * @param keyExtractor
     * @param values
     * @return
     */
    public StreamUtils<T> notIn(boolean condition, Function<T, Object> keyExtractor, Collection<?> values) {
        return condition ? filter(obj -> {
            Object fieldValue = keyExtractor.apply(obj);
            return !(CollUtil.isNotEmpty(values) && values.contains(fieldValue));
        }) : this;
    }

    /**
     * 查找一个
     *
     * @param predicate
     * @return
     */
    public Optional<T> getOne(Predicate<T> predicate) {
        return list.stream().filter(predicate).findFirst();
    }

    /**
     * 查找第一个
     *
     * @param predicate
     * @return
     */
    public T findFirst(Predicate<T> predicate) {
        return list.stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * 查找第一个
     *
     * @return
     */
    public T findFirst() {
        return list.stream().findFirst().orElse(null);
    }

    /**
     * 查找第一个
     *
     * @return
     */
    public T getOne() {
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * not取反操作，
     *
     * @param predicate
     * @return
     */
    public StreamUtils<T> not(Predicate<T> predicate) {
        return filter(predicate.negate());
    }

    /**
     * 并且 and连接符
     *
     * @param other
     * @return
     */
    public StreamUtils<T> and(StreamUtils<T> other) {
        list.retainAll(other.list);
        return this;
    }

    /**
     * 或者
     *
     * @param other
     * @return
     */
    public StreamUtils<T> or(StreamUtils<T> other) {
        other.list.removeAll(list);
        list.addAll(other.list);
        return this;
    }

    /**
     * 默认排序，升序
     *
     * @param comparator
     * @return
     */
    public StreamUtils<T> orderBy(Comparator<T> comparator) {
        list.sort(comparator);
        return this;
    }

    /**
     * 排序，可指定排序方式
     *
     * @param comparator 排序
     * @param orderBy    1：降序，0：升序
     * @return
     */
    public StreamUtils<T> orderBy(Comparator<T> comparator, int orderBy) {
        if (1 == orderBy) {
            list.sort(comparator.reversed());
        } else {
            orderBy(comparator);
        }
        return this;
    }

    /**
     * 排序，可指定排序方式，可以指定条件，条件为true时才执行排序
     *
     * @param comparator 排序
     * @param condition 条件
     * @param orderBy    1：降序，0：升序
     * @return
     */
    public StreamUtils<T> orderBy(boolean condition, Comparator<T> comparator, int orderBy) {
        if (condition) {
            if (1 == orderBy) {
                list.sort(comparator.reversed());
            } else {
                orderBy(comparator);
            }
        }
        return this;
    }

    /**
     * 获取一个List
     *
     * @return
     */
    public List<T> toList() {
        return list;
    }
}
