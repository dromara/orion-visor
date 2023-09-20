package com.orion.ops.framework.redis.core.utils;

import com.orion.lang.define.cache.CacheKeyDefine;
import com.orion.lang.utils.io.Streams;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashSet;
import java.util.Set;

/**
 * redis 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/11/6 11:09
 */
public class RedisUtils {

    protected static RedisTemplate<String, String> redisTemplate;

    protected RedisUtils() {
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
     * 设置过期时间
     *
     * @param define define
     */
    public static void setExpire(CacheKeyDefine define) {
        setExpire(define.getKey(), define);
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

