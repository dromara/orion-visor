package com.orion.ops.framework.websocket.interceptor;

import com.orion.ops.framework.websocket.constant.WsAttr;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 用户拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 20:16
 */
public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // TODO 获取当前用户
        attributes.put(WsAttr.USER, 1);
        // if (user == null){
        //     return false;
        // }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}
