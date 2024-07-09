package com.lvxc.host.handler;

import com.lvxc.host.annotation.HostAdvice;
import com.lvxc.host.properties.HostProperties;
import com.lvxc.web.common.base.ResponseResult;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

@RestControllerAdvice
@ConditionalOnProperty(prefix = HostProperties.HOST_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class HostResponseBodyAdvice implements HostBodyAdvice, ResponseBodyAdvice<Object> {
    @Resource
    private HostProperties hostProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return returnType.hasMethodAnnotation(HostAdvice.class);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Object result;
        if(body instanceof ResponseResult)
            result = ((ResponseResult) body).getData();
        else
            result = body;

        if (result != null) {
            String className = result.getClass().getName();
            if (className.contains("Page")) {
                Method method = result.getClass().getMethod("getRecords");
                List<?> list = (List<?>) method.invoke(result);
                for (Object o : list) {
                    resolveUrlHost(o, false, hostProperties.getDefaultHost());
                }
            } else if (result instanceof Collection) {
                for (Object o : (Collection<?>) result) {
                    resolveUrlHost(o, false, hostProperties.getDefaultHost());
                }
            } else {
                resolveUrlHost(result, false, hostProperties.getDefaultHost());
            }
        }

        return body;
    }
}
