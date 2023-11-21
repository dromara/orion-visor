package com.orion.ops.framework.redis.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.utils.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
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
     * 获取 json
     *
     * @param key key
     * @return JSONObject
     */
    public static JSONObject getJson(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value);
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
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return (T) JSON.parseObject(value, type);
    }

    /**
     * 获取 json 列表
     *
     * @param keys keys
     * @return cache
     */
    public static List<JSONObject> getJsonList(Collection<String> keys) {
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) {
            return new ArrayList<>();
        }
        return values.stream()
                .map(JSON::parseObject)
                .collect(Collectors.toList());
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
        return getJsonList(keys, (Class<T>) define.getType());
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
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) {
            return new ArrayList<>();
        }
        return values.stream()
                .map(s -> JSON.parseObject(s, type))
                .collect(Collectors.toList());
    }

    /**
     * 获取 json
     *
     * @param key key
     * @return JSONArray
     */
    public static JSONArray getJsonArray(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parseArray(value);
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
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return JSON.parseArray(value, type);
    }

    /**
     * 获取 jsonArray 列表
     *
     * @param keys keys
     * @return cache
     */
    public static List<JSONArray> getJsonArrayList(Collection<String> keys) {
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) {
            return new ArrayList<>();
        }
        return values.stream()
                .map(JSON::parseArray)
                .collect(Collectors.toList());
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
        return getJsonArrayList(keys, (Class<T>) define.getType());
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
        List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (values == null) {
            return new ArrayList<>();
        }
        return values.stream()
                .map(s -> JSON.parseArray(s, type))
                .collect(Collectors.toList());
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
