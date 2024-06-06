package com.orion.visor.module.asset.handler.host.terminal.handler;

import com.orion.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * 关闭处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalCloseHandler extends AbstractTerminalHandler<TerminalBasePayload> {

    @Override
    public void handle(WebSocketSession channel, TerminalBasePayload payload) {
        log.info("TerminalCloseHandler-handle start sessionId: {}", payload.getSessionId());
        // 关闭会话
        hostTerminalManager.closeSession(channel.getId(), payload.getSessionId());
    }

}
