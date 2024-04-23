package com.orion.ops.module.infra.enums;

/**
 * 数据分组类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/8 18:57
 */
public enum DataGroupTypeEnum {

    /**
     * 主机
     */
    HOST,

    /**
     * 命令片段
     */
    COMMAND_SNIPPET,

    /**
     * 路径书签
     */
    PATH_BOOKMARK,

    ;

    public static DataGroupTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (DataGroupTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
