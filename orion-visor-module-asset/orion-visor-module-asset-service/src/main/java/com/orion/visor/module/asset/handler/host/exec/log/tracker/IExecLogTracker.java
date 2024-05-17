package com.orion.visor.module.asset.handler.host.exec.log.tracker;

import com.orion.ext.tail.handler.DataHandler;
import com.orion.lang.able.SafeCloseable;

/**
 * log tracker 定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 23:00
 */
public interface IExecLogTracker extends Runnable, DataHandler, SafeCloseable {

    /**
     * 设置最后修改时间
     */
    void setLastModify();

    /**
     * 获取 id
     *
     * @return id
     */
    String getTrackerId();

    /**
     * 获取路径
     *
     * @return path
     */
    String getPath();

    /**
     * 获取绝对路径
     *
     * @return 绝对路径
     */
    String getAbsolutePath();

}
