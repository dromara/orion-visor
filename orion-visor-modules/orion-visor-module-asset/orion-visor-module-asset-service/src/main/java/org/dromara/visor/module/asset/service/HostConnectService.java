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

import org.dromara.visor.common.session.config.RdpConnectConfig;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.config.VncConnectConfig;
import org.dromara.visor.module.asset.entity.domain.HostDO;
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
     * 获取 SSH 连接配置
     *
     * @param hostId hostId
     * @return session
     */
    SshConnectConfig getSshConnectConfig(Long hostId);

    /**
     * 使用用户配置获取 SSH 连接配置
     *
     * @param hostId hostId
     * @param userId userId
     * @return session
     */
    SshConnectConfig getSshConnectConfig(Long hostId, Long userId);

    /**
     * 使用用户配置获取 SSH 连接配置
     *
     * @param host   host
     * @param userId userId
     * @return session
     */
    SshConnectConfig getSshConnectConfig(HostDO host, Long userId);

    /**
     * 获取 RDP 连接配置
     *
     * @param hostId hostId
     * @return session
     */
    RdpConnectConfig getRdpConnectConfig(Long hostId);

    /**
     * 使用用户配置获取 RDP 连接配置
     *
     * @param hostId hostId
     * @param userId userId
     * @return session
     */
    RdpConnectConfig getRdpConnectConfig(Long hostId, Long userId);

    /**
     * 使用用户配置获取 RDP 连接配置
     *
     * @param host   host
     * @param userId userId
     * @return session
     */
    RdpConnectConfig getRdpConnectConfig(HostDO host, Long userId);

    /**
     * 获取 VNC 连接配置
     *
     * @param hostId hostId
     * @return session
     */
    VncConnectConfig getVncConnectConfig(Long hostId);

    /**
     * 使用用户配置获取 VNC 连接配置
     *
     * @param hostId hostId
     * @param userId userId
     * @return session
     */
    VncConnectConfig getVncConnectConfig(Long hostId, Long userId);

    /**
     * 使用用户配置获取 VNC 连接配置
     *
     * @param host   host
     * @param userId userId
     * @return session
     */
    VncConnectConfig getVncConnectConfig(HostDO host, Long userId);

}
