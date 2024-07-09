package com.lvxc.flowable.common.config;

import org.flowable.ui.idm.properties.FlowableIdmAppProperties;
import org.flowable.ui.idm.servlet.ApiDispatcherServletConfiguration;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.flowable.ui.modeler.service.FlowableModelQueryService;
import org.flowable.ui.modeler.service.ModelImageService;
import org.flowable.ui.modeler.service.ModelServiceImpl;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.lang.NonNull;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableConfigurationProperties({FlowableIdmAppProperties.class, FlowableModelerAppProperties.class})
@ComponentScan(basePackages = {
        "org.flowable.idm.api",
        "org.flowable.idm.engine.impl",
        "org.flowable.engine.impl",
        "org.flowable.engine.runtime",
        "org.flowable.ui.modeler",
        "org.flowable.ui.idm.conf",
        "org.flowable.ui.idm.rest.app",
        "org.flowable.ui.idm.security",
        "org.flowable.ui.idm.service",
        "org.flowable.ui.modeler.repository",
        "org.flowable.ui.modeler.service",
        "org.flowable.ui.common.service",
        "org.flowable.ui.common.repository",
        "org.flowable.ui.common.security",
        "org.flowable.ui.common.tenant"
        },
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = org.flowable.ui.idm.conf.ApplicationConfiguration.class)})
public class ApplicationConfiguration {


    @Bean
    public ServletRegistrationBean apiServlet(ApplicationContext applicationContext) {
        AnnotationConfigWebApplicationContext dispatcherServletConfiguration = new AnnotationConfigWebApplicationContext();
        dispatcherServletConfiguration.setParent(applicationContext);
        dispatcherServletConfiguration.register(ApiDispatcherServletConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(dispatcherServletConfiguration);
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(servlet, "/api/*");
        registrationBean.setName("Flowable IDM App API Servlet");
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);
        return registrationBean;
    }

    @Bean
    public WebMvcConfigurer modelerApplicationWebMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addViewControllers(@NonNull ViewControllerRegistry registry) {

                if (!ClassUtils.isPresent("org.flowable.ui.task.conf.ApplicationConfiguration", getClass().getClassLoader())) {
                    // If the task application is not present, then the root should be mapped to admin
                    registry.addViewController("/").setViewName("redirect:/modeler/");
                }
                registry.addViewController("/modeler").setViewName("redirect:/modeler/");
                registry.addViewController("/modeler/").setViewName("forward:/modeler/index.html");
            }
        };
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Bean
    public ModelService modelerModelService() {
        return new ModelServiceImpl();
    }

    @Bean
    public ModelImageService modelerModelImageService() {
        return new ModelImageService();
    }

    @Bean
    public FlowableModelQueryService modelerModelQueryService() {
        return new FlowableModelQueryService();
    }
}
