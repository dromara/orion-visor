package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.exec.ExecLogQueryRequest;
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
     * 查询批量执行日志
     *
     * @param id id
     * @return row
     */
    ExecLogVO getExecLogById(Long id);

    /**
     * 分页查询批量执行日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<ExecLogVO> getExecLogPage(ExecLogQueryRequest request);

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

}
