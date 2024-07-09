package com.lvxc.common.config;


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
public class Knife4jCommonConfiguration {

    @Bean(value = "commonBean")
    public Docket commonBean() {
        //指定使用Swagger2规范
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("接口文档")
                        .termsOfServiceUrl("https://www.baidu.com/")
                        .contact(new Contact("developer","","lvxc@163.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("通用组件服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.lvxc.common"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}