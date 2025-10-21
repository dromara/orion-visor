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

import cn.orionsec.kit.lang.define.cache.TimedCache;
import cn.orionsec.kit.lang.define.cache.TimedCacheBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyBuilder;
import cn.orionsec.kit.lang.define.cache.key.CacheKeyDefine;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.time.Dates;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.entity.PushUser;
import org.dromara.visor.common.enums.BooleanBit;
import org.dromara.visor.framework.biz.push.core.utils.PushUtils;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.module.infra.api.SystemUserApi;
import org.dromara.visor.module.monitor.context.MonitorAgentContext;
import org.dromara.visor.module.monitor.context.MonitorMetricsContext;
import org.dromara.visor.module.monitor.define.cache.AlarmPolicyCacheKeyDefine;
import org.dromara.visor.module.monitor.entity.dto.*;
import org.dromara.visor.module.monitor.enums.AlarmLevelEnum;
import org.dromara.visor.module.monitor.enums.AlarmTriggerConditionEnum;
import org.dromara.visor.module.monitor.enums.MetricsUnitEnum;
import org.dromara.visor.module.monitor.handler.alarm.model.AlarmEnginePolicy;
import org.dromara.visor.module.monitor.handler.alarm.model.AlarmEngineRule;
import org.dromara.visor.module.monitor.service.AlarmEventService;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 告警引擎基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/10/13 10:12
 */
@Slf4j
public abstract class BaseAlarmEngine implements IAlarmEngine {

    /**
     * 告警触发状态缓存 10min
     */
    protected static final TimedCache<AlarmTriggerStateDTO> TRIGGER_STATE_CACHE = TimedCacheBuilder.<AlarmTriggerStateDTO>create()
            .expireAfter(10 * Const.MS_S_60)
            .checkInterval(Const.MS_S_60)
            .build();

    @Resource
    protected AlarmEngineContext alarmEngineContext;

    @Resource
    protected MonitorAgentContext monitorAgentContext;

    @Resource
    protected MonitorMetricsContext monitorMetricsContext;

    @Resource
    protected AlarmEventService alarmEventService;

    @Resource
    protected SystemUserApi systemUserApi;

    @PreDestroy
    public void destroyTimedCache() {
        Streams.close(TRIGGER_STATE_CACHE);
    }

    @Override
    public void checkAndAlarm(String agentKey,
                              AgentMetricsDataDTO prevMetrics,
                              AgentMetricsDataDTO newMetrics) {
        // 获取告警策略
        Long policyId = this.getAlarmPolicyId(agentKey);
        if (policyId == null) {
            return;
        }
        // 获取对应的策略
        AlarmEnginePolicy policy = alarmEngineContext.getMonitorPolicy(policyId);
        if (policy == null || Maps.isEmpty(policy.getRules())) {
            return;
        }
        // 遍历所有指标
        List<AlarmEventTriggerDTO> alarmEvents = new ArrayList<>();
        for (AgentMetricsDTO agentMetrics : newMetrics.getMetrics()) {
            if (Maps.isEmpty(agentMetrics.getValues())) {
                continue;
            }
            Set<String> metricsFields = agentMetrics.getValues().keySet();
            metricsFields.forEach(metricsField -> {
                try {
                    // 检查指标
                    AlarmEventTriggerDTO event = this.checkAndAlarm(agentKey,
                            prevMetrics, newMetrics, agentMetrics, metricsField,
                            policy);
                    if (event != null) {
                        alarmEvents.add(event);
                    }
                } catch (Exception e) {
                    log.error("checkAndAlarm error", e);
                }
            });
        }
        // 通知告警渠道
        if (!alarmEvents.isEmpty()) {
            this.notifyAlarmPolicyChannels(policy, alarmEvents);
        }
    }

    /**
     * 获取告警策略 id
     *
     * @param agentKey agentKey
     * @return policyId
     */
    protected abstract Long getAlarmPolicyId(String agentKey);

