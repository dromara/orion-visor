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

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.monitor.convert.AlarmPolicyRuleConvert;
import org.dromara.visor.module.monitor.dao.AlarmPolicyDAO;
import org.dromara.visor.module.monitor.dao.AlarmPolicyNotifyDAO;
import org.dromara.visor.module.monitor.dao.AlarmPolicyRuleDAO;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyDO;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyNotifyDO;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyRuleDO;
import org.dromara.visor.module.monitor.enums.AlarmSwitchEnum;
import org.dromara.visor.module.monitor.handler.alarm.model.AlarmEnginePolicy;
import org.dromara.visor.module.monitor.handler.alarm.model.AlarmEngineRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 告警引擎上下文
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/21 17:26
 */
@Slf4j
@Component
public class AlarmEngineContext {

    private static final Map<Long, AlarmEnginePolicy> ALARM_POLICY_MAP = new ConcurrentHashMap<>();

    @Resource
    private AlarmPolicyDAO alarmPolicyDAO;

    @Resource
    private AlarmPolicyRuleDAO alarmPolicyRuleDAO;

    @Resource
    private AlarmPolicyNotifyDAO alarmPolicyNotifyDAO;

    /**
     * 初始化告警引擎
     */
    @PostConstruct
    public void init() {
        log.info("Initializing alarm engine...");
        try {
            // 加载所有策略
            this.loadAllPolicies();
            log.info("Alarm engine initialized successfully.");
        } catch (Exception e) {
            log.error("Failed to initialize alarm engine", e);
        }
    }

    /**
     * 加载所有告警策略
     */
    private void loadAllPolicies() {
        // 清空策略
        ALARM_POLICY_MAP.clear();
        // 查询所有策略
        List<AlarmPolicyDO> policyList = alarmPolicyDAO.selectList(null);
        // 查询所有规则
        List<AlarmPolicyRuleDO> ruleList = alarmPolicyRuleDAO.selectList(null);
        // 查询所有通知
        List<AlarmPolicyNotifyDO> notifyList = alarmPolicyNotifyDAO.selectList(null);
        // 转为 map
        Map<Long, List<AlarmPolicyRuleDO>> policyRules = ruleList.stream()
                .collect(Collectors.groupingBy(AlarmPolicyRuleDO::getPolicyId));
        Map<Long, List<AlarmPolicyNotifyDO>> policyNotifiers = notifyList.stream()
                .collect(Collectors.groupingBy(AlarmPolicyNotifyDO::getPolicyId));
        // 初始化策略
        for (AlarmPolicyDO policy : policyList) {
            Long policyId = policy.getId();
            // 获取策略规则
            Map<Long, List<AlarmEngineRule>> engineRules = this.getPolicyRules(policyRules.get(policyId));
            // 获取策略通知
            List<Long> engineNotifier = this.getPolicyEngineNotifier(policyNotifiers.get(policyId));
            // 设置策略
            AlarmEnginePolicy enginePolicy = AlarmEnginePolicy.builder()
                    .id(policyId)
                    .name(policy.getName())
                    .rules(engineRules)
                    .notifyIdList(engineNotifier)
                    .build();
            ALARM_POLICY_MAP.put(policyId, enginePolicy);
            log.info("Loaded alarm policy id: {}, name: {}, metricsCount: {}", policyId, policy.getName(), engineRules.size());
        }
    }

    /**
     * 获取策略规则
     *
     * @param list list
     * @return rules
     */
    private Map<Long, List<AlarmEngineRule>> getPolicyRules(List<AlarmPolicyRuleDO> list) {
        if (Lists.isEmpty(list)) {
            return new HashMap<>();
        }
        // 转为 map
        Map<Long, List<AlarmEngineRule>> ruleMap = Lists.stream(list)
                .map(s -> {
                    // 转换
                    AlarmEngineRule rule = AlarmPolicyRuleConvert.MAPPER.toEngine(s);
                    // 设置 tag
                    rule.setTags(this.parseRuleTags(s.getTags()));
                    return rule;
                })
                .collect(Collectors.groupingBy(AlarmEngineRule::getMetricsId));
        // 处理规则
        for (Map.Entry<Long, List<AlarmEngineRule>> entry : ruleMap.entrySet()) {
            // 移除未启用的规则
            entry.getValue().removeIf(s -> AlarmSwitchEnum.isOff(s.getRuleSwitch()));
            // 通过等级排序规则
            entry.getValue().sort(Comparator.comparingInt(AlarmEngineRule::getLevel));
        }
        // 移除空规则
        ruleMap.entrySet().removeIf(s -> Lists.isEmpty(s.getValue()));
        return ruleMap;
    }

