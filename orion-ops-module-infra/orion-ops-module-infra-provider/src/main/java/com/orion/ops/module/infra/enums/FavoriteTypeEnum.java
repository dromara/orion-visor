package com.orion.ops.module.infra.enums;

/**
 * 收藏类型 枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/25 10:53
 */
public enum FavoriteTypeEnum {

    /**
     * 主机
     */
    HOST,

    ;

    public static FavoriteTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (FavoriteTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
