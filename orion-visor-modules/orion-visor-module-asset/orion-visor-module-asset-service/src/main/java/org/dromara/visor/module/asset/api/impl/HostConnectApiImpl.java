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
package org.dromara.visor.module.asset.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.session.config.RdpConnectConfig;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.config.VncConnectConfig;
import org.dromara.visor.module.asset.api.HostConnectApi;
import org.dromara.visor.module.asset.convert.HostProviderConvert;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.asset.service.HostConnectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 主机连接 对外服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/13 0:03
 */
@Slf4j
@Service
public class HostConnectApiImpl implements HostConnectApi {

    @Resource
    private HostConnectService hostConnectService;

    @Override
    public SshConnectConfig getSshConnectConfig(Long hostId) {
        return hostConnectService.getSshConnectConfig(hostId);
    }

    @Override
    public SshConnectConfig getSshConnectConfig(Long hostId, Long userId) {
        return hostConnectService.getSshConnectConfig(hostId, userId);
    }

    @Override
    public SshConnectConfig getSshConnectConfig(HostDTO host, Long userId) {
        return hostConnectService.getSshConnectConfig(HostProviderConvert.MAPPER.to(host), userId);
    }

    @Override
    public RdpConnectConfig getRdpConnectConfig(Long hostId) {
        return hostConnectService.getRdpConnectConfig(hostId);
    }

    @Override
    public RdpConnectConfig getRdpConnectConfig(Long hostId, Long userId) {
        return hostConnectService.getRdpConnectConfig(hostId, userId);
    }

    @Override
    public RdpConnectConfig getRdpConnectConfig(HostDTO host, Long userId) {
        return hostConnectService.getRdpConnectConfig(HostProviderConvert.MAPPER.to(host), userId);
    }

    @Override
    public VncConnectConfig getVncConnectConfig(Long hostId) {
        return hostConnectService.getVncConnectConfig(hostId);
    }

    @Override
    public VncConnectConfig getVncConnectConfig(Long hostId, Long userId) {
        return hostConnectService.getVncConnectConfig(hostId, userId);
    }

    @Override
    public VncConnectConfig getVncConnectConfig(HostDTO host, Long userId) {
        return hostConnectService.getVncConnectConfig(HostProviderConvert.MAPPER.to(host), userId);
    }

}
