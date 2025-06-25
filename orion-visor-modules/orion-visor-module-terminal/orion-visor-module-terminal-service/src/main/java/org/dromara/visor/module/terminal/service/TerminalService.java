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
package org.dromara.visor.module.terminal.service;

import org.dromara.visor.module.terminal.entity.dto.TerminalAccessDTO;
import org.dromara.visor.module.terminal.entity.dto.TerminalTransferDTO;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalSessionAccessRequest;
import org.dromara.visor.module.terminal.entity.vo.TerminalThemeVO;

import java.util.List;

/**
 * 终端服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 14:22
 */
public interface TerminalService {

    /**
     * 获取终端主题
     *
     * @return themes
     */
    List<TerminalThemeVO> getTerminalThemes();

    /**
     * 获取终端访问 accessToken
     *
     * @param request request
     * @return accessToken
     */
    String getTerminalAccessToken(TerminalSessionAccessRequest request);

    /**
     * 获取终端传输 transferToken
     *
     * @return transferToken
     */
    String getTerminalTransferToken();

    /**
     * 通过 accessToken 获取终端访问信息
     *
     * @param token token
     * @return config
     */
    TerminalAccessDTO getAccessInfoByToken(String token);

    /**
     * 通过 transferToken 获取终端传输信息
     *
     * @param token token
     * @return config
     */
    TerminalTransferDTO getTransferInfoByToken(String token);

}
