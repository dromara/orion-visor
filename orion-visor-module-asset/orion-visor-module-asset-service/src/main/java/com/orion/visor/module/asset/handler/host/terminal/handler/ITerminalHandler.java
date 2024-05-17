package com.orion.visor.module.asset.handler.host.terminal.handler;

import com.orion.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import org.springframework.web.socket.WebSocketSession;

/**
 * 终端消息处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 18:53
 */
public interface ITerminalHandler<T extends TerminalBasePayload> {

    /**
     * 处理消息
     *
     * @param channel channel
     * @param payload payload
     */
    void handle(WebSocketSession channel, T payload);

}
