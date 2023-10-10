package com.orion.ops.framework.biz.operator.log.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 操作日志配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 14:08
 */
@Data
@ConfigurationProperties("orion.operator-log")
public class OperatorLogConfig {

    /**
     * 错误信息长度
     */
    private Integer errorMessageLength;

    public OperatorLogConfig() {
        this.errorMessageLength = 255;
    }
}
