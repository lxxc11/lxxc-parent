package com.lvxc.host.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.lvxc.host.annotation.Host;

import java.lang.reflect.Field;

public interface HostBodyAdvice {
    default void resolveUrlHost(Object obj, boolean remove, String defaultHost) throws Throwable {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Host fileHost = field.getAnnotation(Host.class);
                if (fileHost != null) {
                    field.setAccessible(true);
                    String url = (String) field.get(obj);
                    if (StrUtil.isNotBlank(url)) {
                        String path = URLUtil.getPath(url);
                        if (remove) {
                            url = path;
                            field.set(obj, url);
                        } else {
                            String oldHost = URLUtil.url(url).getHost();
                            String newHost = defaultHost;
                            if (StrUtil.isNotBlank(fileHost.value()))
                                newHost = fileHost.value();
                            if (!newHost.equals(oldHost)) {
                                url = newHost + path;
                                field.set(obj, url);
                            }
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
