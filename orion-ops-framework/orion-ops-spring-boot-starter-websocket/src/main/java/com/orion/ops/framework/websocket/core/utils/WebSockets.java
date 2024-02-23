package com.orion.ops.framework.websocket.core.utils;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Exceptions;
import com.orion.ops.framework.websocket.core.constant.WsCloseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * websocket 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/14 0:36
 */
@Slf4j
public class WebSockets {

    private WebSockets() {
    }

    /**
     * 获取属性
     *
     * @param channel channel
     * @param attr    attr
     * @param <E>     T
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <E> E getAttr(WebSocketSession channel, String attr) {
        return (E) channel.getAttributes().get(attr);
    }

    /**
     * 发送消息 忽略并发报错
     *
     * @param session session
     * @param message message
     */
    public static void sendJson(WebSocketSession session, Object message) {
        sendText(session, JSON.toJSONString(message));
    }

    /**
     * 发送消息 忽略并发报错
     *
     * @param session session
     * @param message message
     */
    public static void sendText(WebSocketSession session, String message) {
        if (!session.isOpen()) {
            return;
        }
        try {
            // 响应
            session.sendMessage(new TextMessage(message));
        } catch (IllegalStateException e) {
            // 并发异常
            log.error("发送消息失败 {}", Exceptions.getDigest(e));
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    /**
     * 关闭会话
     *
     * @param session session
     * @param code    code
     */
    public static void close(WebSocketSession session, WsCloseCode code) {
        if (!session.isOpen()) {
            return;
        }
        try {
            session.close(new CloseStatus(code.getCode(), code.getReason()));
        } catch (Exception e) {
            log.error("websocket close failure", e);
        }
    }

}
