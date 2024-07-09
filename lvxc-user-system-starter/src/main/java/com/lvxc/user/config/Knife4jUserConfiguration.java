package com.lvxc.user.config;


import com.lvxc.user.common.constants.CommonConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@Profile({"dev","test"})
public class Knife4jUserConfiguration {
    @Bean(value = "userDocket")
    @ConditionalOnProperty(prefix = CommonConstant.INITUSER,name = "enable",havingValue = "true")
    public Docket userDocket() {
        //指定使用Swagger2规范
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("接口文档")
                        .termsOfServiceUrl("https://www.baidu.com/")
                        .contact(new Contact("developer","","developer@163.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("用户系统服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.lvxc.user.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}