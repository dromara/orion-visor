package com.orion.ops.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * boolean 枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/19 10:32
 */
@Getter
@AllArgsConstructor
public enum BooleanBit {

    /**
     * 假
     */
    FALSE(0),

    /**
     * 真
     */
    TRUE(1),

    ;

    private final Integer v;

    public static BooleanBit of(Integer value) {
        if (value == null) {
            return null;
        }
        for (BooleanBit e : values()) {
            if (e.v.equals(value)) {
                return e;
            }
        }
        return null;
    }

}
