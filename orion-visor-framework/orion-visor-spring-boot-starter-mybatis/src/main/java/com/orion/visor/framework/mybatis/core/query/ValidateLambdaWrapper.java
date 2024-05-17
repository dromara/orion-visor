package com.orion.visor.framework.mybatis.core.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;

import static com.orion.visor.framework.mybatis.core.query.Conditions.isIllegal;

/**
 * 有效性验证 wrapper
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 11:43
 */
public class ValidateLambdaWrapper<T> extends LambdaQueryWrapper<T> {

    public ValidateLambdaWrapper() {
    }

    public ValidateLambdaWrapper(T entity) {
        super(entity);
    }

    public ValidateLambdaWrapper(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public LambdaQueryWrapper<T> eq(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.eq(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> ne(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.ne(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> gt(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.gt(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> ge(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.ge(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> lt(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.lt(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> le(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.le(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> between(SFunction<T, ?> column, Object val1, Object val2) {
        if (isIllegal(val1, val2)) {
            return this;
        }
        return super.between(column, val1, val2);
    }

    @Override
    public LambdaQueryWrapper<T> notBetween(SFunction<T, ?> column, Object val1, Object val2) {
        if (isIllegal(val1, val2)) {
            return this;
        }
        return super.notBetween(column, val1, val2);
    }

    @Override
    public LambdaQueryWrapper<T> like(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.like(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> notLike(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.notLike(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> notLikeLeft(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.notLikeLeft(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> notLikeRight(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.notLikeRight(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> likeLeft(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.likeLeft(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> likeRight(SFunction<T, ?> column, Object val) {
        if (isIllegal(val)) {
            return this;
        }
        return super.likeRight(column, val);
    }

    @Override
    public LambdaQueryWrapper<T> in(SFunction<T, ?> column, Collection<?> coll) {
        if (isIllegal(coll)) {
            return this;
        }
        return super.in(column, coll);
    }

    @Override
    public LambdaQueryWrapper<T> in(SFunction<T, ?> column, Object... values) {
        if (isIllegal(values)) {
            return this;
        }
        return super.in(column, values);
    }

    @Override
    public LambdaQueryWrapper<T> notIn(SFunction<T, ?> column, Collection<?> coll) {
        if (isIllegal(coll)) {
            return this;
        }
        return super.notIn(column, coll);
    }

    @Override
    public LambdaQueryWrapper<T> notIn(SFunction<T, ?> column, Object... value) {
        if (isIllegal(value)) {
            return this;
        }
        return super.notIn(column, value);
    }

}
