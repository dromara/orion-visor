package com.orion.visor.module.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色状态枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:45
 */
@Getter
@AllArgsConstructor
public enum RoleStatusEnum {

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

    public static RoleStatusEnum of(Integer status) {
        if (status == null) {
            return null;
        }
        for (RoleStatusEnum value : values()) {
            if (value.status.equals(status)) {
                return value;
            }
        }
        return null;
    }

}
