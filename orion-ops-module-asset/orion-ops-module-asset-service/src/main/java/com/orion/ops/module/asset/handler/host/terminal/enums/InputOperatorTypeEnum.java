package com.orion.ops.module.asset.handler.host.terminal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 输入操作类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:33
 */
@Getter
@AllArgsConstructor
public enum InputOperatorTypeEnum {

    /**
     * 连接主机
     */
    CONNECT("co"),

    /**
     * 关闭连接
     */
    CLOSE("cl"),

    /**
     * ping
     */
    PING("p"),

    /**
     * 修改大小
     */
    RESIZE("rs"),

    /**
     * 执行
     */
    EXEC("e"),

    /**
     * 输入
     */
    INPUT("i"),

    ;

    private final String type;

    public static InputOperatorTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (InputOperatorTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
