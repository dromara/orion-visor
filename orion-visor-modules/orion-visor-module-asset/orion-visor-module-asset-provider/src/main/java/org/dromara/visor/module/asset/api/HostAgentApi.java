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
package org.dromara.visor.module.asset.api;

import org.dromara.visor.module.asset.entity.dto.host.HostAgentLogDTO;

import java.util.List;
import java.util.Map;

/**
 * 主机探针对外服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/1 21:14
 */
public interface HostAgentApi {

    /**
     * 查询主机探针安装日志
     *
     * @param hostIdList hostIdList
     * @return rows
     */
    List<HostAgentLogDTO> selectAgentInstallLog(List<Long> hostIdList);

    /**
     * 获取缓存名称
     *
     * @param agentKeyList agentKeyList
     * @return nameMap
     */
    Map<String, String> getCacheNameByAgentKey(List<String> agentKeyList);

    /**
     * 获取探针版本
     *
     * @return version
     */
    String getAgentVersion();

}
