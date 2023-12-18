package com.orion.ops.module.infra.enums;

/**
 * 数据别名类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/18 21:51
 */
public enum DataAliasTypeEnum {

    /**
     * 主机
     */
    HOST,

    ;

    public static DataAliasTypeEnum of(String type) {
        if (type == null) {
            return null;
        }
        for (DataAliasTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
