package com.orion.visor.module.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单可见枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:45
 */
@Getter
@AllArgsConstructor
public enum MenuVisibleEnum {

    /**
     * 显示
     */
    HIDE(0),

    /**
     * 隐藏
     */
    SHOW(1),

    ;

    private final Integer visible;

    public static MenuVisibleEnum of(Integer visible) {
        if (visible == null) {
            return null;
        }
        for (MenuVisibleEnum value : values()) {
            if (value.visible.equals(visible)) {
                return value;
            }
        }
        return null;
    }

}