    /**
     * 获取策略通知
     *
     * @param list list
     * @return id
     */
    private List<Long> getPolicyEngineNotifier(List<AlarmPolicyNotifyDO> list) {
        if (Lists.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(AlarmPolicyNotifyDO::getNotifyId)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 获取告警策略
     *
     * @param policyId policyId
     * @return policy
     */
    public AlarmEnginePolicy getMonitorPolicy(Long policyId) {
        return ALARM_POLICY_MAP.get(policyId);
    }

    /**
     * 重新加载策略
     *
     * @param policyId policyId
     */
    public void reloadPolicy(Long policyId) {
        // 移除策略
        ALARM_POLICY_MAP.remove(policyId);
        // 检查策略是否已被删除
        AlarmPolicyDO policy = alarmPolicyDAO.selectById(policyId);
        if (policy == null) {
            return;
        }
        // 获取策略规则
        List<AlarmPolicyRuleDO> policyRules = alarmPolicyRuleDAO.selectByPolicyId(policyId);
        Map<Long, List<AlarmEngineRule>> engineRules = this.getPolicyRules(policyRules);
        // 获取策略通知
        List<Long> engineNotifier = this.getPolicyEngineNotifier(alarmPolicyNotifyDAO.selectByPolicyId(policyId));
        // 设置策略
        AlarmEnginePolicy enginePolicy = AlarmEnginePolicy.builder()
                .id(policyId)
                .name(policy.getName())
                .rules(engineRules)
                .notifyIdList(engineNotifier)
                .build();
        ALARM_POLICY_MAP.put(policyId, enginePolicy);
        log.info("Reloaded alarm policy id: {}, metricsCount: {}", policyId, engineRules.size());
    }

    /**
     * 重新加载策略规则
     *
     * @param policyId policyId
     */
    public void reloadPolicyRules(Long policyId) {
        AlarmEnginePolicy policy = ALARM_POLICY_MAP.get(policyId);
        if (policy == null) {
            return;
        }
        // 重新设置规则
        policy.setRules(this.getPolicyRules(alarmPolicyRuleDAO.selectByPolicyId(policyId)));
    }

    /**
     * 解析规则标签
     *
     * @param tagsString tagsString
     * @return tags
     */
    private Map<String, List<String>> parseRuleTags(String tagsString) {
        Map<String, List<String>> newTags = new HashMap<>();
        if (Strings.isBlank(tagsString)) {
            return newTags;
        }
        JSONArray tagArray = JSON.parseArray(tagsString);
        for (int i = 0; i < tagArray.size(); i++) {
            JSONObject tag = tagArray.getJSONObject(i);
            String tagKey = tag.getString(Const.KEY);
            Object tagValue = tag.get(Const.VALUE);
            if (tagValue == null) {
                continue;
            }
            // 将 value 转为 string 集合
            List<String> tagValues;
            if (tagValue instanceof Collection) {
                tagValues = ((Collection<?>) tagValue).stream()
                        .filter(Objects::nonNull)
                        .map(String::valueOf)
                        .filter(Strings::isNotBlank)
                        .map(String::trim)
                        .collect(Collectors.toList());
            } else {
                // 非数组直接用 , 分割
                tagValues = Arrays.stream(tagValue.toString().split(","))
                        .filter(Strings::isNotBlank)
                        .map(String::trim)
                        .collect(Collectors.toList());
            }
            // 添加 tag
            if (!Lists.isEmpty(tagValues)) {
                newTags.put(tagKey, tagValues);
            }
        }
        return newTags;
    }

}