    /**
     * 检查并且告警
     *
     * @param agentKey     agentKey
     * @param prevMetrics  prevMetrics
     * @param newMetrics   newMetrics
     * @param agentMetrics agentMetrics
     * @param metricsField metricsField
     * @param policy       policy
     * @return event
     */
    protected AlarmEventTriggerDTO checkAndAlarm(String agentKey,
                                                 AgentMetricsDataDTO prevMetrics,
                                                 AgentMetricsDataDTO newMetrics,
                                                 AgentMetricsDTO agentMetrics,
                                                 String metricsField,
                                                 AlarmEnginePolicy policy) {
        Long alarmTimestamp = newMetrics.getTimestamp();
        // 指标id
        Long metricsId = monitorMetricsContext.getMonitorMetricsId(agentMetrics.getType(), metricsField);
        if (metricsId == null) {
            return null;
        }
        // 指标值
        BigDecimal metricsValue = agentMetrics.getValues().getBigDecimal(metricsField);
        if (metricsValue == null) {
            return null;
        }
        // 获取指标对应的全部规则
        List<AlarmEngineRule> rules = policy.getRules().get(metricsId);
        if (Lists.isEmpty(rules)) {
            return null;
        }
        // 匹配告警规则
        AlarmEngineRule matchedRule;
        if (!Maps.isEmpty(agentMetrics.getTags())) {
            matchedRule = this.matchTaggedAgentMetricsRule(rules, agentMetrics.getTags(), metricsValue);
        } else {
            matchedRule = this.matchAgentMetricsRule(rules, metricsValue);
        }
        // 未匹配到规则
        if (matchedRule == null) {
            return null;
        }
        // 检查是否到达连续触发条件
        boolean check = this.checkConsecutiveCount(agentKey, prevMetrics, newMetrics, matchedRule);
        if (!check) {
            return null;
        }
        // 检查是否在静默期
        boolean inSilence = this.checkAndSetInSilencePeriod(agentKey, alarmTimestamp, matchedRule);
        if (inSilence) {
            return null;
        }
        // 创建告警事件
        return this.createAlarmEvent(agentKey, alarmTimestamp, agentMetrics, metricsValue, matchedRule);
    }

    /**
     * 创建告警事件
     *
     * @param agentKey       agentKey
     * @param alarmTimestamp alarmTimestamp
     * @param agentMetrics   agentMetrics
     * @param metricsValue   metricsValue
     * @param rule           rule
     * @return event
     */
    protected abstract AlarmEventTriggerDTO createAlarmEvent(String agentKey,
                                                             Long alarmTimestamp,
                                                             AgentMetricsDTO agentMetrics,
                                                             BigDecimal metricsValue,
                                                             AlarmEngineRule rule);

    /**
     * 获取到第一个匹配到达阈值的规则 包含 tag
     *
     * @param rules        rules
     * @param metricsTags  metricsTags
     * @param metricsValue metricsValue
     * @return rule
     */
    protected AlarmEngineRule matchTaggedAgentMetricsRule(List<AlarmEngineRule> rules,
                                                          Map<String, String> metricsTags,
                                                          BigDecimal metricsValue) {
        AlarmEngineRule matchedRule = null;
        // context 根据 level 排序了
        for (AlarmEngineRule rule : rules) {
            Map<String, List<String>> ruleTags = rule.getTags();
            final boolean tagEmpty = Maps.isEmpty(ruleTags);
            // tag 为空或 非全部生效
            if (tagEmpty && !BooleanBit.toBoolean(rule.getAllEffect())) {
                continue;
            }
            // 对比 tag 是否匹配
            if (!tagEmpty) {
                boolean tagMatched = true;
                for (Map.Entry<String, List<String>> ruleTagEntry : ruleTags.entrySet()) {
                    String metricsTagValue = metricsTags.get(ruleTagEntry.getKey());
                    // 为空则跳过循环
                    if (Strings.isBlank(metricsTagValue)) {
                        tagMatched = false;
                        break;
                    }
                    // 不包含则跳过循环
                    if (!ruleTagEntry.getValue().contains(metricsTagValue.trim())) {
                        tagMatched = false;
                        break;
                    }
                }
                if (!tagMatched) {
                    continue;
                }
            }
            // 判断当前规则条件是否可触发
            boolean triggered = this.checkAlarmCondition(rule, metricsValue);
            if (triggered) {
                matchedRule = rule;
                break;
            }
        }
        return matchedRule;
    }

    /**
     * 获取到第一个匹配到达阈值的规则 不包含 tag
     *
     * @param rules        rules
     * @param metricsValue metricsValue
     * @return rule
     */
    protected AlarmEngineRule matchAgentMetricsRule(List<AlarmEngineRule> rules, BigDecimal metricsValue) {
        AlarmEngineRule matchedRule = null;
        // context 根据 level 排序了
        for (AlarmEngineRule rule : rules) {
            // 有 tag 则跳过此规则
            if (!Maps.isEmpty(rule.getTags())) {
                continue;
            }
            // 判断当前规则条件是否可触发
            boolean triggered = this.checkAlarmCondition(rule, metricsValue);
            if (triggered) {
                matchedRule = rule;
                break;
            }
        }
        return matchedRule;
    }

