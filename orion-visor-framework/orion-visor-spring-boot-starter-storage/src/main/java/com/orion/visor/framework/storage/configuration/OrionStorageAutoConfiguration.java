package com.orion.visor.framework.storage.configuration;

import com.orion.visor.framework.common.constant.AutoConfigureOrderConst;
import com.orion.visor.framework.common.file.FileClient;
import com.orion.visor.framework.storage.configuration.config.LocalStorageConfig;
import com.orion.visor.framework.storage.configuration.config.LogsStorageConfig;
import com.orion.visor.framework.storage.core.client.PrimaryFileClient;
import com.orion.visor.framework.storage.core.client.local.LocalFileClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

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
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_STORAGE)
@EnableConfigurationProperties({LocalStorageConfig.class, LogsStorageConfig.class})
public class OrionStorageAutoConfiguration {

    /**
     * @return 默认文件客户端
     */
    @Bean(name = "primaryFileClient")
    @Primary
    public FileClient primaryFileClient() {
        return new PrimaryFileClient();
    }

    /**
     * @return 本地文件客户端
     */
    @Bean
    @ConditionalOnProperty(value = "orion.storage.local.enabled", havingValue = "true")
    public FileClient localFileClient(LocalStorageConfig config) {
        return new LocalFileClient(config);
    }

    /**
     * @return 日志文件客户端
     */
    @Bean
    @ConditionalOnProperty(value = "orion.storage.logs.enabled", havingValue = "true")
    public FileClient logsFileClient(LogsStorageConfig config) {
        return new LocalFileClient(config);
    }

}
