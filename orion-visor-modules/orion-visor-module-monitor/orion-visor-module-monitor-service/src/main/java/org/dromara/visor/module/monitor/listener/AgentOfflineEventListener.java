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
package org.dromara.visor.module.monitor.listener;

import org.dromara.visor.module.asset.entity.event.AgentOfflineEvent;
import org.dromara.visor.module.monitor.context.MonitorAgentContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * agent 下线事件监听器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/19 21:04
 */
@Component
public class AgentOfflineEventListener implements ApplicationListener<AgentOfflineEvent> {

    @Resource
    private MonitorAgentContext monitorAgentContext;

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(AgentOfflineEvent event) {
        List<String> agentKeys = (List<String>) event.getSource();
        agentKeys.forEach(monitorAgentContext::setAgentOffline);
    }

}
