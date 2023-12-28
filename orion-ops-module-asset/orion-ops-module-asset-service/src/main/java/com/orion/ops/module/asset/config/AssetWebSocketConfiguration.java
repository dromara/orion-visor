package com.orion.ops.module.asset.config;

import com.orion.ops.module.asset.handler.host.terminal.TerminalDispatchHandler;
import com.orion.ops.module.asset.interceptor.TerminalInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * 资产模块 websocket 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 11:39
 */
@Configuration
public class AssetWebSocketConfiguration implements WebSocketConfigurer {

    @Value("${orion.websocket.prefix}")
    private String prefix;

    @Resource
    private TerminalInterceptor terminalInterceptor;

    @Resource
    private TerminalDispatchHandler terminalDispatchHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 终端
        registry.addHandler(terminalDispatchHandler, prefix + "/host/terminal/{token}")
                .addInterceptors(terminalInterceptor)
                .setAllowedOrigins("*");
    }

}
