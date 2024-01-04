package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalManager;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

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

    @Resource
    private TerminalManager terminalManager;

    @Override
    public void handle(WebSocketSession channel, TerminalBasePayload payload) {
        log.info("TerminalCloseHandler-handle start session: {}", payload.getSession());
        // 关闭会话
        terminalManager.closeSession(channel.getId(), payload.getSession());
    }

}
