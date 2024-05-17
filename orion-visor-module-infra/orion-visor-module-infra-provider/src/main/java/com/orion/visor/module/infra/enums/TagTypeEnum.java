package com.orion.visor.module.infra.enums;

/**
 * 收藏类型 枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 10:53
 */
public enum TagTypeEnum {

    /**
     * 主机
     */
    HOST,

    ;

    public static TagTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (TagTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
