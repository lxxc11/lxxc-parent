package com.lvxc.user.config;

import com.lvxc.user.common.constants.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceInitializerConfig {
    @javax.annotation.Resource
    private DataSource dataSource;

    @Value("classpath:user_system_init.sql")
    private Resource ddl;
    @Value("classpath:user_system_data.sql")
    private Resource dml;



    @Bean
    @ConditionalOnProperty(prefix = CommonConstant.INITUSER,name = "enable",havingValue = "true")
    public DataSourceInitializer dataSourceInitializer() {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        // 设置数据源
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            jdbcTemplate.queryForMap("select count(1) from sys_user");
        } catch (DataAccessException e) {
            log.warn("sys_user 表不存在，开始进行初始化操作");
            // 报错,表不存在,初次导入，执行相关脚本
            populator.addScripts(ddl);
            populator.addScripts(dml);
        } catch (Exception e) {
            log.warn("初始化脚本报错:" + e.getMessage());
        }
        return populator;
    }

}
