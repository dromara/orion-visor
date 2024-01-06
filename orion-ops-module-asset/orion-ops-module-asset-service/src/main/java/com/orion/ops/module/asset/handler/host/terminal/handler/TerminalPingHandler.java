package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.utils.collect.Maps;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalManager;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ping 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalPingHandler extends AbstractTerminalHandler<TerminalBasePayload> {

    @Resource
    private TerminalManager terminalManager;

    @Override
    public void handle(WebSocketSession channel, TerminalBasePayload payload) {
        // 发送 pong
        this.send(channel, OutputTypeEnum.PONG.getType());
        // 活跃 terminal
        Map<String, ITerminalSession> sessions = terminalManager.getSession(channel.getId());
        if (!Maps.isEmpty(sessions)) {
            for (ITerminalSession session : sessions.values()) {
                session.keepAlive();
            }
        }
    }

}
