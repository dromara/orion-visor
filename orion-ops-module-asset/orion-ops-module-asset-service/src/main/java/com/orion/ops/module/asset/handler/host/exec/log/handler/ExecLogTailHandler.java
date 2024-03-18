package com.orion.ops.module.asset.handler.host.exec.log.handler;

import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.entity.dto.ExecLogTailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * 执行日志查看处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 18:38
 */
@Slf4j
@Component
public class ExecLogTailHandler extends AbstractWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("ExecLogTailHandler-afterConnectionEstablished id: {}", session.getId());
        // 获取参数
        ExecLogTailDTO info = WebSockets.getAttr(session, ExtraFieldConst.INFO);


    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("ExecLogTailHandler-handleTransportError id: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String id = session.getId();
        log.info("ExecLogTailHandler-afterConnectionClosed id: {}, code: {}, reason: {}", id, status.getCode(), status.getReason());
        // 关闭会话
    }

}
