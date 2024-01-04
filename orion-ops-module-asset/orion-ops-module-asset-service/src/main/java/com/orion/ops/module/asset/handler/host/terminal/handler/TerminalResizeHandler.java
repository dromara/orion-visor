package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalManager;
import com.orion.ops.module.asset.handler.host.terminal.model.request.TerminalResizeRequest;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * 修改大小处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalResizeHandler extends AbstractTerminalHandler<TerminalResizeRequest> {

    @Resource
    private TerminalManager terminalManager;

    @Override
    public void handle(WebSocketSession channel, TerminalResizeRequest payload) {
        // 获取会话
        ITerminalSession terminalSession = terminalManager.getSession(channel.getId(), payload.getSession());
        if (terminalSession != null) {
            // 修改大小
            terminalSession.resize(payload.getCols(), payload.getRows());
        }
    }

}
