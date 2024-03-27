package com.orion.ops.framework.log.configuration.config;

import com.orion.ops.framework.common.utils.ConfigUtils;
import lombok.Data;

import java.util.List;

/**
 * 日志打印字段配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/28 22:36
 */
@Data
public class LogPrinterFieldConfig {

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
