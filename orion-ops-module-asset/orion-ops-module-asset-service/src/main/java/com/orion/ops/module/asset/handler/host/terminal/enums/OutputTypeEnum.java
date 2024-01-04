package com.orion.ops.module.asset.handler.host.terminal.enums;

import com.orion.lang.utils.json.matcher.ReplacementFormatters;
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
public enum OutputTypeEnum {

    /**
     * 主机连接检查
     */
    CHECK("ck", "${type}|${session}|${result}|${errorMessage}"),

    /**
     * 主机连接
     */
    CONNECT("co", "${type}|${session}|${result}|${errorMessage}"),

    /**
     * pong
     */
    PONG("p", "${type}"),

    /**
     * 输出
     */
    OUTPUT("o", "${type}|${session}|${body}"),

    ;

    private final String type;

    private final String template;

    /**
     * 格式化
     *
     * @param o o
     * @return 格式化
     */
    public String format(Object o) {
        return ReplacementFormatters.format(this.template, o);
    }

    public static OutputTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (OutputTypeEnum value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
