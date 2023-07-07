package com.orion.ops.framework.storage.config;

import com.orion.ops.framework.storage.core.client.FileClient;
import com.orion.ops.framework.storage.core.client.local.LocalFileClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

/**
 * 存储配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 16:21
 * <p>
 * TODO 后续添加 FAST MINIO OSS 等
 */
@AutoConfiguration
@EnableConfigurationProperties(StorageConfig.class)
public class OrionStorageAutoConfiguration {

    @Resource
    private StorageConfig config;

    /**
     * 本地文件客户端
     */
    @Bean
    @Primary
    @ConditionalOnProperty(value = "orion.storage.local.enabled", havingValue = "true")
    public FileClient localFileClient() {
        return new LocalFileClient(config.getLocal());
    }

}
