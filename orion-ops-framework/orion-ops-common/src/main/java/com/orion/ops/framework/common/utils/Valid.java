package com.orion.ops.framework.common.utils;

import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.spring.SpringHolder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.function.Function;

/**
 * 验证器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/18 11:23
 */
public class Valid extends com.orion.lang.utils.Valid {

    private static final Validator VALIDATOR = SpringHolder.getBean(Validator.class);

    /**
     * 验证枚举
     *
     * @param of  of method
     * @param obj obj
     * @param <T> param
     * @param <E> enum
     * @return enum
     */
    public static <T, E extends Enum<?>> E valid(Function<T, E> of, T obj) {
        return notNull(of.apply(obj), ErrorMessage.INVALID_PARAM);
    }

    /**
     * 验证对象
     *
     * @param obj    obj
     * @param groups groups
     * @param <T>    T
     * @return obj
     */
    public static <T> T valid(T obj, Class<?>... groups) {
        notNull(obj, ErrorMessage.PARAM_MISSING);
        // 验证对象
        Set<ConstraintViolation<T>> set = VALIDATOR.validate(obj, groups);
        if (!set.isEmpty()) {
            throw new ConstraintViolationException(set);
        }
        return obj;
    }

}
