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
package org.dromara.visor.common.mapstruct;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import org.dromara.visor.common.constant.Const;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * string 转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/7 17:35
 */
public class StringConversion {

    private StringConversion() {
    }

    /**
     * String > List<Integer>
     *
     * @param str str
     * @return list
     */
    public static List<Integer> stringToIntegerList(String str) {
        if (Strings.isBlank(str)) {
            return Lists.newList();
        }
        return Arrays.stream(str.split(Const.COMMA))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * String > List<String>
     *
     * @param str str
     * @return list
     */
    public static List<Long> stringToLongList(String str) {
        if (Strings.isBlank(str)) {
            return Lists.newList();
        }
        return Arrays.stream(str.split(Const.COMMA))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * String > List<String>
     *
     * @param str str
     * @return list
     */
    public static List<String> stringToStringList(String str) {
        if (Strings.isBlank(str)) {
            return Lists.newList();
        }
        return Lists.of(str.split(Const.COMMA));
    }

    /**
     * List<Integer> String
     *
     * @param list list
     * @return str
     */
    public static String integerListToString(List<Integer> list) {
        if (list != null) {
            return list.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(Const.COMMA));
        }
        return null;
    }

    /**
     * List<Long> String
     *
     * @param list list
     * @return str
     */
    public static String longListToString(List<Long> list) {
        if (list != null) {
            return list.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(Const.COMMA));
        }
        return null;
    }

    /**
     * List<String> String
     *
     * @param list list
     * @return str
     */
    public static String stringListToString(List<String> list) {
        return list != null ? String.join(Const.COMMA, list) : null;
    }

}
