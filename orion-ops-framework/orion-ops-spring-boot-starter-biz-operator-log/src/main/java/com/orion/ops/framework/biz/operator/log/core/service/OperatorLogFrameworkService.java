package com.orion.ops.framework.biz.operator.log.core.service;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;

/**
 * 操作日志框架服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 11:26
 */
public interface OperatorLogFrameworkService {

    /**
     * 记录日志
     *
     * @param log log
     */
    void insert(OperatorLogModel log);

}
