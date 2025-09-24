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
package org.dromara.visor.framework.biz.push.core.utils;

import cn.orionsec.kit.lang.utils.json.matcher.NoMatchStrategy;
import cn.orionsec.kit.lang.utils.json.matcher.ReplacementFormatter;
import cn.orionsec.kit.lang.utils.json.matcher.ReplacementFormatters;

import java.util.Map;

/**
 * 消息工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/16 17:28
 */
public class MessageUtils {

    private static final ReplacementFormatter FORMATTER = ReplacementFormatters.create("@{{ ", " }}")
            .noMatchStrategy(NoMatchStrategy.KEEP);

    private MessageUtils() {
    }

    /**
     * 替换模板
     *
     * @param template template
     * @param params   params
     * @return template
     */
    public static String format(String template, Map<String, Object> params) {
        if (params == null) {
            return template;
        }
        return FORMATTER.format(template, params);
    }

    /**
     * 替换模板
     *
     * @param template template
     * @param json     json
     * @return template
     */
    public static String format(String template, String json) {
        return FORMATTER.format(template, json);
    }

}
