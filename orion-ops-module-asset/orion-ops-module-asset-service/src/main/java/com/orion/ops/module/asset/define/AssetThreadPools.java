package com.orion.ops.module.asset.define;

import com.orion.lang.define.thread.ExecutorBuilder;
import com.orion.ops.framework.common.constant.Const;

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

}
