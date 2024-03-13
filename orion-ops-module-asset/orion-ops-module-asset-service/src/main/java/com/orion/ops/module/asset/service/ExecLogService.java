package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.exec.ExecLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.ExecLogStatusVO;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;

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
     * 获取执行日志状态
     *
     * @param idList idList
     * @return status
     */
    ExecLogStatusVO getExecLogStatus(List<Long> idList);

    /**
     * 查询批量执行日志数量
     *
     * @param request request
     * @return count
     */
    Long queryExecLogCount(ExecLogQueryRequest request);

    /**
     * 删除执行日志
     *
     * @param id id
     * @return effect
     */
    Integer deleteExecLogById(Long id);

    /**
     * 批量删除批量执行日志
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteExecLogByIdList(List<Long> idList);

    /**
     * 清理执行日志
     *
     * @param request request
     * @return effect
     */
    Integer clearExecLog(ExecLogQueryRequest request);

}
