package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostCreateRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostQueryRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecJobHostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.ExecJobHostVO;

import java.util.List;

/**
 * 计划执行任务主机 服务类
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
public interface ExecJobHostService {

    /**
     * 创建计划执行任务主机
     *
     * @param request request
     * @return id
     */
    Long createExecJobHost(ExecJobHostCreateRequest request);

    /**
     * 更新计划执行任务主机
     *
     * @param request request
     * @return effect
     */
    Integer updateExecJobHostById(ExecJobHostUpdateRequest request);

    /**
     * 根据条件更新计划执行任务主机
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateExecJobHost(ExecJobHostQueryRequest query, ExecJobHostUpdateRequest update);

    /**
     * 查询计划执行任务主机
     *
     * @param id id
     * @return row
     */
    ExecJobHostVO getExecJobHostById(Long id);

    /**
     * 批量查询计划执行任务主机
     *
     * @param idList idList
     * @return rows
     */
    List<ExecJobHostVO> getExecJobHostByIdList(List<Long> idList);

    /**
     * 查询全部计划执行任务主机
     *
     * @param request request
     * @return rows
     */
    List<ExecJobHostVO> getExecJobHostList(ExecJobHostQueryRequest request);

    /**
     * 查询计划执行任务主机数量
     *
     * @param request request
     * @return count
     */
    Long getExecJobHostCount(ExecJobHostQueryRequest request);

    /**
     * 分页查询计划执行任务主机
     *
     * @param request request
     * @return rows
     */
    DataGrid<ExecJobHostVO> getExecJobHostPage(ExecJobHostQueryRequest request);

    /**
     * 删除计划执行任务主机
     *
     * @param id id
     * @return effect
     */
    Integer deleteExecJobHostById(Long id);

    /**
     * 批量删除计划执行任务主机
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteExecJobHostByIdList(List<Long> idList);

    /**
     * 根据条件删除计划执行任务主机
     *
     * @param request request
     * @return effect
     */
    Integer deleteExecJobHost(ExecJobHostQueryRequest request);

}
