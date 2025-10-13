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

import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.framework.mybatis.core.utils.DomainFillUtils;
import org.dromara.visor.module.monitor.convert.AlarmPolicyRuleConvert;
import org.dromara.visor.module.monitor.dao.AlarmPolicyDAO;
import org.dromara.visor.module.monitor.dao.AlarmPolicyRuleDAO;
import org.dromara.visor.module.monitor.dao.MonitorMetricsDAO;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyDO;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyRuleDO;
import org.dromara.visor.module.monitor.entity.domain.MonitorMetricsDO;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleCreateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleUpdateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyRuleUpdateSwitchRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmPolicyRuleVO;
import org.dromara.visor.module.monitor.enums.AlarmSwitchEnum;
import org.dromara.visor.module.monitor.handler.alarm.AlarmEngineContext;
import org.dromara.visor.module.monitor.service.AlarmPolicyRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 监控告警规则 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 13:39
 */
@Slf4j
@Service
public class AlarmPolicyRuleServiceImpl implements AlarmPolicyRuleService {

    @Resource
    private AlarmEngineContext alarmEngineContext;

    @Resource
    private MonitorMetricsDAO monitorMetricsDAO;

    @Resource
    private AlarmPolicyDAO alarmPolicyDAO;

    @Resource
    private AlarmPolicyRuleDAO alarmPolicyRuleDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAlarmPolicyRule(AlarmPolicyRuleCreateRequest request) {
        log.info("AlarmPolicyRuleService-createAlarmPolicyRule request: {}", JSON.toJSONString(request));
        // 检查指标是否存在
        MonitorMetricsDO metrics = monitorMetricsDAO.selectById(request.getMetricsId());
        Assert.notNull(metrics, ErrorMessage.METRICS_ABSENT);
        // 检查告警策略是否存在
        AlarmPolicyDO alarmPolicy = alarmPolicyDAO.selectById(request.getPolicyId());
        Assert.notNull(alarmPolicy, ErrorMessage.ALARM_POLICY_ABSENT);
        // 转换
        AlarmPolicyRuleDO record = AlarmPolicyRuleConvert.MAPPER.to(request);
        record.setMetricsMeasurement(metrics.getMeasurement());
        // 插入
        int effect = alarmPolicyRuleDAO.insert(record);
        Long id = record.getId();
        // 重新加载策略
        alarmEngineContext.reloadPolicyRules(record.getPolicyId());
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.ID, id);
        log.info("AlarmPolicyRuleService-createAlarmPolicyRule id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateAlarmPolicyRuleById(AlarmPolicyRuleUpdateRequest request) {
        Long id = Assert.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("AlarmPolicyRuleService-updateAlarmPolicyRuleById id: {}, request: {}", id, JSON.toJSONString(request));
        // 检查指标是否存在
        MonitorMetricsDO metrics = monitorMetricsDAO.selectById(request.getMetricsId());
        Assert.notNull(metrics, ErrorMessage.METRICS_ABSENT);
        // 查询
        AlarmPolicyRuleDO record = alarmPolicyRuleDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        AlarmPolicyRuleDO updateRecord = AlarmPolicyRuleConvert.MAPPER.to(request);
        updateRecord.setMetricsMeasurement(metrics.getMeasurement());
        // 更新
        int effect = alarmPolicyRuleDAO.updateById(updateRecord);
        // 重新加载策略
        alarmEngineContext.reloadPolicyRules(record.getPolicyId());
        log.info("AlarmPolicyRuleService-updateAlarmPolicyRuleById effect: {}", effect);
        return effect;
    }

    @Override
    public Integer updateAlarmPolicyRuleSwitch(AlarmPolicyRuleUpdateSwitchRequest request) {
        Long id = request.getId();
        AlarmSwitchEnum switchEnum = AlarmSwitchEnum.of(request.getRuleSwitch());
        log.info("AlarmPolicyRuleService-updateAlarmPolicyRuleSwitch id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        AlarmPolicyRuleDO record = alarmPolicyRuleDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.SWITCH, switchEnum.name());
        // 更新
        AlarmPolicyRuleDO updateRecord = AlarmPolicyRuleDO.builder()
                .id(id)
                .ruleSwitch(switchEnum.getValue())
                .build();
        int effect = alarmPolicyRuleDAO.updateById(updateRecord);
        // 重新加载策略
        alarmEngineContext.reloadPolicyRules(record.getPolicyId());
        log.info("AlarmPolicyRuleService-updateAlarmPolicyRuleSwitch effect: {}", effect);
        return effect;
    }

    @Override
    public void copyAlarmPolicyRule(Long policyId, Long newPolicyId) {
        // 查询
        List<AlarmPolicyRuleDO> rows = alarmPolicyRuleDAO.of()
                .createWrapper()
                .eq(AlarmPolicyRuleDO::getPolicyId, policyId)
                .then()
                .stream()
                .peek(s -> {
                    DomainFillUtils.clearBaseFields(s);
                    s.setPolicyId(newPolicyId);
                }).collect(Collectors.toList());
        // 插入
        alarmPolicyRuleDAO.insertBatch(rows);
    }

    @Override
    public List<AlarmPolicyRuleVO> getAlarmPolicyRuleList(Long policyId, String measurement) {
        // 查询
        return alarmPolicyRuleDAO.of()
                .createWrapper()
                .eq(AlarmPolicyRuleDO::getPolicyId, policyId)
                .eq(Strings.isNotBlank(measurement), AlarmPolicyRuleDO::getMetricsMeasurement, measurement)
                // 相同的指标在一起
                .orderByAsc(AlarmPolicyRuleDO::getMetricsId)
                // 通过 p0 > p1 排序
                .orderByAsc(AlarmPolicyRuleDO::getLevel)
                .then()
                .list(AlarmPolicyRuleConvert.MAPPER::to);
    }

    @Override
    public Integer deleteByPolicyId(Long policyId) {
        // 删除并且清空缓存
        return this.deleteRuleAndClearCache(Conditions.eq(AlarmPolicyRuleDO::getPolicyId, policyId));
    }

    @Override
    public Integer deleteByMetricsId(Long metricsId) {
        // 删除并且清空缓存
        return this.deleteRuleAndClearCache(Conditions.eq(AlarmPolicyRuleDO::getMetricsId, metricsId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteAlarmPolicyRuleById(Long id) {
        log.info("AlarmPolicyRuleService-deleteAlarmPolicyRuleById id: {}", id);
        // 删除并且清空缓存
        int effect = this.deleteRuleAndClearCache(Conditions.eq(AlarmPolicyRuleDO::getId, id));
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        log.info("AlarmPolicyRuleService-deleteAlarmPolicyRuleById effect: {}", effect);
        return effect;
    }

    /**
     * 删除规则并且清理缓存
     *
     * @param wrapper wrapper
     * @return effect
     */
    private int deleteRuleAndClearCache(LambdaQueryWrapper<AlarmPolicyRuleDO> wrapper) {
        // 查询
        List<AlarmPolicyRuleDO> records = alarmPolicyRuleDAO.selectList(wrapper);
        if (records.isEmpty()) {
            return 0;
        }
        // 删除
        int effect = alarmPolicyRuleDAO.delete(wrapper);
        // 重新加载缓存
        records.stream()
                .map(AlarmPolicyRuleDO::getPolicyId)
                .distinct()
                .forEach(id -> alarmEngineContext.reloadPolicy(id));
        return effect;
    }

}
