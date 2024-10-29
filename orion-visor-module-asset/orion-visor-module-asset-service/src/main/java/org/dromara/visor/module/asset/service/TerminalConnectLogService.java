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
package org.dromara.visor.module.asset.service;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.module.asset.entity.domain.TerminalConnectLogDO;
import org.dromara.visor.module.asset.entity.request.host.TerminalConnectLogClearRequest;
import org.dromara.visor.module.asset.entity.request.host.TerminalConnectLogCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.TerminalConnectLogQueryRequest;
import org.dromara.visor.module.asset.entity.vo.TerminalConnectLogVO;
import org.dromara.visor.module.asset.enums.TerminalConnectStatusEnum;
import org.dromara.visor.module.asset.enums.TerminalConnectTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 终端连接日志 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
public interface TerminalConnectLogService {

    /**
     * 创建终端连接日志
     *
     * @param type    type
     * @param request request
     * @return record
     */
    TerminalConnectLogDO create(TerminalConnectTypeEnum type, TerminalConnectLogCreateRequest request);

    /**
     * 分页查询终端连接日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<TerminalConnectLogVO> getTerminalConnectLogPage(TerminalConnectLogQueryRequest request);

    /**
     * 分页查询终端连接会话
     *
     * @param request request
     * @return rows
     */
    List<TerminalConnectLogVO> getTerminalConnectSessions(TerminalConnectLogQueryRequest request);

    /**
     * 更新连接状态
     *
     * @param id     id
     * @param status status
     * @param extra  extra
     * @return effect
     */
    Integer updateStatusById(Long id, TerminalConnectStatusEnum status, Map<String, Object> extra);

    /**
     * 查询用户最近连接的主机
     *
     * @param request request
     * @return hostId
     */
    Set<Long> getLatestConnectHostId(TerminalConnectLogQueryRequest request);

    /**
     * 删除终端连接日志
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteTerminalConnectLog(List<Long> idList);

    /**
     * 获取终端连接日志数量
     *
     * @param request request
     * @return count
     */
    Long getTerminalConnectLogCount(TerminalConnectLogQueryRequest request);

    /**
     * 清空终端连接日志
     *
     * @param request request
     * @return effect
     */
    Integer clearTerminalConnectLog(TerminalConnectLogClearRequest request);

    /**
     * 强制断开终端连接
     *
     * @param request request
     * @return effect
     */
    Integer forceOffline(TerminalConnectLogQueryRequest request);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<TerminalConnectLogDO> buildQueryWrapper(TerminalConnectLogQueryRequest request);

}