    /**
     * 检查是否触发告警条件
     *
     * @param rule        rule
     * @param metricValue metricValue
     */
    protected boolean checkAlarmCondition(AlarmEngineRule rule, BigDecimal metricValue) {
        // 获取指标值
        if (metricValue == null) {
            return false;
        }
        // 获取指标单位
        MonitorMetricsContextDTO metrics = monitorMetricsContext.getMonitorMetrics(rule.getMetricsId());
        MetricsUnitEnum unit = Optional.ofNullable(metrics)
                .map(MonitorMetricsContextDTO::getUnit)
                .map(MetricsUnitEnum::of)
                .orElse(null);
        if (unit == null) {
            return false;
        }
        // 获取阈值
        BigDecimal threshold = rule.getThreshold();
        if (threshold == null) {
            return false;
        }
        // 将阈值转换为原始值
        threshold = unit.getThresholdOriginalValue(threshold);
        // 判断是否达到触发条件
        return this.evaluateCondition(rule.getTriggerCondition(), metricValue, threshold);
    }

    /**
     * 判断是否满足触发条件
     *
     * @param condition   condition
     * @param metricValue metricValue
     * @param threshold   threshold
     * @return eval
     */
    protected boolean evaluateCondition(String condition,
                                        BigDecimal metricValue,
                                        BigDecimal threshold) {
        // 触发条件
        AlarmTriggerConditionEnum triggerCondition = AlarmTriggerConditionEnum.of(condition);
        switch (triggerCondition) {
            case GT:
                return metricValue.compareTo(threshold) > 0;
            case GE:
                return metricValue.compareTo(threshold) >= 0;
            case EQ:
                return metricValue.compareTo(threshold) == 0;
            case LE:
                return metricValue.compareTo(threshold) <= 0;
            case LT:
                return metricValue.compareTo(threshold) < 0;
            default:
                log.warn("Unsupported alarm condition: {}", condition);
                return false;
        }
    }

    /**
     * 检查是否达到连续触发条件
     *
     * @param agentKey    agentKey
     * @param prevMetrics prevMetrics
     * @param newMetrics  newMetrics
     * @param rule        rule
     * @return result
     */
    protected boolean checkConsecutiveCount(String agentKey,
                                            AgentMetricsDataDTO prevMetrics,
                                            AgentMetricsDataDTO newMetrics,
                                            AlarmEngineRule rule) {
        // 获取规则连续触发次数
        Integer ruleConsecutiveCount = Objects1.def(rule.getConsecutiveCount(), 1);
        // 获取指标连续触发次数
        String cacheKey = this.getTriggerStateCacheKey(agentKey, rule);
        AlarmTriggerStateDTO triggerState = TRIGGER_STATE_CACHE.get(cacheKey);
        // 判断是否是连续触发
        boolean consecutiveTrigger = this.isConsecutiveTrigger(triggerState, prevMetrics);
        if (consecutiveTrigger) {
            // 连续触发则累加触发次数
            triggerState = AlarmTriggerStateDTO.builder()
                    .timestamp(newMetrics.getTimestamp())
                    .consecutiveCount(triggerState.getConsecutiveCount() + 1)
                    .build();
        } else {
            // 首次触发 或 中断后重新触发
            triggerState = AlarmTriggerStateDTO.builder()
                    .timestamp(newMetrics.getTimestamp())
                    .consecutiveCount(1)
                    .build();
        }
        // 重新设置缓存
        TRIGGER_STATE_CACHE.put(cacheKey, triggerState);
        // 检查是否达到连续触发次数
        return triggerState.getConsecutiveCount() >= ruleConsecutiveCount;
    }

    /**
     * 检查是否是连续触发
     *
     * @param triggerState triggerState
     * @param prevMetrics  prevMetrics
     * @return isConsecutiveTrigger
     */
    protected boolean isConsecutiveTrigger(AlarmTriggerStateDTO triggerState,
                                           AgentMetricsDataDTO prevMetrics) {
        if (prevMetrics == null || triggerState == null) {
            return false;
        }
        return Objects1.eq(triggerState.getTimestamp(), prevMetrics.getTimestamp());
    }

    /**
     * 检查并且设置静默期
     *
     * @param agentKey       agentKey
     * @param alarmTimestamp alarmTimestamp
     * @param rule           rule
     * @return inSilence
     */
    protected boolean checkAndSetInSilencePeriod(String agentKey,
                                                 Long alarmTimestamp,
                                                 AlarmEngineRule rule) {
        Integer silencePeriod = Objects1.def(rule.getSilencePeriod(), 0);
        // 无静默期则触发
        if (silencePeriod <= 0) {
            return false;
        }
        // 检查是否在沉默期内
        String cacheKey = AlarmPolicyCacheKeyDefine.ALARM_RULE_SILENCE.format(agentKey, rule.getId());
        boolean inSilence = RedisStrings.hasKey(cacheKey);
        if (!inSilence) {
            // 不在沉默期则重新设置沉默期缓存
            CacheKeyDefine key = CacheKeyBuilder.create()
                    .key(cacheKey)
                    .noPrefix()
                    .timeout(silencePeriod, TimeUnit.MINUTES)
                    .build();
            RedisStrings.set(key, alarmTimestamp);
        }
        return inSilence;
    }

