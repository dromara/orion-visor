package com.orion.ops.module.asset.handler.host.exec.command;

import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandDTO;
import com.orion.ops.module.asset.handler.host.exec.command.handler.ExecTaskHandler;

/**
 * 批量执行命令执行器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 11:10
 */
public class ExecTaskExecutors {

    /**
     * 执行命令
     *
     * @param command command
     */
    public static void start(ExecCommandDTO command) {
        AssetThreadPools.EXEC_TASK.execute(new ExecTaskHandler(command));
    }

}
