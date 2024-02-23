package com.orion.ops.module.asset.handler.host.transfer;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.io.Streams;
import com.orion.ops.module.asset.handler.host.transfer.handler.ITransferHandler;
import com.orion.ops.module.asset.handler.host.transfer.handler.TransferHandler;
import com.orion.ops.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * sftp 传输消息处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 18:22
 */
@Slf4j
@Component
public class TransferMessageDispatcher extends AbstractWebSocketHandler {

    private final ConcurrentHashMap<String, ITransferHandler> handlers = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 获取处理器
        ITransferHandler handler = handlers.computeIfAbsent(session.getId(), s -> new TransferHandler(session));
        // 处理消息
        handler.handleMessage(JSON.parseObject(message.getPayload(), TransferOperatorRequest.class));
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        handlers.get(session.getId()).putContent(message.getPayload().array());
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
        log.info("TransferMessageHandler-afterConnectionClosed id: {}, code: {}, reason: {}", id, status.getCode(), status.getReason());
        // 关闭会话
        Streams.close(handlers.get(id));
    }

}
