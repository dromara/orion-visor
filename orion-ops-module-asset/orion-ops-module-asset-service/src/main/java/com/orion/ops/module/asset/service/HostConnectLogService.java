package com.orion.ops.module.asset.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostConnectLogVO;
import com.orion.ops.module.asset.enums.HostConnectStatusEnum;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 主机连接日志 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
public interface HostConnectLogService {

    /**
     * 创建
     *
     * @param type    type
     * @param request request
     */
    void create(HostConnectTypeEnum type, HostConnectLogCreateRequest request);

    /**
     * 分页查询主机连接日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<HostConnectLogVO> getHostConnectLogPage(HostConnectLogQueryRequest request);

    /**
     * 更新连接状态
     *
     * @param token  token
     * @param status status
     * @param extra  extra
     * @return effect
     */
    Integer updateStatusByToken(String token, HostConnectStatusEnum status, Map<String, Object> extra);

    /**
     * 查询用户最近连接的主机
     *
     * @param request request
     * @return hostId
     */
    List<Long> getLatestConnectHostId(HostConnectLogQueryRequest request);

    /**
     * 查询用户最近连接的主机
     *
     * @param type   type
     * @param userId userId
     * @return hostId
     */
    Future<List<Long>> getLatestConnectHostIdAsync(HostConnectTypeEnum type, Long userId);

    /**
     * 删除主机连接日志
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteHostConnectLog(List<Long> idList);

    /**
     * 获取主机连接日志数量
     *
     * @param request request
     * @return count
     */
    Long getHostConnectLogCount(HostConnectLogQueryRequest request);

    /**
     * 清空主机连接日志
     *
     * @param request request
     * @return effect
     */
    Integer clearHostConnectLog(HostConnectLogQueryRequest request);

    /**
     * 强制断开主机连接
     *
     * @param request request
     * @return effect
     */
    Integer forceOffline(HostConnectLogQueryRequest request);

}
