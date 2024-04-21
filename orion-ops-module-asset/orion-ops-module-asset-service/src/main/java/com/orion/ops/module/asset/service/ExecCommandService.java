package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.dto.ExecCommandExecDTO;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;

/**
 * 批量执行服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 12:02
 */
public interface ExecCommandService {

    /**
     * 批量执行命令
     *
     * @param request request
     * @return result
     */
    ExecLogVO execCommand(ExecCommandRequest request);

    /**
     * 批量执行命令
     *
     * @param request request
     * @return result
     */
    ExecLogVO execCommandWithSource(ExecCommandExecDTO request);

    /**
     * 重新执行命令
     *
     * @param logId logId
     * @return result
     */
    ExecLogVO reExecCommand(Long logId);

}
