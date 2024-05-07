package com.orion.ops.module.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主机系统类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/16 21:58
 */
@Getter
@AllArgsConstructor
public enum HostSshOsTypeEnum {

    /**
     * linux
     */
    LINUX(".sh"),

    /**
     * windows
     */
    WINDOWS(".cmd"),

    ;

    private final String scriptSuffix;

    public static HostSshOsTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostSshOsTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
