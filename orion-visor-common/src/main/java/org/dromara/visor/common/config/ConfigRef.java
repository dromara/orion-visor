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

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 配置引用
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 18:01
 */
@Slf4j
public abstract class ConfigRef<T> {

    public final String key;

    public T value;

    protected final Function<String, T> convert;

    public ConfigRef(String key, Function<String, T> convert) {
        this.key = key;
        this.convert = convert;
    }

    /**
     * 覆盖配置
     *
     * @param value value
     */
    public abstract void override(String value);

    /**
     * 修改配置
     *
     * @param value value
     */
    public abstract void set(T value);

    /**
     * 获取配置
     *
     * @return value
     */
    public abstract T get();

    /**
     * 修改回调
     *
     * @param changeEvent changeEvent
     * @return this
     */
    public abstract ConfigRef<T> onChange(BiConsumer<T, T> changeEvent);

}
