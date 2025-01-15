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
package org.dromara.visor.framework.config.core.ref;

import cn.orionsec.kit.lang.utils.Objects1;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.config.ConfigRef;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 配置引用实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/14 16:10
 */
@Slf4j
public class ConfigRefImpl<T> extends ConfigRef<T> {

    protected BiConsumer<T, T> changeEvent;

    public ConfigRefImpl(String key, Function<String, T> convert) {
        super(key, convert);
    }

    @Override
    public void override(String value) {
        try {
            this.set(convert.apply(value));
        } catch (Exception e) {
            log.error("ConfigRef trigger override error key: {}, value: {}", key, value, e);
        }
    }

    @Override
    public void set(T value) {
        T before = this.value;
        this.value = value;
        // 被修改
        if (!Objects1.eq(before, value)) {
            log.info("ConfigRef changed key: {}, value: {}", key, value);
            // 触发事件
            if (changeEvent != null) {
                changeEvent.accept(value, before);
            }
        }
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public ConfigRef<T> onChange(BiConsumer<T, T> changeEvent) {
        this.changeEvent = changeEvent;
        return this;
    }

}
