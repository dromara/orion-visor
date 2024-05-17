package com.orion.visor.framework.common.thread;

import com.orion.visor.framework.common.utils.ThreadMdcUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 自动注入 MDC 异步线程池
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/10 15:16
 */
public class ThreadPoolMdcTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        super.execute(ThreadMdcUtils.wrap(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(ThreadMdcUtils.wrap(task));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(ThreadMdcUtils.wrap(task));
    }

}
