package com.lvxc.host.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = HostProperties.HOST_PREFIX)
@Data
@Component
public class HostProperties {
    public static final String HOST_PREFIX = "lvxc.host";

    private boolean enable = false;

    private String defaultHost;
}
