package com.orion.ops.framework.storage.config;

import com.orion.ops.framework.storage.core.client.local.LocalFileClientConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 存储配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 18:40
 */
@Data
@ConfigurationProperties(prefix = "orion.storage")
public class StorageConfig {

    /**
     * 本地文件客户端 配置
     */
    private LocalFileClientConfig local;

    /**
     * 日志文件客户端 配置
     */
    private LocalFileClientConfig logs;

}
