package com.orion.ops.framework.log.core.config;

import com.orion.ops.framework.log.core.utils.Utils;
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
        this.ignore = Utils.parseStringList(ignore);
    }

    public void setDesensitize(List<String> desensitize) {
        this.desensitize = Utils.parseStringList(desensitize);
    }

}