    /**
     * 构建告警信息
     *
     * @param metrics          metrics
     * @param rule             rule
     * @param unit             unit
     * @param metricsValue     metricsValue
     * @param consecutiveCount consecutiveCount
     * @return alarmInfo
     */
    protected String buildAlarmInfo(MonitorMetricsContextDTO metrics,
                                    AlarmEngineRule rule,
                                    MetricsUnitEnum unit,
                                    BigDecimal metricsValue,
                                    Integer consecutiveCount) {
        return metrics.getName()
                + Const.SPACE + AlarmTriggerConditionEnum.of(rule.getTriggerCondition()).getCondition()
                + Const.SPACE + unit.format(rule.getThreshold(), new MetricsUnitEnum.FormatOptions(2, metrics.getSuffix()))
                + ", 当前值: " + unit.format(metricsValue, new MetricsUnitEnum.FormatOptions(4, metrics.getSuffix()))
                + ", 已连续触发次数: " + consecutiveCount;
    }

    /**
     * 获取触发状态缓存 key
     *
     * @param agentKey agentKey
     * @param rule     rule
     * @return cacheKey
     */
    protected String getTriggerStateCacheKey(String agentKey, AlarmEngineRule rule) {
        return agentKey + ":" + rule.getId();
    }

    /**
     * 通知告警策略渠道
     *
     * @param policy      policy
     * @param alarmEvents alarmEvents
     */
    protected void notifyAlarmPolicyChannels(AlarmEnginePolicy policy, List<AlarmEventTriggerDTO> alarmEvents) {
        List<Long> notifyIdList = policy.getNotifyIdList();
        if (Lists.isEmpty(notifyIdList)) {
            return;
        }
        // 推送用户
        List<PushUser> pushUsers = Optional.of(alarmEvents.get(0))
                .map(AlarmEventTriggerDTO::getHandleUserId)
                .map(systemUserApi::getNotifyUserById)
                .map(Lists::singleton)
                .orElse(null);
        // 构建参数
        List<Map<String, Object>> paramsList = new ArrayList<>();
        for (AlarmEventTriggerDTO event : alarmEvents) {
            try {
                MonitorMetricsContextDTO metrics = monitorMetricsContext.getMonitorMetrics(event.getMetricsId());
                MetricsUnitEnum unit = MetricsUnitEnum.of(metrics.getUnit());
                AlarmLevelEnum level = AlarmLevelEnum.of(event.getAlarmLevel());
                AlarmTriggerConditionEnum triggerCondition = AlarmTriggerConditionEnum.of(event.getTriggerCondition());

                // 告警事件参数
                Map<String, Object> params = new HashMap<>();
                params.put("id", event.getId());
                params.put("relKey", event.getId());
                params.put("policyId", policy.getId());
                params.put("policyName", policy.getName());
                params.put("ruleId", event.getPolicyRuleId());
                params.put("metrics", metrics.getMeasurement() + "." + metrics.getValue());
                params.put("metricsId", metrics.getId());
                params.put("metricsName", metrics.getName());
                params.put("metricsField", metrics.getValue());
                params.put("metricsMeasurement", metrics.getMeasurement());
                params.put("tags", event.getAlarmTags());
                params.put("level", level.name());
                params.put("levelLabel", level.getLabel());
                params.put("levelSeverity", level.getSeverity());
                params.put("levelColor", level.getColor());
                params.put("consecutiveCount", event.getConsecutiveCount());
                params.put("triggerCondition", triggerCondition.getCondition());
                params.put("alarmInfo", event.getAlarmInfo());
                params.put("alarmValue", unit.format(event.getAlarmValue(), new MetricsUnitEnum.FormatOptions(2, metrics.getSuffix())));
                params.put("alarmThreshold", unit.format(event.getAlarmThreshold(), new MetricsUnitEnum.FormatOptions(4, metrics.getSuffix())));
                params.put("alarmTime", Dates.format(event.getCreateTime()));
                // 设置额外告警推送参数
                this.setExtraAlarmPushParams(params, event);
                paramsList.add(params);
            } catch (Exception e) {
                log.info("AlarmEngine-setAlarmParams error", e);
            }
        }
        // 推送消息
        for (Map<String, Object> params : paramsList) {
            for (Long notifyId : notifyIdList) {
                PushUtils.pushTemplate(notifyId, params, pushUsers);
            }
        }
    }

    /**
     * 设置告警推送参数
     *
     * @param params params
     * @param event  event
     */
    protected abstract void setExtraAlarmPushParams(Map<String, Object> params, AlarmEventTriggerDTO event);

}
