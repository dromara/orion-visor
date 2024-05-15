package com.orion.visor.module.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 脚本执行枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/17 10:17
 */
@Getter
@AllArgsConstructor
public enum ScriptExecEnum {

    /**
     * 不使用
     */
    DISABLED(0),

    /**
     * 使用
     */
    ENABLED(1),

    ;

    private final Integer value;

    public static ScriptExecEnum of(Integer value) {
        if (value == null) {
            return null;
        }
        for (ScriptExecEnum val : values()) {
            if (val.value.equals(value)) {
                return val;
            }
        }
        return null;

    }

    /**
     * 检查是否启用
     *
     * @param value value
     * @return 是否启用
     */
    public static boolean isEnabled(Integer value) {
        return ENABLED.value.equals(value);
    }

}
