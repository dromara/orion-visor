package com.orion.ops.module.asset.handler.host.exec.command.handler;

import com.orion.lang.able.SafeCloseable;

import java.util.List;

/**
 * 执行任务处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 11:43
 */
public interface IExecTaskHandler extends Runnable, SafeCloseable {

    /**
     * 获取主机执行器
     *
     * @return handlers
     */
    List<IExecCommandHandler> getHandlers();

    /**
     * 中断执行
     */
    void interrupt();

}
