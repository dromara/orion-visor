package com.orion.ops.module.infra.framework.service.impl;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import org.springframework.stereotype.Service;

/**
 * 操作日志包 实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 16:53
 */
@Service
public class OperatorLogFrameworkServiceImpl implements OperatorLogFrameworkService {

    @Override
    public void insert(OperatorLogModel log) {
        System.out.println(log);
    }

}
