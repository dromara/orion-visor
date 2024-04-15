package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.exec.*;
import com.orion.ops.module.asset.entity.vo.ExecJobVO;

import java.util.List;

/**
 * 计划任务 服务类
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
public interface ExecJobService {

    /**
     * 创建计划任务
     *
     * @param request request
     * @return id
     */
    Long createExecJob(ExecJobCreateRequest request);

    /**
     * 更新计划任务
     *
     * @param request request
     * @return effect
     */
    Integer updateExecJobById(ExecJobUpdateRequest request);

    /**
     * 更新计划任务状态
     *
     * @param request request
     * @return effect
     */
    Integer updateExecJobStatus(ExecJobUpdateStatusRequest request);

    /**
     * 查询计划任务
     *
     * @param id id
     * @return row
     */
    ExecJobVO getExecJobById(Long id);

    /**
     * 查询全部计划任务
     *
     * @return rows
     */
    List<ExecJobVO> getExecJobList();

    /**
     * 分页查询计划任务
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
     * 删除计划任务
     *
     * @param id id
     * @return effect
     */
    Integer deleteExecJobById(Long id);

    /**
     * 手动触发任务
     *
     * @param id id
     */
    void manualTriggerExecJob(Long id);

    /**
     * 触发任务
     *
     * @param request request
     */
    void triggerExecJob(ExecJobTriggerRequest request);

}
