package com.orion.ops.framework.mybatis.core.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;

import java.util.Collection;

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

    /**
     * eq
     *
     * @param mapping mapping
     * @param e       e
     * @param <T>     T
     * @param <E>     E
     * @return wrapper
     */
    public static <T, E> LambdaQueryWrapper<T> eq(SFunction<T, E> mapping, E e) {
        Valid.notNull(e, ErrorMessage.INVALID_PARAM);
        return new LambdaQueryWrapper<T>().eq(mapping, e);
    }

    /**
     * in
     *
     * @param mapping mapping
     * @param es      es
     * @param <T>     T
     * @param <E>     E
     * @return wrapper
     */
    public static <T, E> LambdaQueryWrapper<T> in(SFunction<T, E> mapping, Collection<E> es) {
        Valid.notEmpty(es, ErrorMessage.INVALID_PARAM);
        return new LambdaQueryWrapper<T>().in(mapping, es);
    }

}
