package com.orion.visor.module.infra.enums;

/**
 * 历史值类型 枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/16 17:40
 */
public enum HistoryValueTypeEnum {

    /**
     * 字典
     */
    DICT,

    ;

    public static HistoryValueTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HistoryValueTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
