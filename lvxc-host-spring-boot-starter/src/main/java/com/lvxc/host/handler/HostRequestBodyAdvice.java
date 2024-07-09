package com.lvxc.host.handler;

import com.lvxc.host.annotation.HostAdvice;
import com.lvxc.host.properties.HostProperties;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;

@RestControllerAdvice
@ConditionalOnProperty(prefix = HostProperties.HOST_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class HostRequestBodyAdvice implements HostBodyAdvice, RequestBodyAdvice {
    @Resource
    private HostProperties hostProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(HostAdvice.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @SneakyThrows
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        resolveUrlHost(body, true, hostProperties.getDefaultHost());
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
