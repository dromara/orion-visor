package org.dromara.visor.common.config;

import java.util.function.Function;

/**
 * 配置中心
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/5 21:30
 */
public interface ConfigStore {

    /**
     * 获取配置
     *
     * @param key key
     * @return conf
     */
    String getConfig(String key);

    /**
     * 获取配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return conf
     */
    String getConfig(String key, String defaultValue);

    /**
     * 获取配置
     *
     * @param key     key
     * @param convert convert
     * @param <T>     T
     * @return conf
     */
    <T> T getConfig(String key, Function<String, T> convert);

    /**
     * 获取配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @param <T>          T
     * @return conf
     */
    <T> T getConfig(String key, Function<String, T> convert, T defaultValue);

    /**
     * 获取 string 配置
     *
     * @param key key
     * @return ref
     */
    ConfigRef<String> string(String key);

    /**
     * 获取 string 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    ConfigRef<String> string(String key, String defaultValue);

    /**
     * 获取 int 配置
     *
     * @param key key
     * @return ref
     */
    ConfigRef<Integer> int32(String key);

    /**
     * 获取 int 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    ConfigRef<Integer> int32(String key, Integer defaultValue);

    /**
     * 获取 long 配置
     *
     * @param key key
     * @return ref
     */
    ConfigRef<Long> int64(String key);

    /**
     * 获取 long 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    ConfigRef<Long> int64(String key, Long defaultValue);

    /**
     * 获取 double 配置
     *
     * @param key key
     * @return ref
     */
    ConfigRef<Double> float64(String key);

    /**
     * 获取 double 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    ConfigRef<Double> float64(String key, Double defaultValue);

    /**
     * 获取 boolean 配置
     *
     * @param key key
     * @return ref
     */
    ConfigRef<Boolean> bool(String key);

    /**
     * 获取 boolean 配置
     *
     * @param key          key
     * @param defaultValue defaultValue
     * @return ref
     */
    ConfigRef<Boolean> bool(String key, Boolean defaultValue);

    /**
     * 获取配置
     *
     * @param key     key
     * @param convert convert
     * @param <T>     T
     * @return ref
     */
    <T> ConfigRef<T> ref(String key, Function<String, T> convert);

    /**
     * 获取配置
     *
     * @param key          key
     * @param convert      convert
     * @param defaultValue defaultValue
     * @param <T>          T
     * @return ref
     */
    <T> ConfigRef<T> ref(String key, Function<String, T> convert, T defaultValue);

}
