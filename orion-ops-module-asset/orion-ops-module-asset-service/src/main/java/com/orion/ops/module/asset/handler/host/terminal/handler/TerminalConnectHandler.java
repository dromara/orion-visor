package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.module.asset.handler.host.terminal.entity.Message;
import com.orion.ops.module.asset.handler.host.terminal.entity.request.TerminalConnectRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * 连接主机处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Component
public class TerminalConnectHandler extends AbstractTerminalHandler<TerminalConnectRequest> {

    public TerminalConnectHandler() {
        super(TerminalConnectRequest.class);
    }

    @Override
    protected void onMessage(WebSocketSession session, Message<TerminalConnectRequest> msg) {
        System.out.println(msg);
    }

}
