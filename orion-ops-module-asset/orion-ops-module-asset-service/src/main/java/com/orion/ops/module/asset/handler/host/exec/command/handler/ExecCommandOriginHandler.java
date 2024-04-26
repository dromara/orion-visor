package com.orion.ops.module.asset.handler.host.exec.command.handler;

import com.orion.lang.support.timeout.TimeoutChecker;
import com.orion.lang.support.timeout.TimeoutEndpoint;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandDTO;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandHostDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 命令执行器 原始日志输出
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 11:30
 */
@Slf4j
public class ExecCommandOriginHandler extends BaseExecCommandHandler {

    public ExecCommandOriginHandler(ExecCommandDTO execCommand, ExecCommandHostDTO execHostCommand, TimeoutChecker<TimeoutEndpoint> timeoutChecker) {
        super(execCommand, execHostCommand, timeoutChecker);
    }

}
