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
package org.dromara.visor.module.exec.utils;

import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.json.matcher.NoMatchStrategy;
import cn.orionsec.kit.lang.utils.json.matcher.ReplacementFormatter;
import cn.orionsec.kit.lang.utils.json.matcher.ReplacementFormatters;
import com.alibaba.fastjson.JSON;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.exec.entity.dto.ExecParameterSchemaDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 执行工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/16 17:28
 */
public class ExecUtils {

    private static final ReplacementFormatter FORMATTER = ReplacementFormatters.create("@{{ ", " }}")
            .noMatchStrategy(NoMatchStrategy.EMPTY);

    private ExecUtils() {
    }

    /**
     * 替换命令
     *
     * @param command command
     * @param params  params
     * @return command
     */
    public static String format(String command, Map<String, Object> params) {
        return Strings.replaceCRLF(FORMATTER.format(command, params));
    }

    /**
     * 替换命令
     *
     * @param command command
     * @param json    json
     * @return command
     */
    public static String format(String command, String json) {
        return Strings.replaceCRLF(FORMATTER.format(command, json));
    }

    /**
     * 提取参数
     *
     * @param parameterSchema parameterSchema
     * @return params
     */
    public static Map<String, Object> extraSchemaParams(String parameterSchema) {
        List<ExecParameterSchemaDTO> schemaList = JSON.parseArray(parameterSchema, ExecParameterSchemaDTO.class);
        if (Lists.isEmpty(schemaList)) {
            return Maps.newMap();
        }
        // 解析参数
        return schemaList.stream()
                .collect(Collectors.toMap(ExecParameterSchemaDTO::getName,
                        s -> {
                            Object value = s.getValue();
                            if (value == null) {
                                value = Const.EMPTY;
                            }
                            return value;
                        },
                        Functions.right()));
    }

}
