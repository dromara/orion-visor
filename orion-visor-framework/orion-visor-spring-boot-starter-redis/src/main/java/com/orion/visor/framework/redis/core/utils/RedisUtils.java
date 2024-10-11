/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.framework.redis.core.utils;

import com.orion.lang.define.cache.key.CacheKeyDefine;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.Streams;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
     * 扫描并删除 key
     *
     * @param match match
     */
    public static void scanKeysDelete(String match) {
        Set<String> keys = scanKeys(match);
        if (keys.isEmpty()) {
            return;
        }
        redisTemplate.delete(keys);
    }

    /**
     * 扫描并删除 key
     *
     * @param match match
     */
    public static void scanKeysDelete(String... match) {
        if (Arrays1.isEmpty(match)) {
            return;
        }
        List<String> keys = Arrays.stream(match)
                .map(RedisUtils::scanKeys)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (keys.isEmpty()) {
            return;
        }
        redisTemplate.delete(keys);
    }

    /**
     * 扫描并删除 key
     *
     * @param match match
     */
    public static void scanKeysDelete(List<String> match) {
        List<String> keys = match.stream()
                .map(RedisUtils::scanKeys)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        if (keys.isEmpty()) {
            return;
        }
        redisTemplate.delete(keys);
    }

    /**
     * 删除 key
     *
     * @param define define
     */
    public static void delete(CacheKeyDefine define) {
        delete(define.getKey());
    }

    /**
     * 删除 key
     *
     * @param key key
     */
    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除 key
     *
     * @param keys keys
     */
    public static void delete(String... keys) {
        if (Arrays1.isEmpty(keys)) {
            return;
        }
        redisTemplate.delete(Arrays.asList(keys));
    }

    /**
     * 删除 key
     *
     * @param keys keys
     */
    public static void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
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
        if (define != null && define.getTimeout() != 0) {
            // 设置过期时间
            redisTemplate.expire(key, define.getTimeout(), define.getUnit());
        }
    }

    /**
     * 设置过期时间
     *
     * @param key     key
     * @param timeout timeout
     * @param unit    unit
     */
    public static void setExpire(String key, long timeout, TimeUnit unit) {
        // 设置过期时间
        redisTemplate.expire(key, timeout, unit);
    }

    public static void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        if (RedisUtils.redisTemplate != null) {
            // unmodified
            throw Exceptions.state();
        }
        RedisUtils.redisTemplate = redisTemplate;
    }

}

