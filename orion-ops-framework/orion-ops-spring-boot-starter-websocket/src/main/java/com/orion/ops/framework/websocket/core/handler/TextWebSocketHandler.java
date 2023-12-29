package com.orion.ops.framework.websocket.core.handler;

import org.springframework.web.socket.*;

/**
 * 文本类型消息处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 18:23
 */
public abstract class TextWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        // 非 text message 不处理
        if (!(message instanceof TextMessage)) {
            return;
        }
        // 处理消息
        this.onMessage(session, (String) message.getPayload());
    }

    /**
     * 处理消息
     *
     * @param session session
     * @param payload payload
     */
    public abstract void onMessage(WebSocketSession session, String payload);

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
