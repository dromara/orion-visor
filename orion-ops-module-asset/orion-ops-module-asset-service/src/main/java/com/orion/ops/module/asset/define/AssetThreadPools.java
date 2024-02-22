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
     * terminal 调度线程池
     */
    ThreadPoolExecutor TERMINAL_SCHEDULER = ExecutorBuilder.create()
            .namedThreadFactory("terminal-thread-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

    /**
     * SFTP 下载线程池
     */
    ThreadPoolExecutor SFTP_DOWNLOAD_SCHEDULER = ExecutorBuilder.create()
            .namedThreadFactory("sftp-download-thread-")
            .corePoolSize(1)
            .maxPoolSize(Integer.MAX_VALUE)
            .keepAliveTime(Const.MS_S_60)
            .workQueue(new SynchronousQueue<>())
            .allowCoreThreadTimeout(true)
            .build();

}
