package com.orion.visor.module.infra.configuration;

import com.orion.visor.module.infra.handler.upload.FileUploadMessageDispatcher;
import com.orion.visor.module.infra.interceptor.FileUploadInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * 基建模块 websocket 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 11:39
 */
@Configuration
public class InfraWebSocketConfiguration implements WebSocketConfigurer {

    @Value("${orion.websocket.prefix}")
    private String prefix;

    @Resource
    private FileUploadInterceptor fileUploadInterceptor;

    @Resource
    private FileUploadMessageDispatcher fileUploadMessageDispatcher;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 文件上传
        registry.addHandler(fileUploadMessageDispatcher, prefix + "/file/upload/{uploadToken}")
                .addInterceptors(fileUploadInterceptor)
                .setAllowedOrigins("*");
    }

}
