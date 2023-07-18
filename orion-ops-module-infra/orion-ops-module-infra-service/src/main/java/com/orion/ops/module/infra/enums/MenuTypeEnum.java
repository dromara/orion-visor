package com.orion.ops.module.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/18 10:01
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

    /**
     * 父菜单
     */
    PARENT_MENU(1),

    /**
     * 子菜单
     */
    SUB_MENU(2),

    /**
     * 功能
     */
    FUNCTION(3),

    ;

    private final Integer type;

    public static MenuTypeEnum of(Integer type) {
        if (type == null) {
            return null;
        }
        for (MenuTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
