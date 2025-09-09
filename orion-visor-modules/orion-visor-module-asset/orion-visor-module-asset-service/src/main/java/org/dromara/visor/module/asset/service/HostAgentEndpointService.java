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

import org.dromara.visor.module.asset.entity.vo.HostOnlineAgentConfigVO;

/**
 * 主机探针端点 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/27 16:06
 */
public interface HostAgentEndpointService {

    /**
     * 设置探针已上线
     *
     * @param agentKey agentKey
     * @param version  version
     * @return config
     */
    HostOnlineAgentConfigVO setAgentOnline(String agentKey, String version);

    /**
     * 设置探针已下线
     *
     * @param agentKey agentKey
     */
    void setAgentOffline(String agentKey);

    /**
     * 设置探针心跳
     *
     * @param agentKey agentKey
     */
    void setAgentHeartbeat(String agentKey);

    /**
     * 检查心跳
     */
    void checkHeartbeat();

}
