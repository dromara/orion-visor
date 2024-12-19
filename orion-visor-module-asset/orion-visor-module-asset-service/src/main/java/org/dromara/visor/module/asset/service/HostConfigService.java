/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import org.dromara.visor.framework.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.enums.HostTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * 主机配置 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
public interface HostConfigService {

    /**
     * 获取主机配置
     *
     * @param id   id
     * @param type type
     * @param <T>  T
     * @return host
     */
    <T extends GenericsDataModel> T getHostConfig(Long id, HostTypeEnum type);

    /**
     * 构建主机配置
     *
     * @param host host
     * @param type type
     * @param <T>  T
     * @return host
     */
    <T extends GenericsDataModel> T buildHostConfig(HostDO host, HostTypeEnum type);

    /**
     * 获取主机配置
     *
     * @param idList idList
     * @param type   type
     * @param <T>    T
     * @return config
     */
    <T extends GenericsDataModel> Map<Long, T> getHostConfigMap(List<Long> idList, HostTypeEnum type);

    /**
     * 构建主机配置
     *
     * @param hostList hostList
     * @param type     type
     * @param <T>      T
     * @return config
     */
    <T extends GenericsDataModel> Map<Long, T> buildHostConfigMap(List<HostDO> hostList, HostTypeEnum type);

}
