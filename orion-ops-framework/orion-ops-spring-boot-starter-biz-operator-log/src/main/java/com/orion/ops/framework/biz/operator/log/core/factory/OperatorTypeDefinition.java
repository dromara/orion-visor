package com.orion.ops.framework.biz.operator.log.core.factory;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

/**
 * 操作类型定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 18:23
 */
public interface OperatorTypeDefinition {

    /**
     * 获取操作类型
     *
     * @return 操作类型
     */
    OperatorType[] types();

}
