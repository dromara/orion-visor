package org.dromara.visor.common.config;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * 配置引用
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/6 18:01
 */
@Slf4j
public class ConfigRef<T> {

    public final String key;

    private final Function<String, T> convert;

    public T value;

    public ConfigRef(String key, Function<String, T> convert) {
        this.key = key;
        this.convert = convert;
    }

    /**
     * 覆盖配置
     *
     * @param value value
     */
    public void override(String value) {
        try {
            this.value = convert.apply(value);
        } catch (Exception e) {
            log.error("ConfigRef trigger override error key: {}, value: {}", key, value, e);
        }
    }

    /**
     * 设置值
     *
     * @param value value
     */
    public void set(T value) {
        this.value = value;
    }

}
