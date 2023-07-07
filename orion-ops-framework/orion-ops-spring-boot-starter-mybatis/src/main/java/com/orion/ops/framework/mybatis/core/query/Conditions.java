package com.orion.ops.framework.mybatis.core.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 条件构建类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 12:24
 */
public class Conditions {

    private Conditions() {
    }

    /**
     * 条件有效性验证 wrapper
     *
     * @param <T> T
     * @return wrapper
     */
    public static <T> LambdaQueryWrapper<T> wrapper() {
        return new ValidateLambdaWrapper<>();
    }

    /**
     * 条件有效性验证 wrapper
     *
     * @param <T> T
     * @return wrapper
     */
    public static <T> LambdaQueryWrapper<T> wrapper(Class<T> clazz) {
        return new ValidateLambdaWrapper<>(clazz);
    }

}
