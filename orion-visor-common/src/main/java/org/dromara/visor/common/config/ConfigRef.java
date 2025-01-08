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
package org.dromara.visor.common.config;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * 配置引用
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 18:01
 */
@Slf4j
public class ConfigRef<T> {

    public final String key;

    private final Function<String, T> convert;

    public T value;

    public ConfigRef(String key, Function<String, T> convert) {
        this.key = key;
        this.convert = convert;
    }

    /**
     * 覆盖配置
     *
     * @param value value
     */
    public void override(String value) {
        try {
            this.value = convert.apply(value);
        } catch (Exception e) {
            log.error("ConfigRef trigger override error key: {}, value: {}", key, value, e);
        }
    }

    /**
     * 设置值
     *
     * @param value value
     */
    public void set(T value) {
        this.value = value;
    }

}
