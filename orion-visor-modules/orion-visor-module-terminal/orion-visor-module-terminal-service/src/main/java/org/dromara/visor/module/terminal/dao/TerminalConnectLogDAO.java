/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.terminal.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.terminal.entity.domain.TerminalConnectLogDO;
import org.dromara.visor.module.terminal.entity.po.TerminalConnectLogCountPO;

import java.util.Date;
import java.util.List;

/**
 * 终端连接日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Mapper
public interface TerminalConnectLogDAO extends IMapper<TerminalConnectLogDO> {

    /**
     * 查询最近连接的 hostId
     *
     * @param userId userId
     * @param type   type
     * @param limit  limit
     * @return hostId
     */
    default List<Long> selectLatestConnectHostId(Long userId, String type, Integer limit) {
        return this.of()
                .createWrapper(true)
                .select(TerminalConnectLogDO::getHostId)
                .eq(TerminalConnectLogDO::getUserId, userId)
                .eq(TerminalConnectLogDO::getType, type)
                .orderByDesc(TerminalConnectLogDO::getId)
                .then()
                .limit(limit)
                .list(TerminalConnectLogDO::getHostId);
    }

    /**
     * 查询终端连接日志用户数量
     *
     * @param userId    userId
     * @param startTime startTime
     * @param endTime   endTime
     * @return rows
     */
    List<TerminalConnectLogCountPO> selectConnectLogUserCount(@Param("userId") Long userId,
                                                              @Param("startTime") Date startTime,
                                                              @Param("endTime") Date endTime);

    /**
     * 通过 sessionId 查询
     *
     * @param sessionId sessionId
     * @return row
     */
    default TerminalConnectLogDO selectBySessionId(String sessionId) {
        return this.of()
                .createWrapper()
                .eq(TerminalConnectLogDO::getSessionId, sessionId)
                .orderByDesc(TerminalConnectLogDO::getId)
                .then()
                .getOne();
    }

}
