package com.orion.visor.module.asset.enums;

/**
 * 主机认证类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 21:41
 */
public enum HostExtraSshAuthTypeEnum {

    /**
     * 默认认证方式
     */
    DEFAULT,

    /**
     * 自定义密钥认证
     */
    CUSTOM_KEY,

    /**
     * 自定义身份认证
     */
    CUSTOM_IDENTITY,

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
