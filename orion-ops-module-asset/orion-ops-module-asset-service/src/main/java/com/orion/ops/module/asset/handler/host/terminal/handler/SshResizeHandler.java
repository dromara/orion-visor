package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.module.asset.handler.host.terminal.model.request.SshResizeRequest;
import com.orion.ops.module.asset.handler.host.terminal.session.ISshSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * ssh 修改大小处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class SshResizeHandler extends AbstractTerminalHandler<SshResizeRequest> {

    @Override
    public void handle(WebSocketSession channel, SshResizeRequest payload) {
        // 获取会话
        ISshSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        // 修改大小
        session.resize(payload.getCols(), payload.getRows());
    }

}
