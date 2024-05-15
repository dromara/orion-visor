package com.orion.visor.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 移动位置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/13 17:26
 */
@Getter
@AllArgsConstructor
public enum MovePosition {

    // 拖拽到目标元素上
    TOP(-1),

    // 拖拽到目标元素中
    IN(0),

    // 拖拽到目标元素下
    BOTTOM(1),

    ;

    private final Integer position;

    public static MovePosition of(Integer position) {
        if (position == null) {
            return null;
        }
        for (MovePosition value : values()) {
            if (value.position.equals(position)) {
                return value;
            }
        }
        return null;
    }

}
