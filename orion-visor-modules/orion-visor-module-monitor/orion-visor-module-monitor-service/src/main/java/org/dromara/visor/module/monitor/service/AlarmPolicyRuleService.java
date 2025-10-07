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
package org.dromara.visor.module.monitor.service;

import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleCreateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleUpdateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleUpdateSwitchRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmPolicyRuleVO;

import java.util.List;

/**
 * 监控告警规则 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 13:39
 */
public interface AlarmPolicyRuleService {

    /**
     * 创建监控告警规则
     *
     * @param request request
     * @return id
     */
    Long createAlarmPolicyRule(AlarmPolicyRuleCreateRequest request);

    /**
     * 更新监控告警规则
     *
     * @param request request
     * @return effect
     */
    Integer updateAlarmPolicyRuleById(AlarmPolicyRuleUpdateRequest request);

    /**
     * 更新监控告警规则开关
     *
     * @param request request
     * @return effect
     */
    Integer updateAlarmPolicyRuleSwitch(AlarmPolicyRuleUpdateSwitchRequest request);

    /**
     * 复制监控告警规则
     *
     * @param policyId    policyId
     * @param newPolicyId newPolicyId
     */
    void copyAlarmPolicyRule(Long policyId, Long newPolicyId);

    /**
     * 查询全部监控告警规则
     *
     * @param policyId    policyId
     * @param measurement measurement
     * @return rows
     */
    List<AlarmPolicyRuleVO> getAlarmPolicyRuleList(Long policyId, String measurement);

    /**
     * 通过 metricsId 删除监控告警规则
     *
     * @param metricsId metricsId
     * @return effect
     */
    Integer deleteByMetricsId(Long metricsId);

    /**
     * 通过 policyId 删除监控告警规则
     *
     * @param policyId policyId
     * @return effect
     */
    Integer deleteByPolicyId(Long policyId);

    /**
     * 删除监控告警规则
     *
     * @param id id
     * @return effect
     */
    Integer deleteAlarmPolicyRuleById(Long id);

}
