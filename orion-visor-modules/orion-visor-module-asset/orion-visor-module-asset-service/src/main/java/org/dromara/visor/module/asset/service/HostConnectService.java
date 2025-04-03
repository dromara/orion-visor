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
package org.dromara.visor.module.asset.service;

import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.dto.TerminalConnectDTO;
import org.dromara.visor.module.asset.entity.request.host.HostTestConnectRequest;

/**
 * 主机连接服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/12 23:54
 */
public interface HostConnectService {

    /**
     * 测试主机连接
     *
     * @param request request
     */
    void testHostConnect(HostTestConnectRequest request);

    /**
     * 获取 SSH 连接信息
     *
     * @param hostId hostId
     * @return session
     */
    TerminalConnectDTO getSshConnectInfo(Long hostId);

    /**
     * 使用用户配置获取 SSH 连接信息
     *
     * @param hostId hostId
     * @param userId userId
     * @return session
     */
    TerminalConnectDTO getSshConnectInfo(Long hostId, Long userId);

    /**
     * 使用用户配置获取 SSH 连接信息
     *
     * @param host   host
     * @param userId userId
     * @return session
     */
    TerminalConnectDTO getSshConnectInfo(HostDO host, Long userId);

}
