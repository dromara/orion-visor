package com.orion.ops.framework.mybatis.core.query;

/**
 * 链式操作
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/7 14:57
 */
public interface Then<T> {

    /**
     * 获取
     *
     * @return T
     */
    T then();

}
