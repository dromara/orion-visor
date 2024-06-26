package com.orion.visor.module.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/8 18:57
 */
@Getter
@AllArgsConstructor
public enum DataPermissionTypeEnum {

    /**
     * 主机分组
     */
    HOST_GROUP(true, "主机分组"),

    /**
     * 主机密钥
     */
    HOST_KEY(true, "主机密钥"),

    /**
     * 主机身份
     */
    HOST_IDENTITY(true, "主机身份"),

    ;

    /**
     * 是否会分配给角色
     */
    private final boolean toRole;

    /**
     * 权限名称
     */
    private final String permissionName;

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
