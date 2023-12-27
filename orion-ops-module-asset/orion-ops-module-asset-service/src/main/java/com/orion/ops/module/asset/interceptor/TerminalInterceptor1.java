package com.orion.ops.module.asset.interceptor;

import com.orion.ops.framework.websocket.core.interceptor.UserHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/27 23:53
 */
// @Configuration
public class TerminalInterceptor1 implements WebSocketConfigurer {

    @Resource
    private UserHandshakeInterceptor userHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new TextWebSocketHandler() {
                    @Override
                    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                        System.out.println(message);
                    }
                }, "/orion/keep-alive/host/terminal1")
                .addInterceptors(userHandshakeInterceptor)
                .setAllowedOrigins("*");
        System.out.println("1231");
    }
}
