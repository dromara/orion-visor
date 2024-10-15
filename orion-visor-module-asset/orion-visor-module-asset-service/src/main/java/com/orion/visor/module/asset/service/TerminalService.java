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

import com.orion.visor.module.asset.entity.domain.HostDO;
import com.orion.visor.module.asset.entity.dto.HostTerminalAccessDTO;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.entity.dto.HostTerminalTransferDTO;
import com.orion.visor.module.asset.entity.vo.HostTerminalThemeVO;

import java.util.List;

/**
 * 主机终端服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 14:22
 */
public interface TerminalService {

    /**
     * 获取主机终端主题
     *
     * @return themes
     */
    List<HostTerminalThemeVO> getTerminalThemes();

    /**
     * 获取主机终端访问 accessToken
     *
     * @return accessToken
     */
    String getTerminalAccessToken();

    /**
     * 获取主机终端传输 transferToken
     *
     * @return transferToken
     */
    String getTerminalTransferToken();

    /**
     * 通过 accessToken 获取主机终端访问信息
     *
     * @param token token
     * @return config
     */
    HostTerminalAccessDTO getAccessInfoByToken(String token);

    /**
     * 通过 transferToken 获取主机终端传输信息
     *
     * @param token token
     * @return config
     */
    HostTerminalTransferDTO getTransferInfoByToken(String token);

    /**
     * 获取连接信息
     *
     * @param hostId hostId
     * @return session
     */
    HostTerminalConnectDTO getTerminalConnectInfo(Long hostId);

    /**
     * 使用用户配置获取连接信息
     *
     * @param hostId hostId
     * @param userId userId
     * @return session
     */
    HostTerminalConnectDTO getTerminalConnectInfo(Long userId, Long hostId);

    /**
     * 使用用户配置获取连接信息
     *
     * @param host   host
     * @param userId userId
     * @return session
     */
    HostTerminalConnectDTO getTerminalConnectInfo(Long userId, HostDO host);

}
