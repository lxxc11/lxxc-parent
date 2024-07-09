package com.lvxc.encrypt.config;

import com.lvxc.encrypt.properties.DataEncryptProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 数据加密自动装配类
 *
 * @author 詹杨锋
 * @since 2023/12/1
 */
@Configuration
@EnableConfigurationProperties(DataEncryptProperties.class)
public class DataEncryptConfiguration {
}
