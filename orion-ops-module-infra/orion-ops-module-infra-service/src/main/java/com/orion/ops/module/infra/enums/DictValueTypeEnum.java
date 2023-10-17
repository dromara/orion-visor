package com.orion.ops.module.infra.enums;

import lombok.Getter;

/**
 * 字典值类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/17 11:18
 */
@Getter
public enum DictValueTypeEnum {

    /**
     * 字符串
     */
    STRING,

    /**
     * 数值
     */
    NUMBER,

    /**
     * 布尔值
     */
    BOOLEAN,

    /**
     * 颜色
     */
    COLOR,

    ;

    public static DictValueTypeEnum of(String type) {
        if (type == null) {
            return STRING;
        }
        for (DictValueTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return STRING;
    }

}
