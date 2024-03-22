package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.vo.ExecHostLogVO;

import java.util.List;

/**
 * 批量执行主机日志 服务类
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
public interface ExecHostLogService {

    /**
     * 查询全部批量执行主机日志
     *
     * @param logId logId
     * @return rows
     */
    List<ExecHostLogVO> getExecHostLogList(Long logId);

    /**
     * 删除批量执行主机日志
     *
     * @param logIdList logIdList
     * @return effect
     */
    Integer deleteExecHostLogByLogId(List<Long> logIdList);

    /**
     * 删除批量执行主机日志
     *
     * @param id id
     * @return effect
     */
    Integer deleteExecHostLogById(Long id);

}
