package com.orion.ops.module.asset.enums;

/**
 * 主机配置类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/11 14:37
 */
public enum HostConfigTypeEnum {

    /**
     * ssh 配置
     */
    SSH,

    /**
     * sftp 配置
     */
    SFTP,

    /**
     * rdp 配置
     */
    RDP,

    /**
     * 环境变量
     */
    ENV,

    /**
     * 普通配置
     */
    CONFIG,

}
