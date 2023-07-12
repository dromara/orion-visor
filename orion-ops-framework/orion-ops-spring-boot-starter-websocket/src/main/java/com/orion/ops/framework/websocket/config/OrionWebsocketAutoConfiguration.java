package com.orion.ops.framework.websocket.config;

import com.orion.ops.framework.common.constant.AutoConfigureOrderConst;
import com.orion.ops.framework.websocket.core.WebsocketContainerConfig;
import com.orion.ops.framework.websocket.interceptor.UserHandshakeInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * websocket 配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 19:45
 */
@EnableWebSocket
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_WEBSOCKET)
@EnableConfigurationProperties(WebsocketContainerConfig.class)
public class OrionWebsocketAutoConfiguration {

    /**
     * @return websocket 缓冲区大小配置
     */
    @Bean
    public ServletServerContainerFactoryBean servletServerContainerFactoryBean(WebsocketContainerConfig config) {
        ServletServerContainerFactoryBean factory = new ServletServerContainerFactoryBean();
        factory.setMaxBinaryMessageBufferSize(config.getBinaryBufferSize());
        factory.setMaxTextMessageBufferSize(config.getBinaryBufferSize());
        factory.setMaxSessionIdleTimeout(config.getSessionIdleTimeout());
        return factory;
    }

    /**
     * @return 用户认证拦截器 按需注入
     */
    @Bean
    public HandshakeInterceptor userHandshakeInterceptor() {
        return new UserHandshakeInterceptor();
    }

}
