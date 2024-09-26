package com.orion.visor.module.asset.enums;

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
            return LINUX;
        }
        for (HostSshOsTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return LINUX;
    }

    /**
     * 是否为 linux 系统
     *
     * @param type type
     * @return isLinux
     */
    public static boolean isLinux(String type) {
        return LINUX.name().equals(type);
    }

    /**
     * 是否为 windows 系统
     *
     * @param type type
     * @return isWindows
     */
    public static boolean isWindows(String type) {
        return WINDOWS.name().equals(type);
    }

}
