package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.dto.ExecLogTailDTO;
import com.orion.ops.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecLogTailRequest;
import com.orion.ops.module.asset.entity.vo.ExecLogStatusVO;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 批量执行日志 服务类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
public interface ExecLogService {

    /**
     * 分页查询批量执行日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<ExecLogVO> getExecLogPage(ExecLogQueryRequest request);

    /**
     * 获取执行日志
     *
     * @param id     id
     * @param source source
     * @return row
     */
    ExecLogVO getExecLog(Long id, String source);

    /**
     * 获取执行历史
     *
     * @param request request
     * @return history
     */
    List<ExecLogVO> getExecHistory(ExecLogQueryRequest request);

    /**
     * 获取执行日志状态
     *
     * @param idList idList
     * @param source source
     * @return status
     */
    ExecLogStatusVO getExecLogStatus(List<Long> idList, String source);

    /**
     * 删除执行日志
     *
     * @param id     id
     * @param source source
     * @return effect
     */
    Integer deleteExecLogById(Long id, String source);

    /**
     * 批量删除批量执行日志
     *
     * @param idList idList
     * @param source source
     * @return effect
     */
    Integer deleteExecLogByIdList(List<Long> idList, String source);

    /**
     * 查询批量执行日志数量
     *
     * @param request request
     * @return count
     */
    Long queryExecLogCount(ExecLogQueryRequest request);

    /**
     * 清理执行日志
     *
     * @param request request
     * @return effect
     */
    Integer clearExecLog(ExecLogQueryRequest request);

    /**
     * 中断命令执行
     *
     * @param logId  logId
     * @param source source
     */
    void interruptExec(Long logId, String source);

    /**
     * 中断命令执行
     *
     * @param hostLogId hostLogId
     * @param source    source
     */
    void interruptHostExec(Long hostLogId, String source);

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
     * @param source   source
     * @param response response
     */
    void downloadLogFile(Long id, String source, HttpServletResponse response);

}
