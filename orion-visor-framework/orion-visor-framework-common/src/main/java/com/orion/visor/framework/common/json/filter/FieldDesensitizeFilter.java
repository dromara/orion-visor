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
package com.orion.visor.framework.common.json.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.orion.lang.utils.Desensitizes;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.collect.Lists;

import java.util.List;

/**
 * 字段脱敏过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/12 11:34
 */
public class FieldDesensitizeFilter implements ValueFilter {

    private final int keepStart;

    private final int keepEnd;

    private final List<String> desensitizeFields;

    public FieldDesensitizeFilter(List<String> desensitizeFields) {
        this(1, 1, desensitizeFields);
    }

    public FieldDesensitizeFilter(int keepStart, int keepEnd, List<String> desensitizeFields) {
        this.keepStart = keepStart;
        this.keepEnd = keepEnd;
        this.desensitizeFields = desensitizeFields;
    }

    @Override
    public Object process(Object object, String name, Object value) {
        if (Lists.isEmpty(desensitizeFields) || !desensitizeFields.contains(name)) {
            return value;
        }
        return Desensitizes.mix(Objects1.toString(value), keepStart, keepEnd);
    }

}
