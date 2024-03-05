package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.host.HostSftpLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostSftpLogVO;

import java.util.List;

/**
 * SFTP 操作日志 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
public interface HostSftpLogService {

    /**
     * 分页查询 SFTP 操作日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<HostSftpLogVO> getHostSftpLogPage(HostSftpLogQueryRequest request);

    /**
     * 删除 SFTP 操作日志
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteHostSftpLog(List<Long> idList);

}
