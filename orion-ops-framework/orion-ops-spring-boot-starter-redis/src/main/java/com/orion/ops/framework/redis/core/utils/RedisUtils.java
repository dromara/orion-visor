package com.orion.ops.framework.redis.core.utils;

import com.alibaba.fastjson.JSON;
import com.orion.lang.define.cache.CacheKeyDefine;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.Streams;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * redis 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 11:09
 */
@SuppressWarnings("unchecked")
public class RedisUtils {

    private static RedisTemplate<String, String> redisTemplate;

    private RedisUtils() {
    }

    /**
     * 扫描 key
     *
     * @param match 匹配值
     * @return keys
     */
    public static Set<String> scanKeys(String match) {
        return scanKeys(ScanOptions.scanOptions()
                .match(match)
                .build());
    }

    /**
     * 扫描 key
     *
     * @param scanOptions scan
     * @return keys
     */
    public static Set<String> scanKeys(ScanOptions scanOptions) {
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(scanOptions);
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next()));
            }
            Streams.close(cursor);
            return keys;
        });
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
     * 查询 list 区间
     *
     * @param key key
     * @return list
     */
    public static List<String> listRange(String key) {
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
    public static <T> List<T> listRange(String key, Function<String, T> mapper) {
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
     * @param key    key
     * @param define define
     * @return list
     */
    public static <T> List<T> listRangeJson(String key, CacheKeyDefine define) {
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
     * @param key    key
     * @param list   list
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void listPushAll(String key, List<T> list, Function<T, String> mapper) {
        redisTemplate.opsForList().rightPushAll(key, Lists.map(list, mapper));
    }

    /**
     * list 添加元素
     *
     * @param key  key
     * @param list list
     * @param <T>  T
     */
    public static <T> void listPushAllJson(String key, List<T> list) {
        List<String> values = list.stream()
                .map(JSON::toJSONString)
                .collect(Collectors.toList());
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * list 添加元素
     *
     * @param key    key
     * @param value  value
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void listPush(String key, T value, Function<T, String> mapper) {
        redisTemplate.opsForList().rightPush(key, mapper.apply(value));
    }

    /**
     * list 添加元素
     *
     * @param key   key
     * @param value value
     * @param <T>   T
     */
    public static <T> void listPushJson(String key, T value) {
        redisTemplate.opsForList().rightPush(key, JSON.toJSONString(value));
    }

    /**
     * list 删除元素
     *
     * @param key    key
     * @param value  value
     * @param mapper mapper
     * @param <T>    T
     */
    public static <T> void listRemove(String key, T value, Function<T, String> mapper) {
        redisTemplate.opsForList().remove(key, 1, mapper.apply(value));
    }

    /**
     * list 删除元素
     *
     * @param key   key
     * @param value value
     * @param <T>   T
     */
    public static <T> void listRemoveJson(String key, T value) {
        redisTemplate.opsForList().remove(key, 1, JSON.toJSONString(value));
    }

    /**
     * 设置过期时间
     *
     * @param key    key
     * @param define define
     */
    public static void setExpire(String key, CacheKeyDefine define) {
        if (define.getTimeout() != 0) {
            // 设置过期时间
            redisTemplate.expire(key, define.getTimeout(), define.getUnit());
        }
    }

    public static void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

}

