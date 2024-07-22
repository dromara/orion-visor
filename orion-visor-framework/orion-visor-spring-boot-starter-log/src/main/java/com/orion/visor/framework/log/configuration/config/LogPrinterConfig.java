package com.orion.visor.framework.log.configuration.config;

import com.orion.visor.framework.common.utils.ConfigUtils;
import com.orion.visor.framework.log.core.enums.LogPrinterMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 日志打印配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/28 22:36
 */
@Data
@ConfigurationProperties("orion.logging.printer")
public class LogPrinterConfig {

    /**
     * 类型
     */
    private LogPrinterMode mode;

    /**
     * 字段配置
     */
    private LogPrinterFieldConfig field;

    /**
     * 显示的请求头
     */
    private List<String> headers;

    /**
     * 切面表达式
     */
    private String expression;

    public void setHeaders(List<String> headers) {
        this.headers = ConfigUtils.parseStringList(headers, String::toLowerCase);
    }

    @Data
    public static class LogPrinterFieldConfig {

        /**
         * 忽略的字段
         */
        private List<String> ignore;

        /**
         * 脱敏的字段
         */
        private List<String> desensitize;

        public void setIgnore(List<String> ignore) {
            this.ignore = ConfigUtils.parseStringList(ignore);
        }

        public void setDesensitize(List<String> desensitize) {
            this.desensitize = ConfigUtils.parseStringList(desensitize);
        }

    }

}
