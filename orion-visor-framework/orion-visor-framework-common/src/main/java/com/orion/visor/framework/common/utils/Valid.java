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
package com.orion.visor.framework.common.utils;

import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.io.Files1;
import com.orion.spring.SpringHolder;
import com.orion.visor.framework.common.constant.ErrorMessage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Map;
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

    public static <T> T notNull(T object) {
        return notNull(object, ErrorMessage.PARAM_MISSING);
    }

    public static <T> T isNull(T object) {
        return isNull(object, ErrorMessage.PARAM_ERROR);
    }

    public static String notBlank(String s) {
        return notBlank(s, ErrorMessage.PARAM_MISSING);
    }

    public static <T extends Map<?, ?>> T notEmpty(T map) {
        return notEmpty(map, ErrorMessage.PARAM_MISSING);
    }

    public static <T extends Collection<?>> T notEmpty(T object) {
        return notEmpty(object, ErrorMessage.PARAM_MISSING);
    }

    public static void allNotNull(Object... objects) {
        if (objects != null) {
            for (Object t : objects) {
                notNull(t, ErrorMessage.PARAM_MISSING);
            }
        }
    }

    public static void allNotBlank(String... ss) {
        if (ss != null) {
            for (String s : ss) {
                notBlank(s, ErrorMessage.PARAM_MISSING);
            }
        }
    }

    public static void eq(Object o1, Object o2) {
        eq(o1, o2, ErrorMessage.INVALID_PARAM);
    }

    public static boolean isTrue(boolean s) {
        return isTrue(s, ErrorMessage.INVALID_PARAM);
    }

    public static boolean isFalse(boolean s) {
        return isFalse(s, ErrorMessage.INVALID_PARAM);
    }

    public static <T extends Comparable<T>> T gte(T t1, T t2) {
        return gte(t1, t2, ErrorMessage.INVALID_PARAM);
    }

    @SafeVarargs
    public static <T> T in(T t, T... ts) {
        notNull(t, ErrorMessage.INVALID_PARAM);
        notEmpty(ts, ErrorMessage.INVALID_PARAM);
        isTrue(Arrays1.contains(ts, t), ErrorMessage.INVALID_PARAM);
        return t;
    }

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

    /**
     * 检查是否更新成功
     *
     * @param effect effect
     * @return effect
     */
    public static int version(int effect) {
        isTrue(effect > 0, ErrorMessage.DATA_MODIFIED);
        return effect;
    }

    /**
     * 检查路径是否合法化 即不包含 ./ ../
     *
     * @param path path
     */
    public static String checkNormalize(String path) {
        Valid.notBlank(path);
        Valid.isTrue(Files1.isNormalize(path), ErrorMessage.PATH_NOT_NORMALIZE);
        return Files1.getPath(path);
    }

}
