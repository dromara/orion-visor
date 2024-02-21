package com.orion.ops.module.asset.handler.host.sftp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * sftp 传输消息处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 18:22
 */
@Slf4j
@Component
public class TransferMessageHandler extends AbstractWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("text");
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        System.out.println("binary");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("TransferMessageHandler-afterConnectionEstablished id: {}", session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.error("TransferMessageHandler-handleTransportError id: {}", session.getId(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String id = session.getId();
        log.info("TransferMessageHandler-afterConnectionClosed  id: {}, code: {}, reason: {}", id, status.getCode(), status.getReason());
    }

}
