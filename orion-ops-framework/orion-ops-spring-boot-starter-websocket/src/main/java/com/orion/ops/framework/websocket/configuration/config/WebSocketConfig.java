package com.orion.ops.framework.websocket.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * websocket 配置属性
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 19:57
 */
@Data
@ConfigurationProperties("orion.websocket")
public class WebSocketConfig {

    /**
     * 二进制消息缓冲区大小 byte
     */
    private Integer binaryBufferSize;

    /**
     * 文本消息缓冲区大小 byte
     */
    private Integer textBufferSize;

    /**
     * session 最大超时时间 ms
     */
    private Long sessionIdleTimeout;

    public WebSocketConfig() {
        this.binaryBufferSize = 1024 * 1024;
        this.textBufferSize = 1024 * 1024;
        this.sessionIdleTimeout = 30 * 60 * 1000L;
    }

}
