package com.lvxc.es.annotation;

import org.springframework.data.annotation.Persistent;

import java.lang.annotation.*;

@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Deprecated
public @interface EsDocument {
    String indexName();
}
