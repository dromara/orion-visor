package com.orion.visor.module.asset.enums;

/**
 * 主机连接状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 22:27
 */
public enum HostConnectStatusEnum {

    /**
     * 连接中
     */
    CONNECTING,

    /**
     * 完成
     */
    COMPLETE,

    /**
     * 失败
     */
    FAILED,

    /**
     * 强制下线
     */
    FORCE_OFFLINE,

    ;

    public static HostConnectStatusEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostConnectStatusEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
