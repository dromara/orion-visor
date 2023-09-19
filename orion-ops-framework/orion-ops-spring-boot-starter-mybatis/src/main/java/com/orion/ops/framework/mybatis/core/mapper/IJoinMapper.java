package com.orion.ops.framework.mybatis.core.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * 通用 join mapper
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/18 11:51
 */
public interface IJoinMapper<T> extends IMapper<T>, MPJBaseMapper<T> {

    /**
     * 获取 MPJLambdaWrapper 对象
     *
     * @return 获取 wrapper
     */
    default MPJLambdaWrapper<T> join() {
        return JoinWrappers.lambda();
    }

}
