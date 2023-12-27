package com.orion.ops.framework.websocket.config;

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
@ConfigurationProperties("spring.websocket")
public class WebsocketConfig {

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

}
