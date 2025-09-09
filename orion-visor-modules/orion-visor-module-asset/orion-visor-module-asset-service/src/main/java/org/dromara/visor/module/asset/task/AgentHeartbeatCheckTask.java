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
package org.dromara.visor.module.asset.task;

import org.dromara.visor.module.asset.service.HostAgentEndpointService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 探针心跳检查任务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 23:50
 */
@Component
public class AgentHeartbeatCheckTask {

    @Resource
    private HostAgentEndpointService hostAgentEndpointService;

    /**
     * 每分钟检测心跳
     */
    @Scheduled(initialDelay = 65000, fixedRate = 60000)
    public void checkHeartbeat() {
        hostAgentEndpointService.checkHeartbeat();
    }

}
