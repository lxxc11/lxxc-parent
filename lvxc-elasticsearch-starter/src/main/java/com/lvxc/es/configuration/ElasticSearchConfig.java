package com.lvxc.es.configuration;


import com.lvxc.es.converter.JSONToList;
import com.lvxc.es.converter.JSONToMap;
import com.lvxc.es.converter.ListToJSON;
import com.lvxc.es.converter.MapToJSON;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

import java.util.ArrayList;
import java.util.List;


@Data
@Configuration
@ConfigurationProperties(prefix = "lvxc.elasticsearch")
public class ElasticSearchConfig {
    /**
     * 地址
     */
    private String address;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 协议
     */
    private String schema;

    /**
     * 连接超时时间
     */
    private int connectTimeout;

    /**
     * Socket 连接超时时间
     */
    private int socketTimeout;

    /**
     * 获取连接的超时时间
     */
    private int connectionRequestTimeout;

    /**
     * 最大连接数
     */
    private int maxConnectNum;

    /**
     * 最大路由连接数
     */
    private int maxConnectPerRoute;

    private RestHighLevelClient restHighLevelClient;


    @Bean("restHighLevelClient")
    public RestHighLevelClient restHighLevelClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials elastic = new UsernamePasswordCredentials(userName, password);
        credentialsProvider.setCredentials(AuthScope.ANY, elastic);

        // 拆分地址
        List<HttpHost> hostLists = new ArrayList<>();
        String[] hostList = address.split(",");
        for (String addr : hostList) {
            String host = addr.split(":")[0];
            String port = addr.split(":")[1];
            hostLists.add(new HttpHost(host, Integer.parseInt(port), schema));
        }
        // 转换成 HttpHost 数组
        HttpHost[] httpHost = hostLists.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeout);
            requestConfigBuilder.setSocketTimeout(socketTimeout);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            return httpClientBuilder;
        });
        restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

    @Bean
    @DependsOn({"restHighLevelClient", "myElasticsearchConverter"})
    public ElasticsearchRestTemplate elasticsearchTemplate(RestHighLevelClient restHighLevelClient, ElasticsearchConverter elasticsearchConverter) throws Exception {
        return new ElasticsearchRestTemplate(restHighLevelClient, elasticsearchConverter);
    }

    @Bean("myElasticsearchConverter")
    public ElasticsearchConverter elasticsearchConverter(SimpleElasticsearchMappingContext mappingContext) {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        defaultConversionService.addConverter(new JSONToList());
        defaultConversionService.addConverter(new JSONToMap());
        defaultConversionService.addConverter(new ListToJSON());
        defaultConversionService.addConverter(new MapToJSON());
        return new MappingElasticsearchConverter(mappingContext, defaultConversionService);
    }
}
