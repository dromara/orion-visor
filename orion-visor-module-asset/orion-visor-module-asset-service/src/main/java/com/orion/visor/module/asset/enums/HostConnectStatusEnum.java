package com.orion.visor.module.asset.enums;

import com.orion.lang.utils.collect.Lists;

import java.util.List;

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

    public static final List<String> FINISH_STATUS_LIST = Lists.of(COMPLETE.name(), FAILED.name(), FORCE_OFFLINE.name());

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
