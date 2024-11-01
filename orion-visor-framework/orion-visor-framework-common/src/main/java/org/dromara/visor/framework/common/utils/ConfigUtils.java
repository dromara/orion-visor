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

import cn.orionsec.kit.lang.constant.Const;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 配置工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/28 23:21
 */
public class ConfigUtils {

    private ConfigUtils() {
    }

    public static List<String> parseStringList(List<String> list) {
        return parseStringList(list, Function.identity());
    }

    /**
     * 解析配置 List<String:String[,]>
     *
     * @param list   list
     * @param mapper mapper
     * @return config
     */
    public static List<String> parseStringList(List<String> list, Function<String, String> mapper) {
        return Optional.ofNullable(list)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .map(s -> s.split(Const.COMMA))
                .flatMap(Arrays::stream)
                .map(String::trim)
                .map(mapper)
                .collect(Collectors.toList());
    }

}
