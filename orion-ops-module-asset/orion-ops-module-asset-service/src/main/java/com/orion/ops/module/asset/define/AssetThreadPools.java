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

}
