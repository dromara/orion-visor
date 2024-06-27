package com.orion.visor.module.asset.define.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用执行日志配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/25 13:36
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.exec-log")
public class AppExecLogConfig {

    /**
     * 是否拼接 ansi 执行状态日志
     */
    private Boolean appendAnsi;

    public AppExecLogConfig() {
        this.appendAnsi = true;
    }

}
