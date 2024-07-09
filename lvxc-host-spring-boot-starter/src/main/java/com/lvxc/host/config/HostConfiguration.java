package com.lvxc.host.config;

import com.lvxc.host.properties.HostProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 链接域名配置
 *
 * @author 詹杨锋
 * @since 2023/12/25
 */
@Configuration
@EnableConfigurationProperties(HostProperties.class)
public class HostConfiguration {
}
