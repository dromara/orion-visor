package com.orion.ops.module.infra.framework.service.impl;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.ops.module.infra.service.OperatorLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 操作日志包 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 16:53
 */
@Service
public class OperatorLogFrameworkServiceImpl implements OperatorLogFrameworkService {

    @Resource
    private OperatorLogService operatorLogService;

    @Override
    public void insert(OperatorLogModel log) {
        operatorLogService.addOperatorLog(log);
    }

}
