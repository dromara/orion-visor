package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.dto.ExecLogTailDTO;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecLogTailRequest;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;

import javax.servlet.http.HttpServletResponse;

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
    ExecLogVO execCommand(ExecCommandRequest request);

    /**
     * 重新执行命令
     *
     * @param id id
     * @return result
     */
    ExecLogVO reExecCommand(Long id);

    /**
     * 中断命令执行
     *
     * @param logId logId
     */
    void interruptExec(Long logId);

    /**
     * 中断命令执行
     *
     * @param hostLogId hostLogId
     */
    void interruptHostExec(Long hostLogId);

    /**
     * 查看执行日志
     *
     * @param request request
     * @return token
     */
    String getExecLogTailToken(ExecLogTailRequest request);

    /**
     * 获取查看执行日志参数
     *
     * @param token token
     * @return log
     */
    ExecLogTailDTO getExecLogTailInfo(String token);

    /**
     * 下载执行日志文件
     *
     * @param id       id
     * @param response response
     */
    void downloadLogFile(Long id, HttpServletResponse response);

}
