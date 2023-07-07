package com.orion.ops.framework.mybatis.core.type;

import org.apache.ibatis.type.TypeHandler;

/**
 * mybatis 类型转换
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 10:38
 */
public interface ITypeHandler<P, R> extends TypeHandler<R> {

    /**
     * 数据类型转换
     *
     * @param param param
     * @return result
     */
    R getResult(P param);

}
