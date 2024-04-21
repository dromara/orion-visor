package com.orion.ops.module.asset.define.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用 tracker 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 22:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.tracker")
public class AppTrackerConfig {

    /**
     * 加载偏移量 (行)
     */
    private Integer offset;

    /**
     * 延迟时间 (ms)
     */
    private Integer delay;

    /**
     * 文件未找到等待次数
     */
    private Integer waitTimes;

    public AppTrackerConfig() {
        this.offset = 300;
        this.delay = 100;
        this.waitTimes = 100;
    }
}
