/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.framework.mybatis.core.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;

import static org.dromara.visor.framework.mybatis.core.query.Conditions.isIllegal;

/**
 * 复制 ValidateLambdaWrapper
 * 继承 ThenLambdaWrapper
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/7 15:26
 */
public class ThenValidateLambdaWrapper<T> extends ThenLambdaWrapper<T> {

    public ThenValidateLambdaWrapper(DataQuery<T> dataQuery) {
        super(dataQuery);
    }

    @Override
    public ThenLambdaWrapper<T> eq(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.eq(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> ne(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.ne(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> gt(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.gt(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> ge(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.ge(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> lt(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.lt(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> le(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.le(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> between(SFunction<T, ?> column, Object val1, Object val2) {
        if (isIllegal(val1, val2)) {
            return this;
        }
        return super.between(column, val1, val2);
    }

    @Override
    public ThenLambdaWrapper<T> notBetween(SFunction<T, ?> column, Object val1, Object val2) {
        if (isIllegal(val1, val2)) {
            return this;
        }
        return super.notBetween(column, val1, val2);
    }

    @Override
    public ThenLambdaWrapper<T> like(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.like(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> notLike(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.notLike(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> notLikeLeft(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.notLikeLeft(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> notLikeRight(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.notLikeRight(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> likeLeft(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.likeLeft(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> likeRight(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.likeRight(column, val);
    }

    @Override
    public ThenLambdaWrapper<T> in(SFunction<T, ?> column, Collection<?> coll) {
        if (isIllegal(coll)) {
            return this;
        }
        return super.in(column, coll);
    }

    @Override
    public ThenLambdaWrapper<T> in(SFunction<T, ?> column, Object... values) {
        if (isIllegal(values)) {
            return this;
        }
        return super.in(column, values);
    }

    @Override
    public ThenLambdaWrapper<T> notIn(SFunction<T, ?> column, Collection<?> coll) {
        if (isIllegal(coll)) {
            return this;
        }
        return super.notIn(column, coll);
    }

    @Override
    public ThenLambdaWrapper<T> notIn(SFunction<T, ?> column, Object... value) {
        if (isIllegal(value)) {
            return this;
        }
        return super.notIn(column, value);
    }

}
