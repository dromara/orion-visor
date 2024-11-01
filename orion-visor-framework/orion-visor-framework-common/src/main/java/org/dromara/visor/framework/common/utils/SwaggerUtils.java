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
package org.dromara.visor.framework.common.utils;

import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.reflect.Annotations;
import io.swagger.v3.oas.annotations.Operation;
import org.dromara.visor.framework.common.constant.Const;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * swagger 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 14:52
 */
public class SwaggerUtils {

    /**
     * api 描述
     */
    private static final Map<String, String> SUMMARY_MAPPING = Maps.newMap();

    private SwaggerUtils() {
    }

    /**
     * 获取 api 描述
     *
     * @param m method
     * @return summary
     */
    public static String getOperationSummary(Method m) {
        // 缓存中获取描述
        String key = m.toString();
        String cache = SUMMARY_MAPPING.get(key);
        if (cache != null) {
            return cache;
        }
        // 获取注解描述
        Operation operation = Annotations.getAnnotation(m, Operation.class);
        String summary = Const.EMPTY;
        if (operation != null) {
            summary = operation.summary();
        }
        SUMMARY_MAPPING.put(key, summary);
        return summary;
    }

}
