package com.orion.ops.module.asset.handler.host.terminal.session;

import com.orion.lang.able.SafeCloseable;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalConfig;

/**
 * 终端会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
public interface ITerminalSession extends SafeCloseable {

    /**
     * 获取 sessionId
     *
     * @return sessionId
     */
    String getSessionId();

    /**
     * 获取 channelId
     *
     * @return channelId
     */
    String getChannelId();

    /**
     * 获取配置
     *
     * @return config
     */
    TerminalConfig getConfig();

    /**
     * 活跃会话
     */
    void keepAlive();

    /**
     * 强制下线
     */
    void forceOffline();

}
