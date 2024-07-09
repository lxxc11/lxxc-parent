package com.lvxc.es.util;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.lvxc.es.domian.Page;
import com.lvxc.es.domian.ChartEntity;
import com.lvxc.es.enums.AggsFunEnum;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedReverseNested;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.ParsedSingleValueNumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.ParsedValueCount;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ESService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private OrikaUtil orikaUtil;

    private static final String NESTED_FLAG = "#";

    public <T> Page<T> page(NativeSearchQuery searchQuery, Class<T> documentClass) {
        SearchHits<T> search = elasticsearchRestTemplate.search(searchQuery, documentClass);
        List<SearchHit<T>> searchHits = search.getSearchHits();
        List<T> list = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        long count = elasticsearchRestTemplate.count(searchQuery, documentClass);
        return new Page<T>(count, list);
    }

    public <T, S> Page<S> page(QueryBuilder query, PageRequest pageRequest, Class<T> clazz, Class<S> targetClass) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        nativeSearchQuery.setPageable(pageRequest);
        SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQuery, clazz);
        List<SearchHit<T>> searchHits = search.getSearchHits();
        List<T> list = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        long count = elasticsearchRestTemplate.count(nativeSearchQuery, clazz);
        return new Page<S>(count, orikaUtil.mapAsList(list, targetClass));
    }

    public <T, S> Page<S> page(QueryBuilder query, PageRequest pageRequest, Sort sort, Class<T> clazz, Class<S> targetClass) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        nativeSearchQuery.setPageable(pageRequest);
        nativeSearchQuery.addSort(sort);
        SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQuery, clazz);
        List<SearchHit<T>> searchHits = search.getSearchHits();
        List<T> list = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        long count = elasticsearchRestTemplate.count(nativeSearchQuery, clazz);
        return new Page<S>(count, orikaUtil.mapAsList(list, targetClass));
    }

    public <T> List<T> list(QueryBuilder query, PageRequest pageRequest, Class<T> clazz) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
