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

import com.orion.visor.framework.common.handler.data.model.GenericsDataModel;
import com.orion.visor.module.asset.entity.request.host.HostExtraQueryRequest;
import com.orion.visor.module.asset.entity.request.host.HostExtraUpdateRequest;
import com.orion.visor.module.asset.enums.HostExtraItemEnum;

import java.util.Map;

/**
 * 主机拓展信息 服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/20 12:04
 */
public interface HostExtraService {

    /**
     * 获取主机额外配置
     *
     * @param hostId hostId
     * @param item   item
     * @return extra
     */
    Map<String, Object> getHostExtra(Long hostId, String item);

    /**
     * 获取主机额外配置
     *
     * @param userId userId
     * @param hostId hostId
     * @param item   item
     * @param <T>    T
     * @return extra
     */
    <T extends GenericsDataModel> T getHostExtra(Long userId, Long hostId, HostExtraItemEnum item);

    /**
     * 获取多个主机拓展信息
     *
     * @param request request
     * @return type:extra
     */
    Map<String, Map<String, Object>> getHostExtraList(HostExtraQueryRequest request);

    /**
     * 修改主机拓展信息
     *
     * @param request request
     * @return effect
     */
    Integer updateHostExtra(HostExtraUpdateRequest request);

}
