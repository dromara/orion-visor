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
     * 自定义秘钥验证
     */
    CUSTOM_KEY,

    /**
     * 自定义身份验证
     */
    CUSTOM_IDENTITY,

    ;

    public static HostExtraSshAuthTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (HostExtraSshAuthTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
