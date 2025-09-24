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
package org.dromara.visor.module.monitor.service.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.monitor.dao.AlarmPolicyNotifyDAO;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyNotifyDO;
import org.dromara.visor.module.monitor.service.AlarmPolicyNotifyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 监控告警策略通知 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 10:04
 */
@Slf4j
@Service
public class AlarmPolicyNotifyServiceImpl implements AlarmPolicyNotifyService {

    @Resource
    private AlarmPolicyNotifyDAO alarmPolicyNotifyDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAlarmPolicyNotify(Long policyId, List<Long> notifyIdList) {
        // 删除
        alarmPolicyNotifyDAO.deleteByPolicyId(policyId);
        // 重新插入
        if (!Lists.isEmpty(notifyIdList)) {
            List<AlarmPolicyNotifyDO> records = notifyIdList.stream()
                    .map(notifyId -> new AlarmPolicyNotifyDO(policyId, notifyId))
                    .collect(Collectors.toList());
            alarmPolicyNotifyDAO.insertBatch(records);
        }
    }

    @Override
    public List<Long> getAlarmPolicyNotify(Long policyId) {
        return alarmPolicyNotifyDAO.of()
                .createWrapper()
                .select(AlarmPolicyNotifyDO::getNotifyId)
                .eq(AlarmPolicyNotifyDO::getPolicyId, policyId)
                .then()
                .list(AlarmPolicyNotifyDO::getNotifyId);
    }

}
