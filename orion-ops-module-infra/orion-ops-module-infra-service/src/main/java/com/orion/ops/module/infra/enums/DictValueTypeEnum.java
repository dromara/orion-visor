package com.orion.ops.module.infra.enums;

import com.orion.lang.utils.convert.Converts;

import java.math.BigDecimal;

/**
 * 字典值类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/17 11:18
 */
public enum DictValueTypeEnum {

    /**
     * 字符串
     */
    STRING,

    /**
     * 整数
     */
    INTEGER {
        @Override
        public Object parse(String s) {
            try {
                return Integer.valueOf(s);
            } catch (Exception e) {
                return super.parse(s);
            }
        }
    },

    /**
     * 小数
     */
    DECIMAL {
        @Override
        public Object parse(String s) {
            try {
                return BigDecimal.valueOf(Double.valueOf(s));
            } catch (Exception e) {
                return super.parse(s);
            }
        }
    },

    /**
     * 布尔值
     */
    BOOLEAN {
        @Override
        public Object parse(String s) {
            try {
                return Converts.toBoolean(s);
            } catch (Exception e) {
                return super.parse(s);
            }
        }
    },

    /**
     * 颜色
     */
    COLOR,

    ;

    /**
     * 转换
     *
     * @param s s
     * @return value
     */
    public Object parse(String s) {
        return s;
    }

    public static DictValueTypeEnum of(String type) {
        if (type == null) {
            return STRING;
        }
        for (DictValueTypeEnum value : values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return STRING;
    }

}
