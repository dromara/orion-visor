package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecInterruptRequest;
import com.orion.ops.module.asset.entity.vo.ExecVO;

/**
 * 批量执行服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 12:02
 */
public interface ExecService {

    /**
     * 批量执行命令
     *
     * @param request request
     * @return result
     */
    ExecVO execCommand(ExecCommandRequest request);

    /**
     * 中断命令执行
     *
     * @param request request
     */
    void interruptCommand(ExecInterruptRequest request);

}
