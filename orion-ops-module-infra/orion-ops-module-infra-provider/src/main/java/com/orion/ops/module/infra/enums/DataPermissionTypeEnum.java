package com.orion.ops.module.infra.enums;

/**
 * 数据权限类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/8 18:57
 */
public enum DataPermissionTypeEnum {

    /**
     * 主机分组
     */
    HOST_GROUP,

    ;

    public static DataPermissionTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (DataPermissionTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
