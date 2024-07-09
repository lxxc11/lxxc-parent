package com.lvxc.common.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author 周锐
 * @Version 1.0
 * @Description 通过实现InitializingBean，重写afterPropertiesSet，在初始化Bean后，为成员变量设置属性值
 */
@Component
public class TencentConfig implements InitializingBean {

    @Value("${tencent.secretID}")
    private String secretID;

    @Value("${tencent.secretKey}")
    private String secretKey;

    @Value("${tencent.region}")
    private String region;

    public static String SECRET_ID;

    public static String SECRET_KEY;

    public static String REGION;

    @Override
    public void afterPropertiesSet() {
        SECRET_ID = secretID;
        SECRET_KEY = secretKey;
        REGION = region;
    }
}