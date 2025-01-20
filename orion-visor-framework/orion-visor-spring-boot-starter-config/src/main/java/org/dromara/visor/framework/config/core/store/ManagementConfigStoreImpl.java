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
package org.dromara.visor.framework.config.core.store;

import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.config.ConfigRef;
import org.dromara.visor.framework.config.core.ref.ConfigRefImpl;
import org.dromara.visor.framework.config.core.service.ConfigFrameworkService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 配置中心实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 17:20
 */
@Slf4j
public class ManagementConfigStoreImpl implements ManagementConfigStore {

    private final ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, List<ConfigRef<?>>> configRefs = new ConcurrentHashMap<>();

    private final ConfigFrameworkService service;

    public ManagementConfigStoreImpl(ConfigFrameworkService service) {
        this.service = service;
    }

    @Override
    public void loadAllConfig() {
        configMap.putAll(service.getAllConfig());
    }

    @Override
    public void override(String key, String value) {
        log.info("ConfigStore.override key: {}, value: {}", key, value);
        // 修改配置
        configMap.put(key, value);
        // 修改引用
        List<ConfigRef<?>> refs = configRefs.get(key);
        if (!Lists.isEmpty(refs)) {
            refs.forEach(s -> s.override(value));
        }
    }

    @Override
    public void register(ConfigRef<?> ref) {
        String key = ref.key;
        log.info("ConfigStore.register ref key: {}", key);
        // 注册引用
        configRefs.computeIfAbsent(key, k -> new ArrayList<>()).add(ref);
    }

    @Override
    public String getString(String key) {
        return this.getConfig(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return this.getConfig(key, defaultValue);
    }

    @Override
    public Integer getInteger(String key) {
        return this.getConfig(key, Integer::valueOf, null);
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return this.getConfig(key, Integer::valueOf, defaultValue);
    }

    @Override
    public Long getLong(String key) {
        return this.getConfig(key, Long::valueOf, null);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return this.getConfig(key, Long::valueOf, defaultValue);
    }

    @Override
    public Double getDouble(String key) {
        return this.getConfig(key, Double::valueOf, null);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return this.getConfig(key, Double::valueOf, defaultValue);
    }

    @Override
    public Boolean getBoolean(String key) {
        return this.getConfig(key, Boolean::valueOf, null);
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return this.getConfig(key, Boolean::valueOf, defaultValue);
    }

    @Override
    public String getConfig(String key) {
        return this.getConfig(key, Function.identity(), null);
    }

    @Override
    public String getConfig(String key, String defaultValue) {
        return this.getConfig(key, Function.identity(), defaultValue);
    }

    @Override
    public <T> T getConfig(String key, Function<String, T> convert) {
        return this.getConfig(key, convert, null);
    }

    @Override
    public <T> T getConfig(String key, Function<String, T> convert, T defaultValue) {
        // 获取配置
        String conf = configMap.get(key);
        // 默认值
        if (conf == null) {
            return defaultValue;
        }
        // 转换
        return convert.apply(conf);
    }

    @Override
    public ConfigRef<String> string(String key) {
        return this.ref(key, Function.identity(), null);
    }

    @Override
    public ConfigRef<String> string(String key, String defaultValue) {
        return this.ref(key, Function.identity(), defaultValue);
    }

    @Override
    public ConfigRef<Integer> int32(String key) {
        return this.ref(key, Integer::valueOf, null);
    }

    @Override
    public ConfigRef<Integer> int32(String key, Integer defaultValue) {
        return this.ref(key, Integer::valueOf, defaultValue);
    }

    @Override
    public ConfigRef<Long> int64(String key) {
        return this.ref(key, Long::valueOf, null);
    }

    @Override
    public ConfigRef<Long> int64(String key, Long defaultValue) {
        return this.ref(key, Long::valueOf, defaultValue);
    }

    @Override
    public ConfigRef<Double> float64(String key) {
        return this.ref(key, Double::valueOf, null);
    }

    @Override
    public ConfigRef<Double> float64(String key, Double defaultValue) {
        return this.ref(key, Double::valueOf, defaultValue);
    }

    @Override
    public ConfigRef<Boolean> bool(String key) {
        return this.ref(key, Boolean::valueOf, null);
    }

    @Override
    public ConfigRef<Boolean> bool(String key, Boolean defaultValue) {
        return this.ref(key, Boolean::valueOf, defaultValue);
    }

    @Override
    public <T> ConfigRef<T> ref(String key, Function<String, T> convert) {
        return this.ref(key, convert, null);
    }

    @Override
    public <T> ConfigRef<T> ref(String key, Function<String, T> convert, T defaultValue) {
        // 创建引用
        ConfigRef<T> ref = new ConfigRefImpl<>(key, convert);
        // 设置值
        String value = configMap.get(key);
        if (value != null) {
            ref.override(value);
        } else {
            ref.set(defaultValue);
        }
        // 注册引用
        this.register(ref);
        return ref;
    }

}
