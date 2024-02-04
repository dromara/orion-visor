package com.orion.ops.module.asset.handler.host.terminal.session;

/**
 * sftp 会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/4 16:48
 */
public interface ISftpSession extends ITerminalSession {

    /**
     * 建立连接
     */
    void connect();

}
