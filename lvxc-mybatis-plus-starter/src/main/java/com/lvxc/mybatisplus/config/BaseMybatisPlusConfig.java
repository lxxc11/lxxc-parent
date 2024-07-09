package com.lvxc.mybatisplus.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lvxc.mybatisplus.handler.AutoFillMetaObjectHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseMybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor mysqlInnerInterceptor = new PaginationInnerInterceptor(DbType.POSTGRE_SQL);
        // 设置sql的limit为无限制，默认是500
        mysqlInnerInterceptor.setMaxLimit(-1L);
        mybatisPlusInterceptor.addInnerInterceptor(mysqlInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    @ConditionalOnMissingBean(MetaObjectHandler.class)

    @Bean
    public AutoFillMetaObjectHandler autoFillMetaObjectHandler() {
        return new AutoFillMetaObjectHandler();
    }
}
