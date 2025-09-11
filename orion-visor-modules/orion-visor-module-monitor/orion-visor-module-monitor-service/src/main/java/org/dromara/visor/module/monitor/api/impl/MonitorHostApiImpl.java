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
package org.dromara.visor.module.monitor.api.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.monitor.api.MonitorHostApi;
import org.dromara.visor.module.monitor.dao.MonitorHostDAO;
import org.dromara.visor.module.monitor.define.context.MonitorContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监控主机对外服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/19 15:48
 */
@Slf4j
@Service
public class MonitorHostApiImpl implements MonitorHostApi {

    @Resource
    private MonitorHostDAO monitorHostDAO;

    @Resource
    private MonitorContext monitorContext;

    @Override
    public void setAgentOffline(List<String> agentKeyList) {
        // 下线后删除指标
        agentKeyList.forEach(s -> monitorContext.setAgentMetrics(s, null));
    }

    @Override
    public Integer deleteByHostIdList(List<Long> hostIdList) {
        log.info("MonitorHostApi.deleteByHostIdList start hostIdList: {}", hostIdList);
        if (Lists.isEmpty(hostIdList)) {
            return Const.N_0;
        }
        // 通过 hostId 删除
        int effect = monitorHostDAO.deleteByHostIdList(hostIdList);
        log.info("MonitorHostApi.deleteByHostIdList finish effect: {}", effect);
        return effect;
    }

}
