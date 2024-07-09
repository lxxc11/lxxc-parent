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

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 线程池配置属性类
 * @author https://juejin.im/entry/5abb8f6951882555677e9da2
 */
@Data
@Component
public class AsyncTaskProperties {
    @Value("${task.pool.core-pool-size:10}")
    public int corePoolSize;
    @Value("${task.pool.max-pool-size:30}")
    public int maxPoolSize;
    @Value("${task.pool.keep-alive-seconds:60}")
    public int keepAliveSeconds;
    @Value("${task.pool.queue-capacity:200}")
    public int queueCapacity;

}
