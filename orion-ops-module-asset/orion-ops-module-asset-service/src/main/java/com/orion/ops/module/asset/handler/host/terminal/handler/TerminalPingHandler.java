package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalBasePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

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

    @Override
    public void handle(WebSocketSession session, TerminalBasePayload payload) {
        // 发送 pong
        this.send(session, OutputTypeEnum.PONG.getType());
    }

}
