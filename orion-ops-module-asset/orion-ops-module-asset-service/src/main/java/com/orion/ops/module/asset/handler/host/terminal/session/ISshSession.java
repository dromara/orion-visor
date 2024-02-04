package com.orion.ops.module.asset.handler.host.terminal.session;

/**
 * ssh 会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/4 16:47
 */
public interface ISshSession extends ITerminalSession {

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

}
