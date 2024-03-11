package com.orion.ops.module.asset.service.impl;

import com.orion.ops.module.asset.dao.ExecLogDAO;
import com.orion.ops.module.asset.entity.request.exec.ExecRequest;
import com.orion.ops.module.asset.service.ExecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 批量执行服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 12:03
 */
@Slf4j
@Service
public class ExecServiceImpl implements ExecService {

    @Resource
    private ExecLogDAO execLogDAO;

    @Override
    public void startExecCommand(ExecRequest request) {

    }

}
