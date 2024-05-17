package com.orion.visor.framework.biz.operator.log.core.service;

import com.orion.visor.framework.biz.operator.log.core.model.OperatorLogModel;

/**
 * 操作日志框架服务 委托类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 14:11
 */
public class OperatorLogFrameworkServiceDelegate implements OperatorLogFrameworkService {

    private final OperatorLogFrameworkService operatorLogFrameworkService;

    public OperatorLogFrameworkServiceDelegate(OperatorLogFrameworkService operatorLogFrameworkService) {
        this.operatorLogFrameworkService = operatorLogFrameworkService;
    }

    @Override
    public void insert(OperatorLogModel log) {
        operatorLogFrameworkService.insert(log);
    }

}
