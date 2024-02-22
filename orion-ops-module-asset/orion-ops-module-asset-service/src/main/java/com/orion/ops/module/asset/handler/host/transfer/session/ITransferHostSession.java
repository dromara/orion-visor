package com.orion.ops.module.asset.handler.host.transfer.session;

import com.orion.lang.able.SafeCloseable;

/**
 * 主机传输会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 23:06
 */
public interface ITransferHostSession extends SafeCloseable {

    /**
     * 初始化
     */
    void init();

}
