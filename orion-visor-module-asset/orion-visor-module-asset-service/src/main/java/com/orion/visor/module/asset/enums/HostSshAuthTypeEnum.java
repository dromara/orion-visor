package com.orion.visor.module.asset.enums;

/**
 * 主机认证类型 - ssh
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/21 19:01
 */
public enum HostSshAuthTypeEnum {

    /**
     * 密码认证
     */
    PASSWORD,

    /**
     * 密钥认证
     */
    KEY,

    /**
     * 身份认证
     */
    IDENTITY,

    ;

    public static HostSshAuthTypeEnum of(String type) {
        if (type == null) {
            return PASSWORD;
        }
        for (HostSshAuthTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return PASSWORD;
    }

}
