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
import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.HashOperations;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * redis hash 工具类
 * <p>
 * 写操作会自动设置过期时间 如果有
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/22 13:44
 */
@SuppressWarnings("unchecked")
public class RedisMaps extends RedisUtils {

    private RedisMaps() {
    }

    /**
     * 插入数据
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public static void put(CacheKeyDefine key, Object hashKey, Object value) {
        put(key.getKey(), key, hashKey, value);
    }

    /**
     * 插入数据
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public static void put(String key, Object hashKey, Object value) {
        put(key, null, hashKey, value);
    }

    /**
     * 插入数据
     *
     * @param key     key
     * @param define  define
     * @param hashKey hashKey
     * @param value   value
     */
    public static void put(String key, CacheKeyDefine define, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, Objects1.toString(hashKey), Objects1.toString(value));
        if (define != null) {
            setExpire(key, define);
        }
    }

    /**
     * 插入数据 json
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public static void putJson(CacheKeyDefine key, Object hashKey, Object value) {
        put(key.getKey(), key, hashKey, JSON.toJSONString(value));
    }

    /**
     * 插入数据 json
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public static void putJson(String key, Object hashKey, Object value) {
        put(key, null, hashKey, JSON.toJSONString(value));
    }

    /**
     * 插入数据 json
     *
     * @param key     key
     * @param define  define
     * @param hashKey hashKey
     * @param value   value
     */
    public static void putJson(String key, CacheKeyDefine define, Object hashKey, Object value) {
        put(key, define, hashKey, JSON.toJSONString(value));
    }

    /**
     * 插入数据 json
     *
     * @param key       key
     * @param keyMapper keyMapper
     * @param value     value
     * @param <V>       V
     */
    public static <V> void putJson(CacheKeyDefine key, Function<V, String> keyMapper, V value) {
        put(key.getKey(), key, keyMapper.apply(value), JSON.toJSONString(value));
    }

    /**
     * 插入数据 json
     *
     * @param key       key
     * @param keyMapper keyMapper
     * @param value     value
     * @param <V>       V
     */
    public static <V> void putJson(String key, Function<V, String> keyMapper, V value) {
        put(key, null, keyMapper.apply(value), JSON.toJSONString(value));
    }

    /**
     * 插入数据 json
     *
     * @param key       key
     * @param define    define
     * @param keyMapper keyMapper
     * @param value     value
     * @param <V>       V
     */
    public static <V> void putJson(String key, CacheKeyDefine define, Function<V, String> keyMapper, V value) {
        put(key, define, keyMapper.apply(value), JSON.toJSONString(value));
    }

    /**
     * 插入数据
     *
     * @param key    key
     * @param values values
     */
    public static void putAll(CacheKeyDefine key, Map<?, ?> values) {
        putAll(key.getKey(), key, values);
    }

    /**
     * 插入数据
     *
     * @param key    key
     * @param values values
     */
    public static void putAll(String key, Map<?, ?> values) {
        putAll(key, null, values);
    }

    /**
     * 插入数据
     *
     * @param key    key
     * @param define define
     * @param values values
     */
    public static void putAll(String key, CacheKeyDefine define, Map<?, ?> values) {
        Map<String, String> map = Maps.map(values, Objects1::toString, Objects1::toString);
        redisTemplate.opsForHash().putAll(key, map);
        if (define != null) {
            setExpire(key, define);
        }
    }

    /**
     * 插入数据 json
     *
     * @param key    key
     * @param values values
     * @param <V>    V
     */
    public static <V> void putAllJson(String key, Map<?, V> values) {
        putAllJson(key, null, values);
    }

    /**
     * 插入数据 json
     *
     * @param key    key
     * @param values values
     * @param <V>    V
     */
    public static <V> void putAllJson(CacheKeyDefine key, Map<?, V> values) {
        putAllJson(key.getKey(), key, values);
    }

    /**
     * 插入数据 json
     *
     * @param key    key
     * @param define define
     * @param values values
     * @param <V>    V
     */
    public static <V> void putAllJson(String key, CacheKeyDefine define, Map<?, V> values) {
        Map<String, String> map = Maps.map(values, Objects1::toString, JSON::toJSONString);
        redisTemplate.opsForHash().putAll(key, map);
        if (define != null) {
            setExpire(key, define);
        }
    }

    /**
     * 插入数据 json
     *
     * @param key       key
     * @param keyMapper keyMapper
     * @param values    values
     * @param <V>       V
     */
    public static <V> void putAllJson(String key, Function<V, String> keyMapper, List<V> values) {
        putAllJson(key, null, keyMapper, values);
    }

    /**
     * 插入数据 json
     *
     * @param key       key
     * @param keyMapper keyMapper
     * @param values    values
     * @param <V>       V
     */
    public static <V> void putAllJson(CacheKeyDefine key, Function<V, String> keyMapper, List<V> values) {
        putAllJson(key.getKey(), key, keyMapper, values);
    }

    /**
     * 插入数据 json
     *
     * @param key       key
     * @param define    define
     * @param keyMapper keyMapper
     * @param values    values
     * @param <V>       V
     */
    public static <V> void putAllJson(String key, CacheKeyDefine define,
                                      Function<V, String> keyMapper, List<V> values) {
        Map<String, String> map = values.stream()
                .collect(Collectors.toMap(keyMapper, JSON::toJSONString, Functions.right()));
        redisTemplate.opsForHash().putAll(key, map);
        if (define != null) {
            setExpire(key, define);
        }
    }

    /**
     * 获取值
     *
     * @param key     key
     * @param hashKey hashKey
     * @return value
     */
    public static String get(CacheKeyDefine key, Object hashKey) {
        return get(key.getKey(), hashKey);
    }

    /**
     * 获取值
     *
     * @param key     key
     * @param hashKey hashKey
     * @return value
     */
    public static String get(String key, Object hashKey) {
        Object value = redisTemplate.opsForHash().get(key, Objects1.toString(hashKey));
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    /**
     * 获取值 json
     *
     * @param key     key
     * @param hashKey hashKey
     * @param <V>     V
     * @return value
     */
    public static <V> V getJson(CacheKeyDefine key, Object hashKey) {
        return getJson(key.getKey(), hashKey, (Class<V>) key.getType());
    }

    /**
     * 获取值 json
     *
     * @param key     key
     * @param hashKey hashKey
     * @param clazz   clazz
     * @param <V>     V
     * @return value
     */
    public static <V> V getJson(String key, Object hashKey, Class<V> clazz) {
        Object value = redisTemplate.opsForHash().get(key, Objects1.toString(hashKey));
        if (value == null) {
            return null;
        }
        return JSON.parseObject(Objects1.toString(value), clazz);
    }

    /**
     * 获取值
     *
     * @param key      key
     * @param hashKeys hashKeys
     * @return values
     */
    public static List<String> multiGet(CacheKeyDefine key, List<?> hashKeys) {
        return multiGet(key.getKey(), hashKeys);
    }

    /**
     * 获取值
     *
     * @param key      key
     * @param hashKeys hashKeys
     * @return values
     */
    public static List<String> multiGet(String key, List<?> hashKeys) {
        HashOperations<String, String, String> op = redisTemplate.opsForHash();
        return op.multiGet(key, Lists.map(hashKeys, Objects1::toString));
    }

    /**
     * 获取值 json
     *
     * @param key       key
     * @param hashKeys  hashKeys
     * @param keyMapper keyMapper
     * @param <K>       K
     * @param <V>       V
     * @return values
     */
    public static <V> List<V> multiGetJson(CacheKeyDefine key, List<?> hashKeys) {
        return multiGetJson(key.getKey(), hashKeys, (Class<V>) key.getType());
    }

    /**
     * 获取值 json
     *
     * @param key       key
     * @param hashKeys  hashKeys
     * @param keyMapper keyMapper
     * @param clazz     clazz
     * @param <K>       K
     * @param <V>       V
     * @return values
     */
    public static <V> List<V> multiGetJson(String key, List<?> hashKeys, Class<V> clazz) {
        HashOperations<String, String, String> op = redisTemplate.opsForHash();
        List<String> multiKeys = hashKeys.stream()
                .map(Objects1::toString)
                .collect(Collectors.toList());
        return op.multiGet(key, multiKeys)
                .stream()
                .map(s -> JSON.parseObject(s, clazz))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有 key
     *
     * @param key key
     * @return keys
     */
    public static List<String> keys(CacheKeyDefine key) {
        return keys(key.getKey(), Function.identity());
    }

    /**
     * 获取所有 key
     *
     * @param key key
     * @return keys
     */
    public static List<String> keys(String key) {
        return keys(key, Function.identity());
    }

    /**
     * 获取所有 key
     *
     * @param key       key
     * @param keyMapper keyMapper
     * @param <K>       K
     * @return keys
     */
    public static <K> List<K> keys(CacheKeyDefine key, Function<String, K> keyMapper) {
        return keys(key.getKey(), keyMapper);
    }

    /**
     * 获取所有 key
     *
     * @param key       key
     * @param keyMapper keyMapper
     * @param <K>       K
     * @return keys
     */
    public static <K> List<K> keys(String key, Function<String, K> keyMapper) {
        HashOperations<String, String, String> op = redisTemplate.opsForHash();
        return op.keys(key)
                .stream()
                .map(keyMapper)
                .collect(Collectors.toList());
    }

    /**
     * 删除
     *
     * @param key     key
     * @param hashKey hashKey
     */
    public static void delete(CacheKeyDefine key, Object hashKey) {
        delete(key.getKey(), hashKey);
    }

    /**
     * 删除
     *
     * @param key     key
     * @param hashKey hashKey
     */
    public static void delete(String key, Object hashKey) {
        redisTemplate.opsForHash().delete(key, Objects1.toString(hashKey));
    }

    /**
     * 删除
     *
     * @param key      key
     * @param hashKeys hashKeys
     */
    public static void delete(CacheKeyDefine key, List<?> hashKeys) {
        delete(key.getKey(), hashKeys);
    }

    /**
     * 删除
     *
     * @param key      key
     * @param hashKeys hashKeys
     */
    public static void delete(String key, List<?> hashKeys) {
        Object[] deleteKeys = hashKeys.stream()
                .map(Objects1::toString)
                .toArray(Object[]::new);
        redisTemplate.opsForHash().delete(key, deleteKeys);
    }

    /**
     * 获取数量
     *
     * @param key key
     * @return size
     */
    public static Long size(CacheKeyDefine key) {
        return size(key.getKey());
    }

    /**
     * 获取数量
     *
     * @param key key
     * @return size
     */
    public static Long size(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取所有键值对
     *
     * @param key key
     * @return entity
     */
    public static Map<String, String> entities(CacheKeyDefine key) {
        return entities(key.getKey());
    }

    /**
     * 获取所有键值对
     *
     * @param key key
     * @return entity
     */
    public static Map<String, String> entities(String key) {
        HashOperations<String, String, String> op = redisTemplate.opsForHash();
        return op.entries(key);
    }

    /**
     * 获取所有键值对 json
     *
     * @param key key
     * @param <V> V
     * @return entity
     */
    public static <V> Map<String, V> entitiesJson(CacheKeyDefine key) {
        return entitiesJson(key.getKey(), Function.identity(), (Class<V>) key.getType());
    }

    /**
     * 获取所有键值对 json
     *
     * @param key   key
     * @param clazz clazz
     * @param <V>   V
     * @return entity
     */
    public static <V> Map<String, V> entitiesJson(String key, Class<V> clazz) {
        return entitiesJson(key, Function.identity(), clazz);
    }

    /**
     * 获取所有键值对 json
     *
     * @param key       key
     * @param keyMapper keyMapper
     * @param <K>       K
     * @param <V>       V
     * @return entity
     */
    public static <K, V> Map<K, V> entitiesJson(CacheKeyDefine key, Function<String, K> keyMapper) {
        return entitiesJson(key.getKey(), keyMapper, (Class<V>) key.getType());
    }

    /**
     * 获取所有键值对 json
     *
     * @param key       key
     * @param keyMapper keyMapper
     * @param clazz     clazz
     * @param <K>       K
     * @param <V>       V
     * @return entity
     */
    public static <K, V> Map<K, V> entitiesJson(String key, Function<String, K> keyMapper, Class<V> clazz) {
        Map<String, String> entities = entities(key);
        return Maps.map(entities, keyMapper, v -> (V) JSON.parseObject(v, clazz));
    }

    /**
     * 获取所有值
     *
     * @param key key
     * @return values
     */
    public static List<String> values(CacheKeyDefine key) {
        return values(key.getKey());
    }

    /**
     * 获取所有值
     *
     * @param key key
     * @return values
     */
    public static List<String> values(String key) {
        HashOperations<String, String, String> op = redisTemplate.opsForHash();
        return op.values(key);
    }

    /**
     * 获取所有值 json
     *
     * @param key key
     * @param <V> V
     * @return values
     */
    public static <V> List<V> valuesJson(CacheKeyDefine key) {
        return valuesJson(key.getKey(), (Class<V>) key.getType());
    }

    /**
     * 获取所有值 json
     *
     * @param key    key
     * @param define define
     * @param <V>    V
     * @return values
     */
    public static <V> List<V> valuesJson(String key, CacheKeyDefine define) {
        return valuesJson(key, (Class<V>) define.getType());
    }

    /**
     * 获取所有值 json
     *
     * @param key key
     * @param <V> V
     * @return values
     */
    public static <V> List<V> valuesJson(String key, Class<V> clazz) {
        return values(key).stream()
                .map(s -> JSON.parseObject(s, clazz))
                .collect(Collectors.toList());
    }

}
