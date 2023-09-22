package com.orion.ops.module.asset.enums;

/**
 * 主机验证类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/21 19:01
 */
public enum HostAuthTypeEnum {

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

    public static HostAuthTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostAuthTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
