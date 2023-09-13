package com.orion.ops.module.asset.enums;

import com.orion.ops.module.asset.entity.dto.host.HostConfigContent;
import com.orion.ops.module.asset.entity.dto.host.HostSftpConfig;
import com.orion.ops.module.asset.entity.dto.host.HostSshConnectConfig;
import com.orion.ops.module.asset.entity.dto.host.HostSshEnvConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 主机配置类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/11 14:37
 */
@Getter
@AllArgsConstructor
public enum HostConfigTypeEnum {

    /**
     * SSH 连接配置
     */
    SSH_CONNECT(HostSshConnectConfig.class) {
    },

    /**
     * SSH 环境变量
     */
    SSH_ENV(HostSshEnvConfig.class),

    /**
     * sftp 配置
     */
    SFTP(HostSftpConfig.class),

    /**
     * rdp 配置
     */
    RDP(null),

    /**
     * 普通配置
     */
    CONFIG(null),

    ;

    private final Class<? extends HostConfigContent> type;

    public static HostConfigTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostConfigTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 插入填充
     *
     * @param o o
     */
    public <T extends HostConfigContent> void insertFill(T o) {
    }

    /**
     * 更新填充
     *
     * @param before before
     * @param after  after
     */
    public <T extends HostConfigContent> void updateFill(T before, T after) {
    }

}
