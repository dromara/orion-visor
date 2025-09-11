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
package org.dromara.visor.framework.redis.core.utils;

import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * redis string 工具类
 * <p>
 * 写操作会自动设置过期时间 如果有
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/20 14:05
 */
@SuppressWarnings("unchecked")
public class RedisStrings extends RedisUtils {

    private RedisStrings() {
    }

    /**
     * 获取值
     *
     * @param key key
     * @return value
     */
    public static String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 获取值
     *
     * @param define define
     * @return value
     */
    public static String get(CacheKeyDefine define) {
        return get(define.getKey());
    }

    /**
     * 获取值
     *
     * @param key    key
     * @param mapper mapper
     * @param <T>    T
     * @return value
     */
    public static <T> T get(String key, Function<String, T> mapper) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return mapper.apply(value);
    }

    /**
     * 获取 json
     *
     * @param key key
     * @return JSONObject
     */
    public static JSONObject getJson(String key) {
        return get(key, JSON::parseObject);
    }

    /**
     * 获取 json
     *
     * @param define define
     * @param <T>    T
     * @return T
     */
    public static <T> T getJson(CacheKeyDefine define) {
        return (T) getJson(define.getKey(), define.getType());
    }

    /**
     * 获取 json
     *
     * @param key    key
     * @param define define
     * @param <T>    T
     * @return T
     */
    public static <T> T getJson(String key, CacheKeyDefine define) {
        return (T) getJson(key, define.getType());
    }

    /**
     * 获取 json
     *
     * @param key  key
     * @param type type
     * @param <T>  T
     * @return T
     */
    public static <T> T getJson(String key, Class<T> type) {
        return get(key, s -> JSON.parseObject(s, type));
    }

    /**
     * 获取 json
     *
     * @param key key
     * @return JSONArray
     */
    public static JSONArray getJsonArray(String key) {
        return get(key, JSON::parseArray);
    }

    /**
     * 获取 json
     *
     * @param define define
     * @param <T>    T
     * @return T
     */
    public static <T> List<T> getJsonArray(CacheKeyDefine define) {
        return (List<T>) getJsonArray(define.getKey(), define.getType());
    }

    /**
     * 获取 json
     *
     * @param key    key
     * @param define define
     * @param <T>    T
     * @return T
     */
    public static <T> List<T> getJsonArray(String key, CacheKeyDefine define) {
        return (List<T>) getJsonArray(key, define.getType());
    }

    /**
     * 获取 json
     *
     * @param key  key
     * @param type type
     * @param <T>  T
     * @return T
     */
    public static <T> List<T> getJsonArray(String key, Class<T> type) {
        return get(key, s -> JSON.parseArray(s, type));
    }

    /**
     * 获取 json 列表
     *
     * @param keys keys
     * @return cache
     */
    public static List<String> getList(Collection<String> keys) {
        return getList(keys, Function.identity());
    }

    /**
     * 获取 json 列表
     *
     * @param keys   keys
     * @param mapper mapper
     * @param <T>    T
     * @return cache
     */
    public static <T> List<T> getList(Collection<String> keys, Function<String, T> mapper) {
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) {
            return new ArrayList<>();
        }
        return values.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * 获取 json 列表
     *
     * @param keys keys
     * @return cache
     */
    public static List<JSONObject> getJsonList(Collection<String> keys) {
        return getList(keys, JSON::parseObject);
    }

    /**
     * 获取 json 列表
     *
     * @param keys   keys
     * @param define define
     * @param <T>    T
     * @return cache
     */
    public static <T> List<T> getJsonList(Collection<String> keys, CacheKeyDefine define) {
        return getList(keys, s -> JSON.parseObject(s, (Class<T>) define.getType()));
    }

    /**
     * 获取 json 列表
     *
     * @param keys keys
     * @param type type
     * @param <T>  T
     * @return cache
     */
    public static <T> List<T> getJsonList(Collection<String> keys, Class<T> type) {
        return getList(keys, s -> JSON.parseObject(s, type));
    }

    /**
     * 获取 jsonArray 列表
     *
     * @param keys keys
     * @return cache
     */
    public static List<JSONArray> getJsonArrayList(Collection<String> keys) {
        return getList(keys, JSON::parseArray);
    }

    /**
     * 获取 jsonArray 列表
     *
     * @param keys   keys
     * @param define define
     * @param <T>    T
     * @return cache
     */
    public static <T> List<List<T>> getJsonArrayList(Collection<String> keys, CacheKeyDefine define) {
        return getList(keys, s -> JSON.parseArray(s, (Class<T>) define.getType()));
    }

    /**
     * 获取 jsonArray 列表
     *
     * @param keys keys
     * @param type type
     * @param <T>  T
     * @return cache
     */
    public static <T> List<List<T>> getJsonArrayList(Collection<String> keys, Class<T> type) {
        return getList(keys, s -> JSON.parseArray(s, type));
    }

    /**
     * 设置值
     *
     * @param define define
     * @param value  value
     */
    public static void set(CacheKeyDefine define, Object value) {
        set(define.getKey(), define, value);
    }

    /**
     * 设置值
     *
     * @param key    key
     * @param define define
     * @param value  value
     */
    public static void set(String key, CacheKeyDefine define, Object value) {
        if (value == null) {
            value = Strings.EMPTY;
        }
        if (define == null || define.getTimeout() == 0) {
            // 不过期
            redisTemplate.opsForValue().set(key, value.toString());
        } else {
            // 过期
            redisTemplate.opsForValue().set(key, value.toString(),
                    define.getTimeout(),
                    define.getUnit());
        }
    }

    /**
     * 设置 json
     *
     * @param define define
     * @param value  value
     */
    public static void setJson(CacheKeyDefine define, Object value) {
        setJson(define.getKey(), define, value);
    }

    /**
     * 设置 json
     *
     * @param key    key
     * @param define define
     * @param value  value
     */
    public static void setJson(String key, CacheKeyDefine define, Object value) {
        if (define == null || define.getTimeout() == 0) {
            // 不过期
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
        } else {
            // 过期
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value),
                    define.getTimeout(),
                    define.getUnit());
        }
    }

    /**
     * 获取并且设置 json
     *
     * @param define    define
     * @param processor processor
     * @param params    params
     * @param <T>       type
     */
    public static <T> void processSetJson(CacheKeyDefine define, Consumer<T> processor, Object... params) {
        processSetJson(define.format(params), define, processor);
    }

    /**
     * 获取并且设置 json
     *
     * @param key       key
     * @param define    define
     * @param processor processor
     * @param <T>       type
     */
    public static <T> void processSetJson(String key, CacheKeyDefine define, Consumer<T> processor) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return;
        }
        // 转换
        T cache = (T) JSON.parseObject(value, define.getType());
        // 执行处理逻辑
        processor.accept(cache);
        // 重新设置
        setJson(key, define, cache);
    }

}
