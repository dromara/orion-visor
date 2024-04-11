package com.orion.ops.module.asset.service;

import java.util.List;

/**
 * 计划任务主机 服务类
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
public interface ExecJobHostService {

    /**
     * 设置任务主机
     *
     * @param jobId      jobId
     * @param hostIdList hostIdList
     */
    void setHostIdByJobId(Long jobId, List<Long> hostIdList);

    /**
     * 通过 hostId 获取 jobId
     *
     * @param jobId jobId
     * @return hostId
     */
    List<Long> getHostIdByJobId(Long jobId);

    /**
     * 通过 jobId 删除
     *
     * @param jobId jobId
     * @return effect
     */
    Integer deleteByJobId(Long jobId);

    /**
     * 通过 hostId 删除
     *
     * @param hostId hostId
     * @return effect
     */
    Integer deleteByHostId(Long hostId);

}
