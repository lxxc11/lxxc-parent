package com.lvxc.common.enums;

/**
 * @author cc
 *
 * SpringBoot + Redis 实现接口限流
 */
public enum LimitType {
    /**
     * 默认策略全局限流
     */
    DEFAULT,
    /**
     * 根据请求者IP进行限流
     */
    IP
}
