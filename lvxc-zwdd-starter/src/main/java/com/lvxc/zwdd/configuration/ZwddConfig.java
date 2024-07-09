package com.lvxc.zwdd.configuration;

import com.alibaba.xxpt.gateway.shared.client.http.ExecutableClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "lvxc.zwdd")
public class ZwddConfig {

    private String appkey ;

    private String appsecret ;

    private String appdomain ;

    private String tenantid;

    @Bean
    public ExecutableClient executableClient() {
        ExecutableClient executableClient = ExecutableClient.getInstance();
        //DomainName不同环境对应不同域名，示例为sass域名
        executableClient.setDomainName(appdomain);
        executableClient.setProtocal("https");
        //应用App Key
        executableClient.setAccessKey(appkey);
        //应用App Secret
        executableClient.setSecretKey(appsecret);
        executableClient.init();
        return  executableClient;
    }

}
