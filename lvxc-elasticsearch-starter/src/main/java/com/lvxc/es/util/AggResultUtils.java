package com.lvxc.es.util;

import com.lvxc.es.domian.ChartEntity;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理es聚会的结果集
 * @author hel
 */
public class AggResultUtils {
    /**
     * 按层级进行对应的排序
     * @param data 元数据
     * @param sort 字符串排序
     * @param layer 层
     * @return
     */
    public static List<ChartEntity> sort(List<ChartEntity> data, List<String> sort, SortPolicy sortPolicy, int layer){
        return sortRoot(data, sort, sortPolicy, layer, 1);
    }

    /**
     * 按层级进行补全排序
     * @param data 元数据
     * @param sort 字符串排序
     * @param layer 层
     * @return
     */
    public static List<ChartEntity> sort(List<ChartEntity> data, List<String> sort, int layer){
        return sortRoot(data, sort, SortPolicy.FULL_SORT, layer, 1);
    }

    /**
     * 默认第一层排序
     * @param data 元数据
     * @param sort 字符串排序
     * @return
     */
    public static List<ChartEntity> sort(List<ChartEntity> data, List<String> sort,SortPolicy sortPolicy){
        return sortRoot(data, sort, sortPolicy,1, 1);
    }
    /**
     * 默认第一层排序,默认补全排序
     * @param data 元数据
     * @param sort 字符串排序
     * @return
     */
    public static List<ChartEntity> sort(List<ChartEntity> data, List<String> sort){
        return sortRoot(data, sort, SortPolicy.FULL_SORT,1, 1);
    }

    private static List<ChartEntity> sortRoot(List<ChartEntity> data, List<String> sort,SortPolicy sortPolicy, int layer, int i){
        if (CollectionUtils.isEmpty(data) && layer != i){
            return data;
        }
        if (layer == i){
            return sortPolicy(data, sort,sortPolicy);
        }
        i++;
        for (ChartEntity datum : data) {
            List<ChartEntity> childChart = datum.getChildChart();
            datum.setChildChart(sortRoot(childChart, sort,sortPolicy , layer, i));
        }
        return data;
    }

    private static List<ChartEntity> sortPolicy(List<ChartEntity> data, List<String> sort, SortPolicy sortPolicy){
        if (Objects.equals(sortPolicy, SortPolicy.SORT)){
            if (CollectionUtils.isEmpty(data)){
                return data;
            }
            data.sort(((o1, o2) -> {
                int max = sort.size() + 20;
                int i = sort.indexOf(o1.getName()) >= 0 ? sort.indexOf(o1.getName()) : max;
                int j = sort.indexOf(o2.getName()) >= 0 ? sort.indexOf(o2.getName()) : max;
                return i - j;
            }));
            return data;
        }
        if (Objects.equals(sortPolicy, SortPolicy.FILTER_SORT)){
            if (CollectionUtils.isEmpty(data)){
                return data;
            }
            return data.stream().filter(o->sort.contains(o.getName())).sorted(((o1, o2) -> {
                int i = sort.indexOf(o1.getName());
                int j = sort.indexOf(o2.getName());
                return i - j;
            })).collect(Collectors.toList());
        }
        Map<String, ChartEntity> dataMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(data)){
            dataMap = data.stream().collect(Collectors.toMap(ChartEntity::getName, x->x));
        }
        List<ChartEntity> result = new ArrayList<>();
        ChartEntity entity = null;
        for (String s : sort) {
            if (dataMap.containsKey(s)){
                entity = dataMap.get(s);
            }else {
                entity = new ChartEntity();
                entity.setName(s);
                entity.setValue("0");
            }
            result.add(entity);
        }
        return result;
    }

    /**
     * 对结果保留几位小数
     * @param data
     * @param scale
     * @return
     */
    public static List<ChartEntity> setScale(List<ChartEntity> data, int scale){
        if (CollectionUtils.isEmpty(data)){
             return data;
        }
        for (ChartEntity datum : data) {
            BigDecimal bigDecimal = new BigDecimal(datum.getValue());
            datum.setValue(bigDecimal.setScale(scale,BigDecimal.ROUND_HALF_UP).toString());
            List<ChartEntity> childChart = datum.getChildChart();
            datum.setChildChart(setScale(childChart, scale));
        }
        return data;
    }

    /**
     * 对结果删除多余的0
     * @param data
     * @return
     */
    public static List<ChartEntity> toPlainString(List<ChartEntity> data){
        if (CollectionUtils.isEmpty(data)){
            return data;
        }
        for (ChartEntity datum : data) {
            BigDecimal bigDecimal = new BigDecimal(datum.getValue());
            datum.setValue(bigDecimal.stripTrailingZeros().toPlainString());
            List<ChartEntity> childChart = datum.getChildChart();
            datum.setChildChart(toPlainString(childChart));
        }
        return data;
    }
    /**
     * 对结果删除多余的0
     * @param data
     * @param scale
     * @return
     */
    public static List<ChartEntity> toPlainString(List<ChartEntity> data, int scale){
        if (CollectionUtils.isEmpty(data)){
            return data;
        }
        for (ChartEntity datum : data) {
            BigDecimal bigDecimal = new BigDecimal(datum.getValue());
            datum.setValue(bigDecimal.setScale(scale,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
            List<ChartEntity> childChart = datum.getChildChart();
            datum.setChildChart(toPlainString(childChart, scale));
        }
        return data;
    }

    /**
     * 获取对应层级的所有分类
     * @param data
     * @param layer
     * @return
     */
    public static Collection<String> getCategory(List<ChartEntity> data, int layer){
        return getCategoryRoot(data, layer, 1);
    }
    private static Collection<String> getCategoryRoot(List<ChartEntity> data, int layer, int i){
        if (CollectionUtils.isEmpty(data)){
            return new HashSet<>();
        }
        if (layer == i){
            return getCategoryPolicy(data);
        }
        i++;
        Set<String> result = new LinkedHashSet<>();
        for (ChartEntity datum : data) {
            List<ChartEntity> childChart = datum.getChildChart();
            Collection<String> categoryRoot = getCategoryRoot(childChart, layer, i);
            if (!CollectionUtils.isEmpty(categoryRoot)){
                result.addAll(categoryRoot);
            }
        }
        return result;
    }

    private static Collection<String> getCategoryPolicy(List<ChartEntity> data){
        if (CollectionUtils.isEmpty(data)){
            return new LinkedHashSet<>();
        }
        return data.stream().map(ChartEntity::getName).collect(Collectors.toSet());
    }

    public enum SortPolicy{
        //排序
        SORT,
        //补全并排序
        FULL_SORT,
        //过滤并排序
        FILTER_SORT
        ;
    }

    public static void main(String[] args) {
        List<ChartEntity> result = new ArrayList<>();
        result = AggResultUtils.sort(result, Arrays.asList("A","B","C","D"));
        result.get(0).setValue("1000");
        result = AggResultUtils.sort(result, Arrays.asList("A","C","B"), SortPolicy.SORT,1);
        result = AggResultUtils.sort(result, Arrays.asList("A","C","B","G"),2);
        result.get(0).setChildChart(AggResultUtils.sort(result.get(0).getChildChart(), Arrays.asList("E","T","D"), 1));
        result = AggResultUtils.setScale(result, 2);
        Collection<String> category = AggResultUtils.getCategory(result, 2);
        int i =2;
    }

}
