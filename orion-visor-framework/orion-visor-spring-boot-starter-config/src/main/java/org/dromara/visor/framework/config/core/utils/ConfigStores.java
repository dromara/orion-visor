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
package org.dromara.visor.framework.config.core.utils;

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.common.config.ConfigRef;
import org.dromara.visor.common.config.ConfigStore;

import java.util.function.Function;

/**
 * 配置中心
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 17:20
 */
public class ConfigStores {

    private static ConfigStore delegate;

    private ConfigStores() {
    }

    /**
     * 获取 string 配置
     *
     * @param key key
     * @return config
     */
    public String getString(String key) {
        return delegate.getString(key);
    }

    /**
     * 获取 string 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return config
     */
    public String getString(String key, String defaultValue) {
        return delegate.getString(key, defaultValue);
    }

    /**
     * 获取 int 配置
     *
     * @param key key
     * @return config
     */
    public Integer getInteger(String key) {
        return delegate.getInteger(key);
    }

    /**
     * 获取 int 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return config
     */
    public Integer getInteger(String key, Integer defaultValue) {
        return delegate.getInteger(key, defaultValue);
    }

    /**
     * 获取 long 配置
     *
     * @param key key
     * @return config
     */
    public Long getLong(String key) {
        return delegate.getLong(key);
    }

    /**
     * 获取 long 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return config
     */
    public Long getLong(String key, Long defaultValue) {
        return delegate.getLong(key, defaultValue);
    }

    /**
     * 获取 double 配置
     *
     * @param key key
     * @return config
     */
    public Double getDouble(String key) {
        return delegate.getDouble(key);
    }

    /**
     * 获取 double 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return config
     */
    public Double getDouble(String key, Double defaultValue) {
        return delegate.getDouble(key, defaultValue);
    }

    /**
     * 获取 boolean 配置
     *
     * @param key key
     * @return config
     */
    public Boolean getBoolean(String key) {
        return delegate.getBoolean(key);
    }

    /**
     * 获取 boolean 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return config
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return delegate.getBoolean(key, defaultValue);
    }

    /**
     * 获取配置
     *
     * @param key key
     * @return conf
     */
    public static String getConfig(String key) {
        return delegate.getConfig(key);
    }

    /**
     * 获取配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return conf
     */
    public static String getConfig(String key, String defaultValue) {
        return delegate.getConfig(key, defaultValue);
    }

    /**
     * 获取配置
     *
     * @param key     key
     * @param convert convert
     * @param <T>     T
     * @return conf
     */
    public static <T> T getConfig(String key, Function<String, T> convert) {
        return delegate.getConfig(key, convert);
    }

    /**
     * 获取配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @param <T>          T
     * @return conf
     */
    public static <T> T getConfig(String key, Function<String, T> convert, T defaultValue) {
        return delegate.getConfig(key, convert, defaultValue);
    }

    /**
     * 获取 string 配置
     *
     * @param key key
     * @return ref
     */
    public static ConfigRef<String> string(String key) {
        return delegate.string(key);
    }

    /**
     * 获取 string 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    public static ConfigRef<String> string(String key, String defaultValue) {
        return delegate.string(key, defaultValue);
    }

    /**
     * 获取 int 配置
     *
     * @param key key
     * @return ref
     */
    public static ConfigRef<Integer> int32(String key) {
        return delegate.int32(key);
    }

    /**
     * 获取 int 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    public static ConfigRef<Integer> int32(String key, Integer defaultValue) {
        return delegate.int32(key, defaultValue);
    }

    /**
     * 获取 long 配置
     *
     * @param key key
     * @return ref
     */
    public static ConfigRef<Long> int64(String key) {
        return delegate.int64(key);
    }

    /**
     * 获取 long 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    public static ConfigRef<Long> int64(String key, Long defaultValue) {
        return delegate.int64(key, defaultValue);
    }

    /**
     * 获取 double 配置
     *
     * @param key key
     * @return ref
     */
    public static ConfigRef<Double> float64(String key) {
        return delegate.float64(key);
    }

    /**
     * 获取 double 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    public static ConfigRef<Double> float64(String key, Double defaultValue) {
        return delegate.float64(key, defaultValue);
    }

    /**
     * 获取 boolean 配置
     *
     * @param key key
     * @return ref
     */
    public static ConfigRef<Boolean> bool(String key) {
        return delegate.bool(key);
    }

    /**
     * 获取 boolean 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    public static ConfigRef<Boolean> bool(String key, Boolean defaultValue) {
        return delegate.bool(key, defaultValue);
    }

    /**
     * 获取配置
     *
     * @param key     key
     * @param convert convert
     * @param <T>     T
     * @return ref
     */
    public static <T> ConfigRef<T> ref(String key, Function<String, T> convert) {
        return delegate.ref(key, convert);
    }

    /**
     * 获取配置
     *
     * @param key          key
     * @param convert      convert
     * @param defaultValue defaultValue
     * @param <T>          T
     * @return ref
     */
    public static <T> ConfigRef<T> ref(String key, Function<String, T> convert, T defaultValue) {
        return delegate.ref(key, convert, defaultValue);
    }

    public static void setDelegate(ConfigStore configStore) {
        if (ConfigStores.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        ConfigStores.delegate = configStore;
    }

}
