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
package org.dromara.visor.module.monitor.handler.alarm;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.enums.BooleanBit;
import org.dromara.visor.module.asset.api.HostAgentApi;
import org.dromara.visor.module.asset.entity.dto.host.HostBaseDTO;
import org.dromara.visor.module.monitor.convert.AlarmEventConvert;
import org.dromara.visor.module.monitor.entity.domain.AlarmEventDO;
import org.dromara.visor.module.monitor.entity.dto.*;
import org.dromara.visor.module.monitor.enums.AlarmEventSourceTypeEnum;
import org.dromara.visor.module.monitor.enums.AlarmHandleStatusEnum;
import org.dromara.visor.module.monitor.enums.AlarmSwitchEnum;
import org.dromara.visor.module.monitor.enums.MetricsUnitEnum;
import org.dromara.visor.module.monitor.handler.alarm.model.AlarmEngineRule;
import org.dromara.visor.module.monitor.handler.alarm.model.HostAlarmSourceInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 告警引擎
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/21 17:26
 */
@Slf4j
@Component("metricsAlarmEngine")
public class MetricsAlarmEngine extends BaseAlarmEngine {

    @Resource
    protected HostAgentApi hostAgentApi;

    @Override
    protected Long getAlarmPolicyId(String agentKey) {
        // 获取主机信息
        MonitorHostContextDTO monitorHost = monitorAgentContext.getMonitorHost(agentKey);
        if (monitorHost == null) {
            return null;
        }
        // 检查策略是否开启
        Long policyId = monitorHost.getPolicyId();
        if (policyId == null || AlarmSwitchEnum.isOff(monitorHost.getAlarmSwitch())) {
            return null;
        }
        return policyId;
    }

    @Override
    protected AlarmEventTriggerDTO createAlarmEvent(String agentKey,
                                                    Long alarmTimestamp,
                                                    AgentMetricsDTO agentMetrics,
                                                    BigDecimal metricsValue,
                                                    AlarmEngineRule rule) {
        MonitorHostContextDTO monitorHost = monitorAgentContext.getMonitorHost(agentKey);
        // 查询主机信息
        HostBaseDTO host = hostAgentApi.getHostCacheByAgentKey(agentKey);
        if (host == null) {
            host = new HostBaseDTO();
        }
        // 获取指标
        MonitorMetricsContextDTO metrics = monitorMetricsContext.getMonitorMetrics(rule.getMetricsId());
        // 指标单位
        MetricsUnitEnum unit = MetricsUnitEnum.of(metrics.getUnit());
        // 获取连续触发次数
        Integer consecutiveCount = Optional.ofNullable(TRIGGER_STATE_CACHE.get(this.getTriggerStateCacheKey(agentKey, rule)))
                .map(AlarmTriggerStateDTO::getConsecutiveCount)
                .orElse(1);
        // 构建告警信息
        String alarmInfo = this.buildAlarmInfo(metrics, rule, unit, metricsValue, consecutiveCount);
        // 创建告警事件记录
        Map<String, String> tags = agentMetrics.getTags();
        AlarmEventDO alarmEvent = AlarmEventDO.builder()
                .agentKey(agentKey)
                .sourceType(AlarmEventSourceTypeEnum.HOST.name())
                .sourceId(host.getId())
                .sourceInfo(HostAlarmSourceInfo.builder()
                        .name(host.getName())
                        .code(host.getCode())
                        .address(host.getAddress())
                        .build()
                        .toJsonString())
                .policyId(rule.getPolicyId())
                .policyRuleId(rule.getId())
                .metricsId(rule.getMetricsId())
                .metricsMeasurement(metrics.getMeasurement())
                .alarmTags(tags == null ? Const.EMPTY_OBJECT : JSON.toJSONString(tags))
                .alarmValue(metricsValue)
                .alarmThreshold(unit.getThresholdOriginalValue(rule.getThreshold()))
                .alarmInfo(alarmInfo)
                .alarmLevel(rule.getLevel())
                .triggerCondition(rule.getTriggerCondition())
                .consecutiveCount(consecutiveCount)
                .falseAlarm(BooleanBit.FALSE.getValue())
                .handleStatus(AlarmHandleStatusEnum.NEW.name())
                .handleUserId(monitorHost.getOwnerUserId())
                .handleUsername(monitorHost.getOwnerUsername())
                .createTime(new Date(alarmTimestamp))
                .updateTime(new Date(alarmTimestamp))
                .build();

        // 保存告警事件
        alarmEventService.createAlarmEvent(alarmEvent);
        // 填充其他参数
        return AlarmEventConvert.MAPPER.toTrigger(alarmEvent);
    }

    @Override
    protected void setExtraAlarmPushParams(Map<String, Object> params, AlarmEventTriggerDTO event) {
        HostAlarmSourceInfo sourceInfo = JSON.parseObject(event.getSourceInfo(), HostAlarmSourceInfo.class);
        params.put("hostId", event.getSourceId());
        params.put("hostName", sourceInfo.getName());
        params.put("hostCode", sourceInfo.getCode());
        params.put("hostAddress", sourceInfo.getAddress());
    }

}