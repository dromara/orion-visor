package com.orion.ops.framework.web.core.config;

import com.orion.ops.framework.web.core.utils.Utils;
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
    private List<String> desensitization;

    public void setIgnore(List<String> ignore) {
        this.ignore = Utils.parseStringList(ignore);
    }

    public void setDesensitization(List<String> desensitization) {
        this.desensitization = Utils.parseStringList(desensitization);
    }

}
