package com.orion.ops.module.asset.enums;

/**
 * 主机验证类型 - ssh
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/21 19:01
 */
public enum HostConfigSshAuthTypeEnum {

    /**
     * 密码验证
     */
    PASSWORD,

    /**
     * 秘钥验证
     */
    KEY,

    /**
     * 身份验证
     */
    IDENTITY,

    ;

    public static HostConfigSshAuthTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostConfigSshAuthTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
