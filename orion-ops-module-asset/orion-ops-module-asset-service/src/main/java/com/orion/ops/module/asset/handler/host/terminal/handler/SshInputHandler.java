package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.module.asset.handler.host.terminal.model.request.SshInputRequest;
import com.orion.ops.module.asset.handler.host.terminal.session.ISshSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * ssh 处理输入处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class SshInputHandler extends AbstractTerminalHandler<SshInputRequest> {

    @Override
    public void handle(WebSocketSession channel, SshInputRequest payload) {
        // 获取会话
        ISshSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        // 处理输入
        session.write(payload.getCommand());
    }

}
