package com.orion.ops.framework.websocket.core.interceptor;

import com.orion.ops.framework.common.security.SecurityHolder;
import com.orion.ops.framework.websocket.core.constant.WsAttr;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 20:16
 */
public class UserHandshakeInterceptor implements HandshakeInterceptor {

    @Resource
    private SecurityHolder securityHolder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // TODO TEST
        attributes.put(WsAttr.USER, securityHolder.getLoginUserId());
        // if (user == null){
        //     return false;
        // response.setStatusCode(HttpStatus.MULTI_STATUS);
        // }
        // HttpSessionHandshakeInterceptor
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}
