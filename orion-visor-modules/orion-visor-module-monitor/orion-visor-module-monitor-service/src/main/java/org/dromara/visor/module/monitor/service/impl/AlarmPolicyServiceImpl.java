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

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.Booleans;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.monitor.convert.AlarmPolicyConvert;
import org.dromara.visor.module.monitor.dao.AlarmPolicyDAO;
import org.dromara.visor.module.monitor.dao.AlarmPolicyNotifyDAO;
import org.dromara.visor.module.monitor.dao.AlarmPolicyRuleDAO;
import org.dromara.visor.module.monitor.dao.MonitorHostDAO;
import org.dromara.visor.module.monitor.define.cache.AlarmPolicyCacheKeyDefine;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyDO;
import org.dromara.visor.module.monitor.entity.dto.AlarmPolicyAlarmCountDTO;
import org.dromara.visor.module.monitor.entity.dto.AlarmPolicyCacheDTO;
import org.dromara.visor.module.monitor.entity.po.AlarmPolicyRuleCountPO;
import org.dromara.visor.module.monitor.entity.po.MonitorHostCountPO;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyCopyRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyCreateRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyQueryRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmPolicyUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmPolicyVO;
import org.dromara.visor.module.monitor.handler.alarm.AlarmEngineContext;
import org.dromara.visor.module.monitor.service.AlarmEventService;
import org.dromara.visor.module.monitor.service.AlarmPolicyNotifyService;
import org.dromara.visor.module.monitor.service.AlarmPolicyRuleService;
import org.dromara.visor.module.monitor.service.AlarmPolicyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 监控告警策略 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-14 00:12
 */
@Slf4j
@Service
public class AlarmPolicyServiceImpl implements AlarmPolicyService {

    @Resource
    private MonitorHostDAO monitorHostDAO;

    @Resource
    private AlarmPolicyDAO alarmPolicyDAO;

    @Resource
    private AlarmPolicyNotifyDAO alarmPolicyNotifyDAO;

    @Resource
    private AlarmPolicyRuleDAO alarmPolicyRuleDAO;

    @Resource
    private AlarmPolicyRuleService alarmPolicyRuleService;

    @Resource
    private AlarmPolicyNotifyService alarmPolicyNotifyService;

    @Resource
    private AlarmEventService alarmEventService;

