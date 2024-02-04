package com.orion.ops.module.asset.enums;

/**
 * 主机连接类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 22:27
 */
public enum HostConnectTypeEnum {

    /**
     * ssh
     */
    SSH,

    /**
     * sftp
     */
    SFTP,

    ;

    public static HostConnectTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostConnectTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
