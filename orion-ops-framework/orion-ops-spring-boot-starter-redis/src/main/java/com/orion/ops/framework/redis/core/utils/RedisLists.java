package com.orion.ops.framework.redis.core.utils;

import com.alibaba.fastjson.JSON;
import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * redis list 工具类
 * <p>
 * 写操作会自动设置过期时间 如果有
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/20 14:08
 */
@SuppressWarnings("unchecked")
public class RedisLists extends RedisUtils {

    private RedisLists() {
    }

    /**
     * 查询 list 区间
     *
     * @param key key
     * @return list
     */
    public static List<String> range(String key) {
        // 查询列表
        List<String> elements = redisTemplate.opsForList().range(key, 0, -1);
        if (elements == null) {
            return Lists.newList();
        }
        return elements;
    }

    /**
     * 查询 list 区间
     *
     * @param key    key
     * @param mapper mapper
     * @param <T>    T
     * @return list
     */
    public static <T> List<T> range(String key, Function<String, T> mapper) {
        // 查询列表
        List<String> elements = redisTemplate.opsForList().range(key, 0, -1);
        if (elements == null) {
            return Lists.newList();
        }
        return Lists.map(elements, mapper);
    }

    /**
     * 查询 list 区间
     *
     * @param define define
     * @return list
     */
    public static <T> List<T> rangeJson(CacheKeyDefine define) {
        return rangeJson(define.getKey(), define);
    }

    /**
     * 查询 list 区间
     *
     * @param key    key
     * @param define define
     * @return list
     */
    public static <T> List<T> rangeJson(String key, CacheKeyDefine define) {
        // 查询列表
        List<String> elements = redisTemplate.opsForList().range(key, 0, -1);
        if (elements == null) {
            return Lists.newList();
        }
        return (List<T>) elements.stream()
                .map(s -> JSON.parseObject(s, define.getType()))
                .collect(Collectors.toList());
    }

    /**
     * list 添加元素
     *
     * @param key   key
     * @param value value
     * @param <T>   T
     */
    public static <T> void push(CacheKeyDefine key, String value) {
        push(key.getKey(), key, value, Function.identity());
    }

    /**
     * list 添加元素
     *
     * @param key   key
     * @param value value
     * @param <T>   T
     */
    public static <T> void push(String key, String value) {
        push(key, null, value, Function.identity());
    }

    /**
     * list 添加元素
     *
     * @param key    key
     * @param define define
     * @param value  value
     * @param <T>    T
     */
    public static <T> void push(String key, CacheKeyDefine define, String value) {
        push(key, define, value, Function.identity());
    }

    /**
     * list 添加元素
     *
     * @param key    key
     * @param value  value
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void push(CacheKeyDefine key, T value, Function<T, String> mapper) {
        push(key.getKey(), key, value, mapper);
    }

    /**
     * list 添加元素
     *
     * @param key    key
     * @param value  value
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void push(String key, T value, Function<T, String> mapper) {
        push(key, null, value, mapper);
        redisTemplate.opsForList().rightPush(key, mapper.apply(value));
    }

    /**
     * list 添加元素
     *
     * @param key    key
     * @param define define
     * @param value  value
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void push(String key, CacheKeyDefine define, T value, Function<T, String> mapper) {
        redisTemplate.opsForList().rightPush(key, mapper.apply(value));
        if (define != null) {
            setExpire(key, define);
        }
    }

    /**
     * list 添加元素
     *
     * @param key   key
     * @param value value
     * @param <T>   T
     */
    public static <T> void pushJson(String key, T value) {
        redisTemplate.opsForList().rightPush(key, JSON.toJSONString(value));
    }

    /**
     * list 添加元素
     *
     * @param define define
     * @param list   list
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void pushAll(CacheKeyDefine define, List<T> list, Function<T, String> mapper) {
        pushAll(define.getKey(), define, list, mapper);
    }

    /**
     * list 添加元素
     *
     * @param key    key
     * @param list   list
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void pushAll(String key, List<T> list, Function<T, String> mapper) {
        pushAll(key, null, list, mapper);
    }

    /**
     * list 添加元素
     *
     * @param key    key
     * @param define define
     * @param list   list
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void pushAll(String key, CacheKeyDefine define, List<T> list, Function<T, String> mapper) {
        redisTemplate.opsForList().rightPushAll(key, Lists.map(list, mapper));
        if (define != null) {
            setExpire(key, define);
        }
    }

    /**
     * list 添加元素
     *
     * @param key  key
     * @param list list
     * @param <T>  T
     */
    public static <T> void pushAllJson(CacheKeyDefine key, List<T> list) {
        pushAllJson(key.getKey(), key, list);
    }

    /**
     * list 添加元素
     *
     * @param key  key
     * @param list list
     * @param <T>  T
     */
    public static <T> void pushAllJson(String key, List<T> list) {
        pushAllJson(key, null, list);
    }

    /**
     * list 添加元素
     *
     * @param key    key
     * @param define define
     * @param list   list
     * @param <T>    T
     */
    public static <T> void pushAllJson(String key, CacheKeyDefine define, List<T> list) {
        List<String> values = list.stream()
                .map(JSON::toJSONString)
                .collect(Collectors.toList());
        redisTemplate.opsForList().rightPushAll(key, values);
        if (define != null) {
            setExpire(key, define);
        }
    }

    /**
     * 是否包含某个值
     *
     * @param key   key
     * @param value value
     * @return 是否包含
     */
    public static boolean contains(String key, String value) {
        Long index = redisTemplate.opsForList().indexOf(key, value);
        return index != null && !Const.L_N_1.equals(index);
    }

    /**
     * list 删除元素
     *
     * @param key    key
     * @param value  value
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void remove(String key, T value, Function<T, String> mapper) {
        redisTemplate.opsForList().remove(key, 1, mapper.apply(value));
    }

    /**
     * list 删除元素
     *
     * @param key   key
     * @param value value
     * @param <T>   T
     */
    public static <T> void removeJson(String key, T value) {
        redisTemplate.opsForList().remove(key, 1, JSON.toJSONString(value));
    }

}