//        PageRequest.of(1,10)
        nativeSearchQuery.setPageable(pageRequest);
        SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQuery, clazz);
        List<SearchHit<T>> searchHits = search.getSearchHits();
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public <T> List<T> list(NativeSearchQuery searchQuery, Class<T> documentClass) {
        SearchHits<T> search = elasticsearchRestTemplate.search(searchQuery, documentClass);
        List<SearchHit<T>> searchHits = search.getSearchHits();
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public <T> List<T> list(QueryBuilder query, Class<T> clazz) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQuery, clazz);
        List<SearchHit<T>> searchHits = search.getSearchHits();
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public <T> List<T> list(QueryBuilder query, PageRequest pageRequest, Sort sort, Class<T> clazz, List<String> fields) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        if (ObjectUtil.isNotEmpty(fields)) {
            nativeSearchQuery.setFields(fields);
        }
        nativeSearchQuery.setPageable(pageRequest);
        nativeSearchQuery.addSort(sort);
        SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQuery, clazz);
        List<SearchHit<T>> searchHits = search.getSearchHits();
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public <T> Long count(QueryBuilder query, Class<T> clazz) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        return elasticsearchRestTemplate.count(nativeSearchQuery, clazz);
    }

    public <T, S> Page<S> page(QueryBuilder query, PageRequest pageRequest, Sort sort, Class<T> clazz, Class<S> targetClass, FieldSortBuilder fieldSortBuilder) {
        if (ObjectUtil.isEmpty(fieldSortBuilder)) {
            return page(query,
                    pageRequest,
                    sort,
                    clazz,
                    targetClass);
        }
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query, null, Arrays.asList(fieldSortBuilder));
        nativeSearchQuery.setPageable(pageRequest);
        nativeSearchQuery.addSort(sort);
        SearchHits<T> search = elasticsearchRestTemplate.search(nativeSearchQuery, clazz);
        List<SearchHit<T>> searchHits = search.getSearchHits();
        List<T> list = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
        long count = elasticsearchRestTemplate.count(nativeSearchQuery, clazz);
        return new Page(count, orikaUtil.mapAsList(list, targetClass));
    }

    /**
     * 如果是nested分组，nested相关的分组条件也必须包含在aggsField中
     * 示例对hs_ind_list#level_second分组，正确传参为“hs_ind_list#graph_code(此参数为过滤条件，没有则不需要)”，“hs_ind_list#level_second”，然后对结果for循环过滤
     *
     * @param esEntityClass
     * @param query
     * @param aggsField
     * @return
     */
    public List<ChartEntity> esAggsSearch(Class<?> esEntityClass, QueryBuilder query,
                                          String... aggsField) {
        List<ChartEntity> result = new ArrayList<>();
        if (esEntityClass == null || aggsField.length == 0) {
            return result;
        }
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        nativeSearchQuery.setMaxResults(0);
        nativeSearchQuery.addAggregation(aggsBuilder(aggsField));
        SearchHits<?> search = elasticsearchRestTemplate.search(nativeSearchQuery, esEntityClass);
        AggregationsContainer<?> aggregations = search.getAggregations();
        Aggregations aggregation = (Aggregations) aggregations.aggregations();
        //遍历结果，并将结果按层级放入ChartEntity
        return buildChartEntity(nestedToTerms(aggregation.get(aggsField[0]), aggsField[0]), aggsField, aggsField.length);
    }
    /**
     * nested分组
     * 如果要对多个不同的nested字段分组，请勿使用该方法
     * @param query      查询条件
     * @param nestBoolFilter bool类型的过滤条件，，如果查询条件中包含分组的nested字段，那么需要传入过滤条件
     * @param clazz      实体类
     * @param aggsField  分组字段，示例：hs_ind_list#graph_code
     * @param <T>        实体类
     * @return 分组结果
     */
    public <T> List<ChartEntity> aggs(QueryBuilder query, QueryBuilder nestBoolFilter, Class<T> clazz, String... aggsField) {
        if (nestBoolFilter == null) {
            return this.esAggsSearch(clazz, query, aggsField);
        }
        List<ChartEntity> result = new ArrayList();
        if (clazz != null && aggsField.length != 0) {
            NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withQuery(query).withMaxResults(0);
            searchQueryBuilder.addAggregation(this.recursionAggsBuild(false, nestBoolFilter, 1000, 1, aggsField));
            SearchHits<T> search = elasticsearchRestTemplate.search(searchQueryBuilder.build(), clazz);
            AggregationsContainer<?> aggregations = search.getAggregations();
            if (aggregations == null) {
                return result;
            }
            Aggregations aggregation = (Aggregations) aggregations.aggregations();
            return this.dealAggsResult(aggregation, aggsField);
        } else {
            return result;
        }
    }

    public List<ChartEntity> esFirstFieldDateRangeAggsSearch(Class<?> esEntityClass, QueryBuilder query, boolean cumulative,
                                                             List<String> times, String dateFormat,
                                                             String... aggsField) {
        List<ChartEntity> result = new ArrayList<>();
        String[] fields = new String[aggsField.length - 1];
        if (esEntityClass == null || aggsField.length == 0) {
            return result;
        }
        //按照特定时间格式区间聚合
        DateRangeAggregationBuilder dateRange = AggregationBuilders.dateRange(aggsField[0])
                .field(aggsField[0]).format(dateFormat);
        for (int i = 0; i < times.size() - 1; i++) {
            if (cumulative) {
                dateRange.addRange(times.get(i), null, times.get(i + 1));
            } else {
                dateRange.addRange(times.get(i), times.get(i), times.get(i + 1));
            }
        }
        //对传参aggsField数组其他元素进行聚合查询（group by）
        if (aggsField.length > 1) {
            System.arraycopy(aggsField, 1, fields, 0, fields.length);
            dateRange.subAggregation(aggsBuilder(fields));
        }
        NativeSearchQuery sq = new NativeSearchQuery(query);
        sq.setMaxResults(0);
        sq.addAggregation(dateRange);
        SearchHits<?> search = elasticsearchRestTemplate.search(sq, esEntityClass);
        AggregationsContainer<?> aggregations = search.getAggregations();
        Aggregations aggregation = (Aggregations) aggregations.aggregations();
        ParsedDateRange internalDateRange = aggregation.get(aggsField[0]);
        //遍历结果，并将结果按层级放入ChartEntity
        for (Range.Bucket bucket : internalDateRange.getBuckets()) {
            ChartEntity chartEntity = new ChartEntity();
            chartEntity.setName(bucket.getKeyAsString());
            chartEntity.setValue(String.valueOf(bucket.getDocCount()));
            if (fields.length > 0) {
                Aggregation aggregation1 = bucket.getAggregations().asMap().get(fields[0]);
                chartEntity.setChildChart(buildChartEntity(nestedToTerms(aggregation1, fields[0]), fields, fields.length));
            } else {
                chartEntity.setChildChart(new ArrayList<>());
            }
            result.add(chartEntity);
        }
        return result;
    }

    public List<ChartEntity> esFunAggsSearch(Class<?> esEntityClass, AggsFunEnum aggsFunEnum,
                                             Integer newScale, QueryBuilder query, String... aggsField) {
        return this.esFunAggsSearchRoot(esEntityClass, aggsFunEnum, newScale, query, aggsField);
    }

    private List<ChartEntity> esFunAggsSearchRoot(Class<?> esEntityClass, AggsFunEnum aggsFunEnum,
                                                  Integer newScale, QueryBuilder query, String... aggsField) {
        List<ChartEntity> result = new ArrayList<>();
        if (esEntityClass == null || aggsField.length == 0) {
            return result;
        }
        NativeSearchQuery searchQuery = new NativeSearchQuery(query);
        searchQuery.setMaxResults(0);
        searchQuery.addAggregation(funAggsBuilder(aggsFunEnum, aggsField));
        SearchHits<?> search = elasticsearchRestTemplate.search(searchQuery, esEntityClass);
        AggregationsContainer<?> aggregations = search.getAggregations();
        Aggregations aggregation = (Aggregations) aggregations.aggregations();

        //遍历结果，并将结果按层级放入ChartEntity
        return funAggsBuildChart(aggregation.get(aggsField[0]), null, aggsField, 0, newScale);
    }

    /**
     * 仅支持terms与最内层为函数式的聚合封装
     *
     * @param aggsFunEnum
     * @param aggsField
     * @return
     */
    private AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> funAggsBuilder(
            AggsFunEnum aggsFunEnum, String[] aggsField
    ) {
        AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> aggs = null;
        if (aggsField != null && aggsFunEnum != null) {
            if (AggsFunEnum.FUN_SUM.equals(aggsFunEnum)) {
                aggs = AggregationBuilders.sum(aggsField[aggsField.length - 1]).field(aggsField[aggsField.length - 1].replace(NESTED_FLAG, "."));
            }
            if (AggsFunEnum.FUN_AVG.equals(aggsFunEnum)) {
                aggs = AggregationBuilders.avg(aggsField[aggsField.length - 1]).field(aggsField[aggsField.length - 1].replace(NESTED_FLAG, "."));
            }
            if (AggsFunEnum.FUN_MAX.equals(aggsFunEnum)) {
                aggs = AggregationBuilders.max(aggsField[aggsField.length - 1]).field(aggsField[aggsField.length - 1].replace(NESTED_FLAG, "."));
            }
            if (AggsFunEnum.FUN_MIN.equals(aggsFunEnum)) {
                aggs = AggregationBuilders.min(aggsField[aggsField.length - 1]).field(aggsField[aggsField.length - 1].replace(NESTED_FLAG, "."));
            }
            if (AggsFunEnum.FUN_COUNT.equals(aggsFunEnum)) {
                aggs = AggregationBuilders.count(aggsField[aggsField.length - 1]).field(aggsField[aggsField.length - 1].replace(NESTED_FLAG, "."));
            }
            if (aggsField.length == 1 && aggsField[aggsField.length - 1].contains(NESTED_FLAG)) {
                String[] split = aggsField[aggsField.length - 1].split(NESTED_FLAG);
                aggs = AggregationBuilders.nested(aggsField[aggsField.length - 1], split[0]).subAggregation(aggs);
                return aggs;
            }
            if (aggsField.length == 1 && !aggsField[aggsField.length - 1].contains(NESTED_FLAG)) {
                return aggs;
            }
            if (aggsField[aggsField.length - 1].contains(NESTED_FLAG)) {
                String[] split = aggsField[aggsField.length - 1].split(NESTED_FLAG);
                if (aggsField[aggsField.length - 2].contains(NESTED_FLAG)) {
                    String[] split1 = aggsField[aggsField.length - 2].split(NESTED_FLAG);
                    if (!split1[0].equals(split[0])) {
                        throw new RuntimeException("此方法不支持NESTED结构跨父级得函数式聚合");
                    }
                } else {
                    aggs = AggregationBuilders.nested(aggsField[aggsField.length - 1], split[0]).subAggregation(aggs);
                }
            } else if (aggsField[aggsField.length - 2].contains(NESTED_FLAG)) {
                aggs = AggregationBuilders.reverseNested(aggsField[aggsField.length - 1]).subAggregation(aggs);
            }
            if (aggs != null) {
                for (int i = aggsField.length - 2; i >= 0; i--) {
                    aggs = AggregationBuilders.terms(aggsField[i]).field(aggsField[i].replace(NESTED_FLAG, ".")).size(1000)
                            .subAggregation(aggs);
                    if (i > 0 && aggsField[i].contains(NESTED_FLAG)) {
                        String[] split = aggsField[i].split(NESTED_FLAG);
                        if (aggsField[i - 1].contains(NESTED_FLAG)) {
                            String[] split1 = aggsField[i - 1].split(NESTED_FLAG);
                            if (!split1[0].equals(split[0])) {
                                throw new RuntimeException("此方法不支持NESTED结构跨父级得函数式聚合");
                            }
                        } else {
                            aggs = AggregationBuilders.nested(aggsField[i], split[0]).subAggregation(aggs);
                        }
                    }else if (i > 0 && !aggsField[i].contains(NESTED_FLAG) && aggsField[i - 1].contains(NESTED_FLAG)){
                        aggs = AggregationBuilders.reverseNested(aggsField[i]).subAggregation(aggs);
                    }else if (i == 0 && aggsField[i].contains(NESTED_FLAG)) {
                        String[] split = aggsField[i].split(NESTED_FLAG);
                        aggs = AggregationBuilders.nested(aggsField[i], split[0]).subAggregation(aggs);
                    }
                }
            }
        }
        return aggs;
    }

    private List<ChartEntity> funAggsBuildChart(Aggregation bucket,ChartEntity chart, String[] fields, int size,int newScale) {
        List<ChartEntity> result = new ArrayList<>();
        if (bucket != null && fields != null) {
            ChartEntity chartEntity;
            if (bucket instanceof Terms) {
                for (Terms.Bucket bucket1 : ((Terms)bucket).getBuckets()) {
                    chartEntity = new ChartEntity();
                    chartEntity.setName(bucket1.getKeyAsString());
                    chartEntity.setValue(String.valueOf(bucket1.getDocCount()));
                    if (fields.length - size != fields.length - 1) {
                        chartEntity.setChildChart(funAggsBuildChart(getNestedChildAgg(bucket1.getAggregations().asMap()
                                .get(fields[fields.length - size + 1]), fields[fields.length - size + 1]),chartEntity, fields, size - 1,newScale));
                    } else {
                        if (fields[fields.length - 1].contains(NESTED_FLAG)) {
                            ParsedReverseNested aggregation = (ParsedReverseNested) bucket1.getAggregations().asMap()
                                    .get(fields[fields.length - 1].split(NESTED_FLAG)[0]);
                            chartEntity.setValue(String.valueOf(aggregation.getDocCount()));
                        }
                    }
                    result.add(chartEntity);
                }
            }else if(bucket instanceof ParsedReverseNested || bucket instanceof ParsedNested ){
                return funAggsBuildChart(getNestedChildAgg(bucket, fields[fields.length - size]),chart, fields, size,newScale);
            }else {
                if (fields.length == 1) {
                    chart = new ChartEntity();
                    chart.setName("全部");
                    result.add(chart);
                }
                NumericMetricsAggregation.SingleValue aggregation1 = null;
                if (bucket instanceof ParsedValueCount){
                    aggregation1 = (ParsedValueCount) bucket;
                }else {
                    aggregation1 = (ParsedSingleValueNumericMetricsAggregation) bucket;
                }
                if (chart != null) {
                    BigDecimal decimal = new BigDecimal(aggregation1.getValueAsString());
                    chart.setValue(decimal.setScale(newScale, BigDecimal.ROUND_HALF_UP).toString());
                }
            }
        }
        return result;
    }

    private Aggregation getNestedChildAgg(Aggregation aggregation, String termsName) {
        if (aggregation instanceof ParsedReverseNested) {
            Aggregation aggregation1 = ((ParsedReverseNested) aggregation).getAggregations().asMap().get(termsName);
            if (aggregation1 instanceof ParsedNested) {
                return ((ParsedNested) aggregation1).getAggregations().asMap().get(termsName);
            } else {
                return aggregation1;
            }
        }
        if (aggregation instanceof ParsedNested) {
            return ((ParsedNested) aggregation).getAggregations().asMap().get(termsName);
        } else {
            return aggregation;
        }
    }

    private AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> aggsBuilder(String[] fields) {
        return aggsBuilderRoot(fields, "count", false);
    }

    private AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> aggsBuilderRoot(String[] fields, String order, Boolean sort) {
        if (fields == null || fields.length == 0) {
            return null;
        }
        AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> rootAggs = null;
        for (int i = fields.length - 1; i >= 0; i--) {
            String fieldsName = fields[i].replace(NESTED_FLAG, ".");
            TermsAggregationBuilder nextTerms = AggregationBuilders.terms(fields[i])
                    .field(fieldsName).size(1000).order(BucketOrder.key(true));
            if ("count".equalsIgnoreCase(order)) {
                nextTerms.order(BucketOrder.count(sort));
            } else {
                nextTerms.order(BucketOrder.key(sort));
            }
            if (i == fields.length - 1 && fields[i].contains(NESTED_FLAG)) {
                nextTerms.subAggregation(AggregationBuilders.reverseNested(fields[i].split(NESTED_FLAG)[0]));
            }
            if (i != fields.length - 1) {
                AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> lastAggs = rootAggs;
                nextTerms.subAggregation(lastAggs);
            }
            if (i > 0) {
                if (fields[i - 1].contains(NESTED_FLAG)) {
                    String parentPath = fields[i - 1].split(NESTED_FLAG)[0];
                    if (fields[i].contains(NESTED_FLAG)) {
                        String path = fields[i].split(NESTED_FLAG)[0];
                        NestedAggregationBuilder nestedChild = AggregationBuilders.nested(fields[i], path)
                                .subAggregation(nextTerms);
                        if (!path.equals(parentPath)) {
                            rootAggs = AggregationBuilders.reverseNested(fields[i]).subAggregation(nestedChild);
                        } else {
                            rootAggs = nextTerms;
                        }
                    } else {
                        rootAggs = AggregationBuilders.reverseNested(fields[i]).subAggregation(nextTerms);
                    }
                } else {
                    if (fields[i].contains(NESTED_FLAG)) {
                        String path = fields[i].split(NESTED_FLAG)[0];
                        rootAggs = AggregationBuilders.nested(fields[i], path)
                                .subAggregation(nextTerms);
                    } else {
                        rootAggs = nextTerms;
                    }
                }
            } else {
                if (fields[i].contains(NESTED_FLAG)) {
                    String path = fields[i].split(NESTED_FLAG)[0];
                    rootAggs = AggregationBuilders.nested(fields[i], path)
                            .subAggregation(nextTerms);
                } else {
                    rootAggs = nextTerms;
                }
            }
        }
        return rootAggs;
    }

    private Terms nestedToTerms(Aggregation aggregation, String termsName) {
        if (aggregation instanceof ParsedReverseNested) {
            Aggregation aggregation1 = ((ParsedReverseNested) aggregation).getAggregations().asMap().get(termsName);
            if (aggregation1 instanceof ParsedNested) {
                return (Terms) ((ParsedNested) aggregation1).getAggregations().asMap().get(termsName);
            } else {
                return (Terms) aggregation1;
            }
        }
        if (aggregation instanceof ParsedNested) {
            return (Terms) ((ParsedNested) aggregation).getAggregations().asMap().get(termsName);
        } else {
            return (Terms) aggregation;
        }
    }

    private List<ChartEntity> buildChartEntity(Terms bucket, String[] fields, int size) {
        List<ChartEntity> result = new ArrayList<>();
        if (bucket != null && fields != null) {
            ChartEntity chartEntity;
            for (Terms.Bucket bucket1 : bucket.getBuckets()) {
                chartEntity = new ChartEntity();
                chartEntity.setName(bucket1.getKeyAsString());
                chartEntity.setValue(String.valueOf(bucket1.getDocCount()));
                if (fields.length - size != fields.length - 1) {
                    chartEntity.setChildChart(buildChartEntity(nestedToTerms(bucket1.getAggregations().asMap()
                                            .get(fields[fields.length - size + 1]),
                                    fields[fields.length - size + 1]), fields,
                            size - 1));
                } else {
                    if (fields[fields.length - 1].contains(NESTED_FLAG)) {
                        ParsedReverseNested aggregation = (ParsedReverseNested) bucket1.getAggregations().asMap()
                                .get(fields[fields.length - 1].split(NESTED_FLAG)[0]);
                        chartEntity.setValue(String.valueOf(aggregation.getDocCount()));
                    }
                }
                result.add(chartEntity);
            }
        }
        return result;
    }
    /**
     * 递归组合aggs，过滤filter
     *
     * @return aggs
     */
    private AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> recursionAggsBuild(boolean filteredFlag, QueryBuilder nestedFilter, int size, int depth, String... aggsFields) {
        AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> rootAggs = null;
        if (depth > aggsFields.length) {
            return rootAggs;
        }
        String aggsField = aggsFields[depth - 1];
        if (aggsField.contains(NESTED_FLAG)) {
            String[] split = aggsField.split(NESTED_FLAG);
            if (filteredFlag) {
                AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> childAggs = this.recursionAggsBuild(filteredFlag, nestedFilter, size, depth + 1, aggsFields);
                TermsAggregationBuilder termsAggs = AggregationBuilders.terms(aggsField).field(split[0] + "." + split[1]).size(size);
                if (childAggs != null) {
                    termsAggs.subAggregation(childAggs
                    );
                }
                rootAggs = termsAggs.subAggregation(
                        AggregationBuilders.reverseNested("reverse")
                );
            } else {
                filteredFlag = true;
                AbstractAggregationBuilder<? extends AbstractAggregationBuilder<?>> childAggs = this.recursionAggsBuild(filteredFlag, nestedFilter, size, depth + 1, aggsFields);
                TermsAggregationBuilder termsAggs = AggregationBuilders.terms(aggsField).field(split[0] + "." + split[1]).size(size);
                if (childAggs != null) {
                    termsAggs.subAggregation(childAggs);
                }
                rootAggs = AggregationBuilders.nested(split[0], split[0]).subAggregation(
                        AggregationBuilders.filter("filter", nestedFilter).subAggregation(
                                termsAggs.subAggregation(
                                        AggregationBuilders.reverseNested("reverse")
                                )
                        ).subAggregation(
                                AggregationBuilders.reverseNested("reverse")
                        )
                );
            }
        } else {
            rootAggs = AggregationBuilders.terms(aggsField).field(aggsField).size(size).subAggregation(
                    this.recursionAggsBuild(filteredFlag, nestedFilter, size, depth + 1, aggsFields)
            );
        }
        return rootAggs;
    }

    private <T> List<ChartEntity> dealAggsResult(Aggregations aggs, String... aggsFields) {
        String aggsField = aggsFields[0];
        List<? extends Terms.Bucket> buckets;
        boolean filteredFlag = false;
        if (aggsField.contains(NESTED_FLAG)) {
            filteredFlag = true;
            String[] split = aggsField.split(NESTED_FLAG);
            ParsedNested nested = aggs.get(split[0]);
            ParsedFilter filter = nested.getAggregations().get("filter");
            ParsedStringTerms terms = filter.getAggregations().get(aggsField);
            buckets = terms.getBuckets();
        } else {
            ParsedStringTerms terms = aggs.get(aggsField);
            buckets = terms.getBuckets();
        }
        return recursionChildren(buckets, 1, null, filteredFlag, aggsFields);
    }

    /**
     * 递归获取子级
     *
     * @param bucketList 分组结果
     * @param parentName 父级名称
     * @param aggsFields 分组字段
     * @return 子级
     */
    private List<ChartEntity> recursionChildren(List<? extends Terms.Bucket> bucketList, int depth,
                                                String parentName, boolean filteredFlag, String... aggsFields) {
        List<ChartEntity> resultList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(bucketList)) {
            for (Terms.Bucket bucket : bucketList) {
                ChartEntity chartEntity = new ChartEntity();
                chartEntity.setName(bucket.getKeyAsString());
                String curAggsField = aggsFields[depth-1];
                if (curAggsField.contains(NESTED_FLAG)) {
                    ParsedReverseNested reverse = bucket.getAggregations().get("reverse");
                    chartEntity.setValue(Convert.toStr(reverse.getDocCount()));
                } else {
                    chartEntity.setValue(Convert.toStr(bucket.getDocCount()));
                }
                if (depth < aggsFields.length) {
                    String aggsField = aggsFields[depth];
                    if (aggsField.contains(NESTED_FLAG)) {
                        String[] split = aggsField.split(NESTED_FLAG);
                        ParsedStringTerms terms = null;
                        if (filteredFlag) {
                            terms = bucket.getAggregations().get(aggsField);
                        } else {
                            ParsedNested nested = bucket.getAggregations().get(split[0]);
                            ParsedFilter filter = nested.getAggregations().get("filter");
                            terms = filter.getAggregations().get(aggsField);
                        }
                        List<? extends Terms.Bucket> buckets = terms.getBuckets();
                        chartEntity.setChildChart(recursionChildren(buckets, depth + 1, bucket.getKeyAsString(), filteredFlag, aggsFields));
                    } else {
                        ParsedStringTerms terms = bucket.getAggregations().get(aggsField);
                        List<? extends Terms.Bucket> buckets = terms.getBuckets();
                        chartEntity.setChildChart(recursionChildren(buckets, depth + 1, bucket.getKeyAsString(), filteredFlag, aggsFields));
                    }
                }
                resultList.add(chartEntity);
            }
        }
        return resultList;
    }

}
