/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.lvxc.common.config.thread;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于获取自定义线程池
 *
 * @author Zheng Jie
 */
@Configuration
public class ThreadPoolExecutorUtil {
    @Autowired
    private AsyncTaskProperties taskProperties;
    public ExecutorService getPoll() {
        return getPoll(null);
    }

    public ExecutorService getPoll(String threadName) {
        return new ThreadPoolExecutor(
                taskProperties.corePoolSize,
                taskProperties.maxPoolSize,
                taskProperties.keepAliveSeconds,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(taskProperties.queueCapacity),
                new DefaultThreadFactory(StringUtil.isNullOrEmpty(threadName) ? "default" : threadName),
                // 队列与线程池中线程都满了时使用调用者所在的线程来执行
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
