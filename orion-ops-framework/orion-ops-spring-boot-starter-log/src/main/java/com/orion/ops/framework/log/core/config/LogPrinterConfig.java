package com.orion.ops.framework.log.core.config;

import com.orion.ops.framework.common.utils.ConfigUtils;
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

    public void setField(LogPrinterFieldConfig field) {
        this.field = field;
    }

    public void setHeaders(List<String> headers) {
        this.headers = ConfigUtils.parseStringList(headers, String::toLowerCase);
    }

}
