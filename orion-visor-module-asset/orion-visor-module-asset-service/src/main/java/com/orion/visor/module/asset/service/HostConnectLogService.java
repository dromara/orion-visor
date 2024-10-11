/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.module.asset.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.asset.entity.domain.HostConnectLogDO;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogClearRequest;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.visor.module.asset.entity.vo.HostConnectLogVO;
import com.orion.visor.module.asset.enums.HostConnectStatusEnum;
import com.orion.visor.module.asset.enums.HostConnectTypeEnum;

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
     * 创建主机连接日志
     *
     * @param type    type
     * @param request request
     * @return record
     */
    HostConnectLogDO create(HostConnectTypeEnum type, HostConnectLogCreateRequest request);

    /**
     * 分页查询主机连接日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<HostConnectLogVO> getHostConnectLogPage(HostConnectLogQueryRequest request);

    /**
     * 分页查询主机连接会话
     *
     * @param request request
     * @return rows
     */
    List<HostConnectLogVO> getHostConnectSessions(HostConnectLogQueryRequest request);

    /**
     * 更新连接状态
     *
     * @param id     id
     * @param status status
     * @param extra  extra
     * @return effect
     */
    Integer updateStatusById(Long id, HostConnectStatusEnum status, Map<String, Object> extra);

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
    Integer clearHostConnectLog(HostConnectLogClearRequest request);

    /**
     * 强制断开主机连接
     *
     * @param request request
     * @return effect
     */
    Integer forceOffline(HostConnectLogQueryRequest request);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<HostConnectLogDO> buildQueryWrapper(HostConnectLogQueryRequest request);

}
