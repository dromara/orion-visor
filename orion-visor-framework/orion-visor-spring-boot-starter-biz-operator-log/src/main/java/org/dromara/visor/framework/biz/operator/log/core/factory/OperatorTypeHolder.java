/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.framework.biz.operator.log.core.factory;

import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志类型实例
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 14:43
 */
public class OperatorTypeHolder {

    private static final Map<String, OperatorType> TYPES = new HashMap<>();

    private OperatorTypeHolder() {
    }

    /**
     * 获取类型
     *
     * @param key key
     * @return type
     */
    public static OperatorType get(String key) {
        return TYPES.get(key);
    }

    /**
     * 设置类型
     *
     * @param type type
     */
    public static void set(OperatorType type) {
        TYPES.put(type.getType(), type);
    }

}