    @Resource
    private AlarmEngineContext alarmEngineContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAlarmPolicy(AlarmPolicyCreateRequest request) {
        log.info("AlarmPolicyService-createAlarmPolicy request: {}", JSON.toJSONString(request));
        // 转换
        AlarmPolicyDO record = AlarmPolicyConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkAlarmPolicyPresent(record);
        // 插入
        int effect = alarmPolicyDAO.insert(record);
        Long id = record.getId();
        // 设置告警通知
        alarmPolicyNotifyService.setAlarmPolicyNotify(id, request.getNotifyIdList());
        // 重新加载上下文
        alarmEngineContext.reloadPolicy(id);
        // 删除缓存
        RedisMaps.delete(AlarmPolicyCacheKeyDefine.ALARM_POLICY.format(record.getType()),
                AlarmPolicyCacheKeyDefine.ALARM_POLICY.format(Const.ALL));
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.ID, id);
        log.info("AlarmPolicyService-createAlarmPolicy id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyAlarmPolicy(AlarmPolicyCopyRequest request) {
        Long id = Assert.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("AlarmPolicyService-copyAlarmPolicy id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        AlarmPolicyDO record = alarmPolicyDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 创建策略
        Long newId = this.createAlarmPolicy(request);
        // 复制策略规则
        alarmPolicyRuleService.copyAlarmPolicyRule(id, newId);
        // 重新加载上下文
        alarmEngineContext.reloadPolicy(id);
        // 删除缓存
        RedisMaps.delete(AlarmPolicyCacheKeyDefine.ALARM_POLICY.format(record.getType()),
                AlarmPolicyCacheKeyDefine.ALARM_POLICY.format(Const.ALL));
        return newId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateAlarmPolicyById(AlarmPolicyUpdateRequest request) {
        Long id = Assert.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("AlarmPolicyService-updateAlarmPolicyById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        AlarmPolicyDO record = alarmPolicyDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        AlarmPolicyDO updateRecord = AlarmPolicyConvert.MAPPER.to(request);
        updateRecord.setType(record.getType());
        // 查询数据是否冲突
        this.checkAlarmPolicyPresent(updateRecord);
        // 更新
        int effect = alarmPolicyDAO.updateById(updateRecord);
        // 设置告警通知
        if (Booleans.isTrue(request.getUpdateNotify())) {
            alarmPolicyNotifyService.setAlarmPolicyNotify(id, request.getNotifyIdList());
        }
        log.info("AlarmPolicyService-updateAlarmPolicyById effect: {}", effect);
        // 重新加载上下文
        alarmEngineContext.reloadPolicy(id);
        // 删除缓存
        RedisMaps.delete(AlarmPolicyCacheKeyDefine.ALARM_POLICY.format(record.getType()),
                AlarmPolicyCacheKeyDefine.ALARM_POLICY.format(Const.ALL));
        return effect;
    }

    @Override
    public AlarmPolicyVO getAlarmPolicyById(Long id) {
        // 查询
        AlarmPolicyDO record = alarmPolicyDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 查询通知策略
        List<Long> notifyIdList = alarmPolicyNotifyService.getAlarmPolicyNotify(id);
        // 转换
        AlarmPolicyVO vo = AlarmPolicyConvert.MAPPER.to(record);
        vo.setNotifyIdList(notifyIdList);
        return vo;
    }

    @Override
    public List<AlarmPolicyVO> getAlarmPolicyListByCache(String type) {
        String cacheKey = AlarmPolicyCacheKeyDefine.ALARM_POLICY.format(type);
        // 查询缓存
        List<AlarmPolicyCacheDTO> list = RedisMaps.valuesJson(cacheKey, AlarmPolicyCacheKeyDefine.ALARM_POLICY);
        if (list.isEmpty()) {
            // 查询数据库
            list = alarmPolicyDAO.of()
                    .createWrapper()
                    .eq(!Const.ALL.equals(type), AlarmPolicyDO::getType, type)
                    .then()
                    .list(AlarmPolicyConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, AlarmPolicyCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(cacheKey, AlarmPolicyCacheKeyDefine.ALARM_POLICY, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(AlarmPolicyConvert.MAPPER::to)
                .sorted(Comparator.comparing(AlarmPolicyVO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public DataGrid<AlarmPolicyVO> getAlarmPolicyPage(AlarmPolicyQueryRequest request) {
        // 条件
        LambdaQueryWrapper<AlarmPolicyDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        DataGrid<AlarmPolicyVO> dataGrid = alarmPolicyDAO.of()
                .wrapper(wrapper)
                .page(request)
                .order(request, AlarmPolicyDO::getId)
                .dataGrid(AlarmPolicyConvert.MAPPER::to);
        if (dataGrid.isEmpty()) {
            return dataGrid;
        }
        List<Long> policyIdList = dataGrid.stream()
                .map(AlarmPolicyVO::getId)
                .collect(Collectors.toList());
        // 查询规则数
        Map<Long, Integer> ruleCountMap = alarmPolicyRuleDAO.selectPolicyRuleCount(policyIdList)
                .stream()
                .collect(Collectors.toMap(AlarmPolicyRuleCountPO::getPolicyId, AlarmPolicyRuleCountPO::getCount));
        // 查询主机数
        Map<Long, Integer> hostCountMap = monitorHostDAO.selectPolicyHostCount(policyIdList)
                .stream()
                .collect(Collectors.toMap(MonitorHostCountPO::getPolicyId, MonitorHostCountPO::getCount));
        // 今日触发次数
        Map<Long, Integer> todayTriggerCountMap = alarmEventService.getPolicyEventCount(policyIdList, 0, 0)
                .stream()
                .collect(Collectors.toMap(AlarmPolicyAlarmCountDTO::getPolicyId, AlarmPolicyAlarmCountDTO::getCount));
        // 一周触发次数
        Map<Long, Integer> weekTriggerCountMap = alarmEventService.getPolicyEventCount(policyIdList, -7, -1)
                .stream()
                .collect(Collectors.toMap(AlarmPolicyAlarmCountDTO::getPolicyId, AlarmPolicyAlarmCountDTO::getCount));
        for (AlarmPolicyVO policy : dataGrid) {
            policy.setRuleCount(ruleCountMap.getOrDefault(policy.getId(), 0));
            policy.setHostCount(hostCountMap.getOrDefault(policy.getId(), 0));
            policy.setTodayTriggerCount(todayTriggerCountMap.getOrDefault(policy.getId(), 0));
            policy.setWeekTriggerCount(weekTriggerCountMap.getOrDefault(policy.getId(), 0) + todayTriggerCountMap.getOrDefault(policy.getId(), 0));
        }
        return dataGrid;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteAlarmPolicyById(Long id) {
        log.info("AlarmPolicyService-deleteAlarmPolicyById id: {}", id);
        // 查询数据
        AlarmPolicyDO record = alarmPolicyDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除主表
        int effect = alarmPolicyDAO.deleteById(id);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.NAME, record.getName());
        // 设置主机策略为空
        monitorHostDAO.setPolicyIdWithNull(id);
        // 删除策略通知
        alarmPolicyNotifyDAO.deleteByPolicyId(id);
        // 删除策略规则
        alarmPolicyRuleDAO.deleteByPolicyId(id);
        // 删除缓存
        alarmEngineContext.reloadPolicy(id);
        log.info("AlarmPolicyService-deleteAlarmPolicyById effect: {}", effect);
        return effect;
    }

    @Override
    public LambdaQueryWrapper<AlarmPolicyDO> buildQueryWrapper(AlarmPolicyQueryRequest request) {
        return alarmPolicyDAO.wrapper()
                .eq(AlarmPolicyDO::getId, request.getId())
                .eq(AlarmPolicyDO::getType, request.getType())
                .like(AlarmPolicyDO::getName, request.getName())
                .like(AlarmPolicyDO::getDescription, request.getDescription());
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkAlarmPolicyPresent(AlarmPolicyDO domain) {
        // 构造条件
        LambdaQueryWrapper<AlarmPolicyDO> wrapper = alarmPolicyDAO.wrapper()
                // 更新时忽略当前记录
                .ne(AlarmPolicyDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(AlarmPolicyDO::getType, domain.getType())
                .eq(AlarmPolicyDO::getName, domain.getName());
        // 检查是否存在
        boolean present = alarmPolicyDAO.of(wrapper).present();
        Assert.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
