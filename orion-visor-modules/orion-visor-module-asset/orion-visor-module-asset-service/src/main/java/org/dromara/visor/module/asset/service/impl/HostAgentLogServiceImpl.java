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
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.collect.Lists;
import org.dromara.visor.module.asset.convert.HostAgentLogConvert;
import org.dromara.visor.module.asset.dao.HostAgentLogDAO;
import org.dromara.visor.module.asset.entity.domain.HostAgentLogDO;
import org.dromara.visor.module.asset.entity.vo.HostAgentLogVO;
import org.dromara.visor.module.asset.entity.vo.HostAgentStatusVO;
import org.dromara.visor.module.asset.enums.AgentLogStatusEnum;
import org.dromara.visor.module.asset.service.HostAgentLogService;
import org.dromara.visor.module.asset.service.HostAgentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 主机探针日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/2 16:31
 */
@Service
public class HostAgentLogServiceImpl implements HostAgentLogService {

    @Resource
    private HostAgentLogDAO hostAgentLogDAO;

    @Resource
    private HostAgentService hostAgentService;

    @Override
    public HostAgentLogDO selectById(Long id) {
        return hostAgentLogDAO.selectById(id);
    }

    @Override
    public List<HostAgentLogVO> getAgentInstallLogStatus(List<Long> idList) {
        if (Lists.isEmpty(idList)) {
            return Lists.empty();
        }
        // 查询日志
        List<HostAgentLogVO> records = hostAgentLogDAO.selectBatchIds(idList)
                .stream()
                .map(HostAgentLogConvert.MAPPER::to)
                .collect(Collectors.toList());
        // 如果是已完成需要查询
        List<HostAgentLogVO> successRecords = records.stream()
                .filter(s -> AgentLogStatusEnum.SUCCESS.name().equals(s.getStatus()))
                .collect(Collectors.toList());
        if (!successRecords.isEmpty()) {
            // 完成后查询主机信息
            List<Long> hostIdList = successRecords.stream()
                    .map(HostAgentLogVO::getHostId)
                    .distinct()
                    .collect(Collectors.toList());
            // 查询状态信息
            Map<Long, HostAgentStatusVO> agentStatusMap = hostAgentService.getAgentStatus(hostIdList)
                    .stream()
                    .collect(Collectors.toMap(HostAgentStatusVO::getId,
                            Function.identity(),
                            Functions.right()));
            // 设置状态信息
            for (HostAgentLogVO successRecord : successRecords) {
                successRecord.setAgentStatus(agentStatusMap.get(successRecord.getHostId()));
            }
        }
        return records;
    }

    @Override
    public void updateStatus(Long id, String status, String message) {
        HostAgentLogDO update = HostAgentLogDO.builder()
                .id(id)
                .status(status)
                .message(message)
                .build();
        hostAgentLogDAO.updateById(update);
    }

}
