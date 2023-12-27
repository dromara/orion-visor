package com.orion.ops.module.asset.interceptor;

import com.orion.ops.framework.websocket.core.interceptor.UserHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/27 23:53
 */
@Configuration
public class TerminalInterceptor implements WebSocketConfigurer {

    // https://blog.csdn.net/oNew_Lifeo/article/details/130003676
    // https://wstool.js.org/

    @Resource
    private UserHandshakeInterceptor userHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler1(), "/orion/keep-alive/host/terminal")
                .addInterceptors(userHandshakeInterceptor)
                .setAllowedOrigins("*");
        System.out.println("123");
    }

    static class WebSocketHandler1 implements WebSocketHandler {

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println(1);
        }

        @Override
        public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
            System.out.println(message);
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            System.out.println(1);
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
            System.out.println(1);
        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }
    }

}
