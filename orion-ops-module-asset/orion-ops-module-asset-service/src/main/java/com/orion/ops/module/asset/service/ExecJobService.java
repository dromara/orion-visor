package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.exec.ExecJobCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobUpdateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobUpdateStatusRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobVO;

/**
 * 计划执行任务 服务类
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
public interface ExecJobService {

    /**
     * 创建计划执行任务
     *
     * @param request request
     * @return id
     */
    Long createExecJob(ExecJobCreateRequest request);

    /**
     * 更新计划执行任务
     *
     * @param request request
     * @return effect
     */
    Integer updateExecJobById(ExecJobUpdateRequest request);

    /**
     * 更新计划执行任务状态
     *
     * @param request request
     * @return effect
     */
    Integer updateExecJobStatus(ExecJobUpdateStatusRequest request);

    /**
     * 查询计划执行任务
     *
     * @param id id
     * @return row
     */
    ExecJobVO getExecJobById(Long id);

    /**
     * 分页查询计划执行任务
     *
     * @param request request
     * @return rows
     */
    DataGrid<ExecJobVO> getExecJobPage(ExecJobQueryRequest request);

    /**
     * 获取下一个执行序列
     *
     * @param id id
     * @return seq
     */
    Integer getNextExecSeq(Long id);

    /**
     * 删除计划执行任务
     *
     * @param id id
     * @return effect
     */
    Integer deleteExecJobById(Long id);

}
