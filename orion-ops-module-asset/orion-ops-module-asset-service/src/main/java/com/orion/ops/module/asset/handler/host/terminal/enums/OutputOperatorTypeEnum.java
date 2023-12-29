package com.orion.ops.module.asset.handler.host.terminal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 输出操作类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:33
 */
@Getter
@AllArgsConstructor
public enum OutputOperatorTypeEnum {

    /**
     * 连接主机成功
     */
    CONNECT_COMPLETE("cc"),

    /**
     * 连接主机失败
     */
    CONNECT_FAILED("cf"),

    /**
     * pong
     */
    PONG("p"),

    /**
     * 输出
     */
    OUTPUT("o"),

    /**
     * 发生错误
     */
    ERROR("e"),

    ;

    private final String type;

    public static OutputOperatorTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (OutputOperatorTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
