package com.orion.visor.module.asset.enums;

/**
 * 主机状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/17 16:07
 */
public enum HostStatusEnum {

    /**
     * 停用
     */
    DISABLED,

    /**
     * 启用
     */
    ENABLED,

    ;

    public static HostStatusEnum of(String name) {
        if (name == null) {
            return null;
        }
        for (HostStatusEnum value : values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }

}
