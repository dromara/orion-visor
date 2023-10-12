package com.orion.ops.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 启用状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/12 16:02
 */
@Getter
@AllArgsConstructor
public enum EnableStatus {

    /**
     * 停用
     */
    DISABLED(0),

    /**
     * 启用
     */
    ENABLED(1),

    ;

    private final Integer value;

    public static EnableStatus of(Integer value) {
        if (value == null) {
            return null;
        }
        for (EnableStatus e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }

}
