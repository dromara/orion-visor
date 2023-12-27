package com.orion.ops.framework.websocket.core.utils;

import com.orion.lang.exception.AuthenticationException;
import com.orion.lang.exception.ConnectionRuntimeException;
import com.orion.lang.exception.DisabledException;
import com.orion.lang.exception.TimeoutException;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Urls;
import com.orion.ops.framework.websocket.core.constant.WsCloseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;

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
     * 发送消息 忽略并发报错
     *
     * @param session session
     * @param message message
     */
    public static void sendText(WebSocketSession session, byte[] message) {
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

    /**
     * 获取 urlToken
     *
     * @param request request
     * @return token
     */
    public static String getToken(ServerHttpRequest request) {
        return Urls.getUrlSource(Objects.requireNonNull(request.getURI().toString()));
    }

    /**
     * 获取 urlToken
     *
     * @param session session
     * @return token
     */
    public static String getToken(WebSocketSession session) {
        return Urls.getUrlSource(Objects.requireNonNull(session.getUri()).toString());
    }

    /**
     * 打开 session 异常关闭
     *
     * @param session session
     * @param e       e
     */
    public static void openSessionStoreThrowClose(WebSocketSession session, Exception e) {
        if (Exceptions.isCausedBy(e, TimeoutException.class)) {
            close(session, WsCloseCode.CONNECTION_TIMEOUT);
        } else if (Exceptions.isCausedBy(e, ConnectionRuntimeException.class)) {
            close(session, WsCloseCode.CONNECTION_FAILURE);
        } else if (Exceptions.isCausedBy(e, AuthenticationException.class)) {
            close(session, WsCloseCode.CONNECTION_AUTH_FAILURE);
        } else if (Exceptions.isCausedBy(e, DisabledException.class)) {
            close(session, WsCloseCode.HOST_DISABLED);
        } else {
            close(session, WsCloseCode.CONNECTION_EXCEPTION);
        }
    }

}
