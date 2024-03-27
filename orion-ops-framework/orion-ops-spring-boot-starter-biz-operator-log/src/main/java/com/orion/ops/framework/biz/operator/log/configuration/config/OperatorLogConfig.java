package com.orion.ops.framework.biz.operator.log.configuration.config;

import com.orion.ops.framework.common.utils.ConfigUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

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

    /**
     * userAgent 长度
     */
    private Integer userAgentLength;

    /**
     * 忽略记录的字段
     */
    private List<String> ignore;

    /**
     * 需要脱敏的字段
     */
    private List<String> desensitize;

    public OperatorLogConfig() {
        this.errorMessageLength = 255;
        this.userAgentLength = 128;
    }

    public void setIgnore(List<String> ignore) {
        this.ignore = ConfigUtils.parseStringList(ignore);
    }

    public void setDesensitize(List<String> desensitize) {
        this.desensitize = ConfigUtils.parseStringList(desensitize);
    }

}
