package com.lvxc.multidatasource;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lvxc.mybatisplus.handler.AutoFillMetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class MybatisPlusSqlSessionFactoryConfig {
    public static final String db1_session_factory = "db1SessionFactory";
    public static final String db2_session_factory = "db2SessionFactory";
    public static final String db3_session_factory = "db3SessionFactory";

    @Autowired
    @Qualifier("globalConfig")
    private GlobalConfig globalConfig;

    @Primary
    @ConditionalOnProperty(value = MultiDataSourceConfig.db1_path + ".enable", havingValue = "true")
    @Bean(name = MybatisPlusSqlSessionFactoryConfig.db1_session_factory)
    public SqlSessionFactory db1SqlSessionFactory(
            @Qualifier(MultiDataSourceConfig.db1_ds) DataSource dataSource,
            MybatisPlusInterceptor mybatisPlusInterceptor,
            AutoFillMetaObjectHandler autoFillMetaObjectHandler,
            @Value("${spring.datasource.db1.mapper-locations:#{'classpath:com/lvxc/mapper/db1/xml/*.xml'}}") String xmlPath,
            @Value("${spring.datasource.db1.type-handler-package:#{'com.lvxc.common.typehandler'}}") String typeHandlersPackage,
            Environment environment
    ) throws Exception {
        return getSqlSessionFactory(dataSource, mybatisPlusInterceptor, autoFillMetaObjectHandler, xmlPath, typeHandlersPackage, environment);
    }

    @ConditionalOnProperty(value = MultiDataSourceConfig.db2_path + ".enable", havingValue = "true")
    @Bean(name = MybatisPlusSqlSessionFactoryConfig.db2_session_factory)
    public SqlSessionFactory db2SqlSessionFactory(
            @Qualifier(MultiDataSourceConfig.db2_ds) DataSource dataSource,
            MybatisPlusInterceptor mybatisPlusInterceptor,
            AutoFillMetaObjectHandler autoFillMetaObjectHandler,
            @Value("${spring.datasource.db2.mapper-locations:#{'classpath:com/lvxc/mapper/db2/xml/*.xml'}}") String xmlPath,
            @Value("${spring.datasource.db2.type-handler-package:#{'com.lvxc.common.typehandler'}}") String typeHandlersPackage,
            Environment environment
    ) throws Exception {
        return getSqlSessionFactory(dataSource, mybatisPlusInterceptor, autoFillMetaObjectHandler, xmlPath, typeHandlersPackage, environment);
    }


    @ConditionalOnProperty(value = MultiDataSourceConfig.db3_path + ".enable", havingValue = "true")
    @Bean(name = MybatisPlusSqlSessionFactoryConfig.db3_session_factory)
    public SqlSessionFactory db3SqlSessionFactory(
            @Qualifier(MultiDataSourceConfig.db3_ds) DataSource dataSource,
            MybatisPlusInterceptor mybatisPlusInterceptor,
            AutoFillMetaObjectHandler autoFillMetaObjectHandler,
            @Value("${spring.datasource.db3.mapper-locations:#{'classpath:com/lvxc/mapper/db3/xml/*.xml'}}") String xmlPath,
            @Value("${spring.datasource.db3.type-handler-package:#{'com.lvxc.common.typehandler'}}") String typeHandlersPackage,
            Environment environment
    ) throws Exception {
        return getSqlSessionFactory(dataSource, mybatisPlusInterceptor, autoFillMetaObjectHandler, xmlPath, typeHandlersPackage, environment);
    }

    private SqlSessionFactory getSqlSessionFactory(DataSource dataSource, MybatisPlusInterceptor mybatisPlusInterceptor, AutoFillMetaObjectHandler autoFillMetaObjectHandler, String xmlPath, String typeHandlersPackage, Environment environment) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        // 全局配置插件
        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        globalConfig.setMetaObjectHandler(autoFillMetaObjectHandler);
        sessionFactoryBean.setGlobalConfig(globalConfig);
        // 数据源
        sessionFactoryBean.setDataSource(dataSource);
        // 日志
        MybatisConfiguration configuration = new MybatisConfiguration();
        if (environment.getActiveProfiles().length == 0 || Arrays.asList("dev", "local", "feature").contains(environment.getActiveProfiles()[0])) {
            configuration.setLogImpl(StdOutImpl.class);
        }
        sessionFactoryBean.setConfiguration(configuration);
        // 插件
        Interceptor[] plugins = new Interceptor[]{mybatisPlusInterceptor};
        sessionFactoryBean.setPlugins(plugins);

        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        String[] mapperLocations = StringUtils.tokenizeToStringArray(xmlPath, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

        List<Resource> resources = new ArrayList();
        if (!CollectionUtils.isEmpty(Arrays.asList(mapperLocations))) {
            for (String mapperLocation : mapperLocations) {
                try {
                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException e) {
                    log.error("Get myBatis resources happened exception", e);
                }
            }
        }

        sessionFactoryBean.setMapperLocations(resources.toArray(new Resource[resources.size()]));
        // 类型转换
        sessionFactoryBean.setTypeHandlersPackage(typeHandlersPackage);
        return sessionFactoryBean.getObject();
    }

    @Primary
    @ConditionalOnProperty(value = MultiDataSourceConfig.db1_path + ".enable", havingValue = "true")
    @MapperScan(
            basePackages = {"${spring.datasource.db1.mapper-package:com.lvxc.mapper.db1}"},
            sqlSessionFactoryRef = MybatisPlusSqlSessionFactoryConfig.db1_session_factory
    )
    class db1Mapper {

    }

    @ConditionalOnProperty(value = MultiDataSourceConfig.db2_path + ".enable", havingValue = "true")
    @MapperScan(
            basePackages = {"${spring.datasource.db2.mapper-package:com.lvxc.mapper.db2}"},
            sqlSessionFactoryRef = MybatisPlusSqlSessionFactoryConfig.db2_session_factory
    )
    class db2Mapper {

    }

    @ConditionalOnProperty(value = MultiDataSourceConfig.db3_path + ".enable", havingValue = "true")
    @MapperScan(
            basePackages = {"${spring.datasource.db3.mapper-package:com.lvxc.mapper.db3}"},
            sqlSessionFactoryRef = MybatisPlusSqlSessionFactoryConfig.db3_session_factory
    )
    class db3Mapper {

    }
}
