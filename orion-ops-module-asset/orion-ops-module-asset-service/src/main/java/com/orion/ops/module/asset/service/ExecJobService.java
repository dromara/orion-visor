package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.exec.ExecJobCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobVO;

import java.util.List;

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
     * 根据条件更新计划执行任务
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateExecJob(ExecJobQueryRequest query, ExecJobUpdateRequest update);

    /**
     * 查询计划执行任务
     *
     * @param id id
     * @return row
     */
    ExecJobVO getExecJobById(Long id);

    /**
     * 批量查询计划执行任务
     *
     * @param idList idList
     * @return rows
     */
    List<ExecJobVO> getExecJobByIdList(List<Long> idList);

    /**
     * 查询全部计划执行任务
     *
     * @param request request
     * @return rows
     */
    List<ExecJobVO> getExecJobList(ExecJobQueryRequest request);

    /**
     * 查询计划执行任务数量
     *
     * @param request request
     * @return count
     */
    Long getExecJobCount(ExecJobQueryRequest request);

    /**
     * 分页查询计划执行任务
     *
     * @param request request
     * @return rows
     */
    DataGrid<ExecJobVO> getExecJobPage(ExecJobQueryRequest request);

    /**
     * 删除计划执行任务
     *
     * @param id id
     * @return effect
     */
    Integer deleteExecJobById(Long id);

    /**
     * 批量删除计划执行任务
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteExecJobByIdList(List<Long> idList);

    /**
     * 根据条件删除计划执行任务
     *
     * @param request request
     * @return effect
     */
    Integer deleteExecJob(ExecJobQueryRequest request);

}
