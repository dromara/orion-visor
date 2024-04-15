package com.orion.ops.framework.common.configuration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池配置类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/10 15:49
 */
@Data
@ConfigurationProperties(prefix = "orion.async.executor")
public class AsyncExecutorConfig {

    /**
     * 核心线程数量
     */
    private int corePoolSize;

    /**
     * 最大线程数量
     */
    private int maxPoolSize;

    /**
     * 队列容量
     */
    private int queueCapacity;

    /**
     * 活跃时间
     */
    private int keepAliveSeconds;

    public AsyncExecutorConfig() {
        this.corePoolSize = 8;
        this.maxPoolSize = 16;
        this.queueCapacity = 200;
        this.keepAliveSeconds = 300;
    }

}
