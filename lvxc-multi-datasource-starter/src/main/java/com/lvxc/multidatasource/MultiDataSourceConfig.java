package com.lvxc.multidatasource;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class MultiDataSourceConfig {
    public static final String db1_ds = "db1DataSource";
    public static final String db2_ds = "db2DataSource";
    public static final String db3_ds = "db3DataSource";
    public static final String db1_path = "spring.datasource.db1";
    public static final String db2_path = "spring.datasource.db2";
    public static final String db3_path = "spring.datasource.db3";
    public static final String db1_tm = "db1DataSourceTransactionManager";
    public static final String db2_tm = "db2DataSourceTransactionManager";
    public static final String db3_tm = "db3DataSourceTransactionManager";


    @ConditionalOnProperty(value = db1_path + ".enable", havingValue = "true")
    @Bean(db1_ds)
    @Primary
    public DataSource initdb1DataSource(
            Environment environment,
            HikariProperties hikariProperties
    ) {
        return new TransactionAwareDataSourceProxy(new HikariDataSource(
                getHikariConfig(
                        environment.getProperty(db1_path + ".url"),
                        environment.getProperty(db1_path + ".username"),
                        environment.getProperty(db1_path + ".password"),
                        environment.getProperty(db1_path + ".schema"),
                        hikariProperties
                )
        ));
    }

    @ConditionalOnProperty(value = db2_path + ".enable", havingValue = "true")
    @Bean(db2_ds)
    public DataSource initdb2DataSource(
            Environment environment,
            HikariProperties hikariProperties
    ) {
        return new TransactionAwareDataSourceProxy(new HikariDataSource(
                getHikariConfig(
                        environment.getProperty(db2_path + ".url"),
                        environment.getProperty(db2_path + ".username"),
                        environment.getProperty(db2_path + ".password"),
                        environment.getProperty(db2_path + ".schema"),
                        hikariProperties
                )
        ));
    }

    @ConditionalOnProperty(value = db3_path + ".enable", havingValue = "true")
    @Bean(db3_ds)
    public DataSource initdb3DataSource(
            Environment environment,
            HikariProperties hikariProperties
    ) {
        return new TransactionAwareDataSourceProxy(new HikariDataSource(
                getHikariConfig(
                        environment.getProperty(db3_path + ".url"),
                        environment.getProperty(db3_path + ".username"),
                        environment.getProperty(db3_path + ".password"),
                        environment.getProperty(db3_path + ".schema"),
                        hikariProperties
                )
        ));
    }


    private HikariConfig getHikariConfig(String url, String username, String password, String schema, HikariProperties hikariProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setSchema(schema);
        config.setMaximumPoolSize(hikariProperties.getMaximumPoolSize());
        config.setMinimumIdle(hikariProperties.getMinimumIdle());
        config.setAutoCommit(hikariProperties.getAutoCommit());
        config.setIdleTimeout(hikariProperties.getIdleTimeout());
        config.setMaxLifetime(hikariProperties.getMaxLifetime());
        config.setConnectionTimeout(hikariProperties.getConnectionTimeout());
        config.setConnectionTestQuery(hikariProperties.getConnectionTestQuery());
        config.setPoolName(hikariProperties.getPoolName());
        return config;
    }

    @Primary
    @Bean(db1_tm)
    public DataSourceTransactionManager db1DataSourceTransactionManager(Environment environment, @Qualifier(MultiDataSourceConfig.db1_ds) DataSource dataSource,
                                                                        ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        DataSourceTransactionManager transactionManager = createTransactionManager(environment, dataSource);
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
        return transactionManager;
    }

    @ConditionalOnProperty(value = db2_path + ".enable", havingValue = "true")
    @Bean(db2_tm)
    public DataSourceTransactionManager db2DataSourceTransactionManager(Environment environment, @Qualifier(MultiDataSourceConfig.db2_ds) DataSource dataSource,
                                                                        ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        DataSourceTransactionManager transactionManager = createTransactionManager(environment, dataSource);
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
        return transactionManager;
    }

    @ConditionalOnProperty(value = db3_path + ".enable", havingValue = "true")
    @Bean(db3_tm)
    public DataSourceTransactionManager db3DataSourceTransactionManager(Environment environment, @Qualifier(MultiDataSourceConfig.db3_ds) DataSource dataSource,
                                                                        ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        DataSourceTransactionManager transactionManager = createTransactionManager(environment, dataSource);
        transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
        return transactionManager;
    }

    private DataSourceTransactionManager createTransactionManager(Environment environment, DataSource dataSource) {
        return environment.getProperty("spring.dao.exceptiontranslation.enabled", Boolean.class, Boolean.TRUE)
                ? new JdbcTransactionManager(dataSource) : new DataSourceTransactionManager(dataSource);
    }

    @Scope("prototype")
    @Bean(name = "globalConfig")
    @ConfigurationProperties(prefix = "mybatis-plus.global-config")
    public GlobalConfig globalConfig(){
        GlobalConfig globalConfig = new GlobalConfig();
        return globalConfig;
    }

}
