package com.orion.ops.module.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单状态枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:45
 */
@Getter
@AllArgsConstructor
public enum MenuStatusEnum {

    /**
     * 停用
     */
    DISABLED(0),

    /**
     * 启用
     */
    ENABLED(1),

    ;

    private final Integer status;

    public static MenuStatusEnum of(Integer status) {
        if (status == null) {
            return null;
        }
        for (MenuStatusEnum value : values()) {
            if (value.status.equals(status)) {
                return value;
            }
        }
        return null;
    }

}
