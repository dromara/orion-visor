package com.orion.ops.module.asset.enums;

/**
 * 主机认证类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 21:41
 */
public enum HostExtraSshAuthTypeEnum {

    /**
     * 默认验证方式
     */
    DEFAULT,

    /**
     * 秘钥验证
     */
    KEY,

    /**
     * 身份验证
     */
    IDENTITY,

    ;

    public static HostExtraSshAuthTypeEnum of(String type) {
        if (type == null) {
            return DEFAULT;
        }
        for (HostExtraSshAuthTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return DEFAULT;
    }

}
