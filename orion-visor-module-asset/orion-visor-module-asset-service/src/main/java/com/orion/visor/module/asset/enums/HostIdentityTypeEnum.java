package com.orion.visor.module.asset.enums;

/**
 * 主机身份类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/21 19:01
 */
public enum HostIdentityTypeEnum {

    /**
     * 密码
     */
    PASSWORD,

    /**
     * 密钥
     */
    KEY,

    ;

    public static HostIdentityTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostIdentityTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
