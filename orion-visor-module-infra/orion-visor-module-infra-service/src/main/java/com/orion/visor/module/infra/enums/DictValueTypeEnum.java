/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.module.infra.enums;

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
                return Boolean.valueOf(s);
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
