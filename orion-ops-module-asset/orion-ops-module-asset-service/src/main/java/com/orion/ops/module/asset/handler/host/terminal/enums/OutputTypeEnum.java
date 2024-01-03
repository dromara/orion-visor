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
    CHECK("ck", "${type}|${session}|${token}|${result}|${errorMessage}"),

    /**
     * 主机连接
     */
    CONNECT("co", "${type}|${session}|${result}|${errorMessage}"),

    /**
     * pong
     */
    PONG("p", "${type}|${session}"),

    /**
     * 输出
     */
    OUTPUT("o", "${type}|${session}|${body}"),

    /**
     * 发生错误
     */
    ERROR("e", "${type}|${session}"),

    ;

    private static final char SEPARATOR = '|';

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
