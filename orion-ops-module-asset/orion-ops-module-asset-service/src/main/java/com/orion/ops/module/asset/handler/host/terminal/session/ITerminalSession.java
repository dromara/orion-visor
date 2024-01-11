package com.orion.ops.module.asset.handler.host.terminal.session;

import com.orion.lang.able.SafeCloseable;

/**
 * 终端会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
public interface ITerminalSession extends SafeCloseable {

    /**
     * 连接
     *
     * @param terminalType terminalType
     * @param cols         cols
     * @param rows         rows
     */
    void connect(String terminalType, int cols, int rows);

    /**
     * 重置大小
     *
     * @param cols cols
     * @param rows rows
     */
    void resize(int cols, int rows);

    /**
     * 写入内容
     *
     * @param b b
     */
    void write(String b);

    /**
     * 写入内容
     *
     * @param b b
     */
    void write(byte[] b);

    /**
     * 活跃回话
     */
    void keepAlive();

}
