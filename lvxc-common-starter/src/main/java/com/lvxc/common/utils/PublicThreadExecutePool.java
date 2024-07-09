package com.lvxc.common.utils;


import cn.hutool.extra.spring.SpringUtil;
import com.lvxc.common.config.thread.ThreadPoolExecutorUtil;

import java.util.concurrent.ExecutorService;

/**
 * 静态线程池工具类
 *
 * @author Administrator
 */
public class PublicThreadExecutePool {
    //公共线程池
    public final static ExecutorService COMMON_POOL = SpringUtil.getBean(ThreadPoolExecutorUtil.class).getPoll("COMMON_POOL");
    /**
     * 延时处理线程池，主要处理不需要等待结果的延时任务
     */
    public final static ExecutorService ASYNC_POOL = SpringUtil.getBean(ThreadPoolExecutorUtil.class).getPoll("ASYNC_POOL");
    /**
     * 多线程处理线程池，主要处理需要获取结果的任务
     */
    public final static ExecutorService MULTI_POOL = SpringUtil.getBean(ThreadPoolExecutorUtil.class).getPoll("MULTI_POOL");
}
