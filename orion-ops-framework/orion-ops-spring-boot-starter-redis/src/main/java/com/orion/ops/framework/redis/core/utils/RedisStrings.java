package com.orion.ops.framework.redis.core.utils;

import com.alibaba.fastjson.JSON;
import com.orion.lang.define.cache.CacheKeyDefine;
import com.orion.lang.utils.Strings;

import java.util.List;
import java.util.function.Consumer;

/**
 * redis string 工具类
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
     * @param define define
     * @param <T>    T
     * @return T
     */
    public static <T> T getJson(CacheKeyDefine define) {
        return getJson(define.getKey(), define);
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
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return (T) JSON.parseObject(value, define.getType());
    }

    /**
     * 获取 json
     *
     * @param define define
     * @param <T>    T
     * @return T
     */
    public static <T> List<T> getJsonArray(CacheKeyDefine define) {
        return getJsonArray(define.getKey(), define);
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
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }
        return (List<T>) JSON.parseArray(value, define.getType());
    }

    /**
     * 设置 json
     *
     * @param key    key
     * @param define define
     * @param value  value
     */
    public static void set(String key, CacheKeyDefine define, Object value) {
        if (value == null) {
            value = Strings.EMPTY;
        }
        if (define.getTimeout() == 0) {
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
     * @param key    key
     * @param define define
     * @param value  value
     */
    public static void setJson(String key, CacheKeyDefine define, Object value) {
        if (define.getTimeout() == 0) {
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
