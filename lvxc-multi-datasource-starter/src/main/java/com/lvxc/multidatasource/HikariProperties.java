package com.lvxc.multidatasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class HikariProperties {
    private Integer maximumPoolSize = 15;
    private Integer minimumIdle = 5;
    private Boolean autoCommit = true;
    private Long idleTimeout = 30000L;
    private Long maxLifetime = 1800000L;
    private Long connectionTimeout = 30000L;
    private String connectionTestQuery = "SELECT 1";
    private String poolName = "HikariCP";









}
