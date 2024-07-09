package com.lvxc.common.config;

import com.lvxc.common.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class EmailConfig {

    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.isLocal}")
    private Boolean isLocal;

    @Value("${mail.debug}")
    private String debug;




    @Bean
    public void initEmail(){
        EmailUtil.setFrom(from);
        EmailUtil.setHost(host);
        EmailUtil.setPort(port);
        EmailUtil.setUsername(username);
        EmailUtil.setPassword(password);
        EmailUtil.setIsLocal(isLocal);
        EmailUtil.setDebug(debug);
    }



}
