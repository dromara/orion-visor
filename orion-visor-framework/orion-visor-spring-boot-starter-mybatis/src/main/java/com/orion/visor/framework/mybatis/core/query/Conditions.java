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
package com.orion.visor.framework.mybatis.core.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.utils.Valid;

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
     * 获取 LambdaQueryWrapper
     *
     * @param <T> T
     * @return wrapper
     */
    public static <T> LambdaQueryWrapper<T> lambda() {
        return new LambdaQueryWrapper<>();
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

    /**
     * 有效性验证
     *
     * @param objects objects
     * @return isValidate
     */
    public static boolean isIllegal(Object... objects) {
        for (Object object : objects) {
            // 非 null 检查
            if (object == null) {
                return true;
            }
            // 字符串 非空校验
            if (object instanceof String) {
                return ((String) object).isEmpty();
            }
            // 集合 非空校验
            if (object instanceof Collection<?>) {
                return ((Collection<?>) object).isEmpty();
            }
        }
        return false;
    }

}
