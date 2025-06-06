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

import org.dromara.visor.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.module.asset.entity.request.host.HostConfigQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostConfigUpdateRequest;

import java.util.List;

/**
 * 主机配置 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
public interface HostConfigService {

    /**
     * 更新主机配置
     *
     * @param request request
     * @return effect
     */
    Integer updateHostConfig(HostConfigUpdateRequest request);

    /**
     * 复制主机配置
     *
     * @param originId originId
     * @param newId    newId
     * @param types    types
     */
    void copyHostConfig(Long originId, Long newId, List<String> types);

    /**
     * 获取主机配置
     *
     * @param hostId hostId
     * @param type   type
     * @param <T>    T
     * @return config
     */
    <T extends GenericsDataModel> T getHostConfig(Long hostId, String type);

    /**
     * 查询主机配置
     *
     * @param request request
     * @return config
     */
    <T extends GenericsDataModel> T getHostConfigView(HostConfigQueryRequest request);

}
