package com.orion.ops.framework.mybatis.query;

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
    public static <T> LambdaQueryWrapper<T> validateWrapper() {
        return new ValidateLambdaWrapper<>();
    }

}
