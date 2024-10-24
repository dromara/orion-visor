/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.module.asset.define;

import com.orion.lang.define.thread.ExecutorBuilder;
import com.orion.visor.framework.common.constant.Const;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 资产线程池
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 11:21
 */
public interface AssetThreadPools {

    /**
     * 超时检查线程池
     */
    ThreadPoolExecutor TIMEOUT_CHECK = ExecutorBuilder.create()
            .namedThreadFactory("timeout-check-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    /**
     * terminal 标准输出线程池
     */
    ThreadPoolExecutor TERMINAL_STDOUT = ExecutorBuilder.create()
            .namedThreadFactory("terminal-stdout-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    /**
     * terminal 操作线程池
     */
    ThreadPoolExecutor TERMINAL_OPERATOR = ExecutorBuilder.create()
            .namedThreadFactory("terminal-operator-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    /**
     * 批量执行任务线程池
     */
    ThreadPoolExecutor EXEC_TASK = ExecutorBuilder.create()
            .namedThreadFactory("exec-task-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    /**
     * 批量执行主机命令线程池
     */
    ThreadPoolExecutor EXEC_HOST = ExecutorBuilder.create()
            .namedThreadFactory("exec-host-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    /**
     * 批量执行日志查看线程池
     */
    ThreadPoolExecutor EXEC_LOG = ExecutorBuilder.create()
            .namedThreadFactory("exec-log-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    /**
     * 批量上传任务线程池
     */
    ThreadPoolExecutor UPLOAD_TASK = ExecutorBuilder.create()
            .namedThreadFactory("upload-task-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    /**
     * 批量上传主机线程池
     */
    ThreadPoolExecutor UPLOAD_HOST = ExecutorBuilder.create()
            .namedThreadFactory("upload-host-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

}
