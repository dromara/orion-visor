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
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.time.Dates;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.enums.BooleanBit;
import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.common.utils.SqlUtils;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.monitor.convert.AlarmEventConvert;
import org.dromara.visor.module.monitor.dao.AlarmEventDAO;
import org.dromara.visor.module.monitor.define.cache.AlarmEventCacheKeyDefine;
import org.dromara.visor.module.monitor.entity.domain.AlarmEventDO;
import org.dromara.visor.module.monitor.entity.dto.AlarmPolicyAlarmCountDTO;
import org.dromara.visor.module.monitor.entity.po.AlarmEventCountPO;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventClearRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventHandleRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventQueryRequest;
import org.dromara.visor.module.monitor.entity.request.alarm.AlarmEventSetFalseRequest;
import org.dromara.visor.module.monitor.entity.vo.AlarmEventVO;
import org.dromara.visor.module.monitor.service.AlarmEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 监控告警事件 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-17 21:31
 */
@Slf4j
@Service
public class AlarmEventServiceImpl implements AlarmEventService {

    @Resource
    private AlarmEventDAO alarmEventDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createAlarmEvent(AlarmEventDO record) {
        log.info("AlarmEventService-createAlarmEvent record: {}", JSON.toJSONString(record));
        alarmEventDAO.insert(record);
    }

    @Override
    public Integer handleAlarmEvent(AlarmEventHandleRequest request) {
        LoginUser loginUser = SecurityUtils.getLoginUserNotNull();
        // 条件
        LambdaQueryWrapper<AlarmEventDO> wrapper = Conditions.in(AlarmEventDO::getId, request.getIdList());
        AlarmEventDO entity = new AlarmEventDO();
        entity.setHandleStatus(request.getHandleStatus());
        entity.setHandleTime(request.getHandleTime());
        entity.setHandleRemark(request.getHandleRemark());
        entity.setHandleUserId(loginUser.getId());
        entity.setHandleUsername(loginUser.getUsername());
        // 更新
        int effect = alarmEventDAO.update(entity, wrapper);
        log.info("AlarmEventService-handleAlarmEvent effect: {}", effect);
        return effect;
    }

    @Override
    public Integer setAlarmEventFalse(AlarmEventSetFalseRequest request) {
        AlarmEventDO entity = new AlarmEventDO();
        entity.setFalseAlarm(BooleanBit.TRUE.getValue());
        // 更新
        int effect = alarmEventDAO.update(entity, Conditions.in(AlarmEventDO::getId, request.getIdList()));
        log.info("AlarmEventService-setAlarmEventFalse effect: {}", effect);
        return effect;
    }

    @Override
    public DataGrid<AlarmEventVO> getAlarmEventPage(AlarmEventQueryRequest request) {
        // 条件
        LambdaQueryWrapper<AlarmEventDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return alarmEventDAO.of()
                .wrapper(wrapper)
                .page(request)
                .order(request, AlarmEventDO::getId)
                .dataGrid(AlarmEventConvert.MAPPER::to);
    }

    @Override
    public Long getAlarmEventCount(AlarmEventQueryRequest request) {
        // 条件
        LambdaQueryWrapper<AlarmEventDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return alarmEventDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteAlarmEventById(Long id) {
        return this.deleteAlarmEventByIdList(Lists.singleton(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteAlarmEventByIdList(List<Long> idList) {
        if (Lists.isEmpty(idList)) {
            OperatorLogs.add(OperatorLogs.COUNT, Const.N_0);
            return Const.N_0;
        }
        log.info("AlarmEventService-deleteAlarmEventByIdList idList: {}", idList);
        int effect = alarmEventDAO.deleteBatchIds(idList);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        log.info("AlarmEventService-deleteAlarmEventByIdList effect: {}", effect);
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer clearAlarmEvent(AlarmEventClearRequest request) {
        // 删除条件
        LambdaQueryWrapper<AlarmEventDO> wrapper = this.buildQueryWrapper(request)
                .select(AlarmEventDO::getId)
                .orderByAsc(AlarmEventDO::getId)
                .last(SqlUtils.limit(request.getLimit()));
        int effect = alarmEventDAO.delete(wrapper);
        log.info("AlarmEventService-clearAlarmEvent effect: {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public List<AlarmPolicyAlarmCountDTO> getPolicyEventCount(List<Long> policyIdList, int startDay, int endDay) {
        Date startDate = Dates.stream().addDay(startDay).clearHms().date();
        Date endDate = Dates.stream().addDay(endDay).dayEnd().date();
        // 查询缓存
        String cacheKey = AlarmEventCacheKeyDefine.ALARM_POLICY_EVENT_COUNT.format(Dates.format(startDate, Dates.YMD2), Dates.format(endDate, Dates.YMD2));
        List<Integer> countList = RedisMaps.multiGet(cacheKey, policyIdList)
                .stream()
                .map(s -> {
                    if (s != null) {
                        return Integer.valueOf(s);
                    } else {
                        return null;
                    }
                }).collect(Collectors.toList());
        // 构建返回数据
        List<AlarmPolicyAlarmCountDTO> result = new ArrayList<>();
        List<Long> queryIdList = new ArrayList<>();
        for (int i = 0; i < countList.size(); i++) {
            Long policyId = policyIdList.get(i);
            Integer count = countList.get(i);
            if (count != null) {
                result.add(new AlarmPolicyAlarmCountDTO(policyId, count));
            } else {
                queryIdList.add(policyId);
            }
        }
        // 查询数据库
        if (!queryIdList.isEmpty()) {
            Map<Long, Integer> countMap = alarmEventDAO.selectPolicyEventCount(queryIdList, startDate, endDate)
                    .stream()
                    .collect(Collectors.toMap(AlarmEventCountPO::getPolicyId, AlarmEventCountPO::getCount));
            // 设置缓存
            for (Long policyId : queryIdList) {
                Integer count = Objects1.def(countMap.get(policyId), 0);
                countMap.put(policyId, count);
                result.add(new AlarmPolicyAlarmCountDTO(policyId, count));
            }
            RedisMaps.putAll(cacheKey, countMap);
            // 设置过期时间
            Date currentDate = Dates.stream().clearHms().date();
            if (startDate.compareTo(currentDate) <= 0 && currentDate.compareTo(endDate) <= 0) {
                // 包含当天数据缓存 15 分钟
                RedisMaps.setExpire(cacheKey, 15, TimeUnit.MINUTES);
            } else {
                // 不包含当天的数据缓存 1 天
                RedisMaps.setExpire(cacheKey, 1, TimeUnit.DAYS);
            }
        }
        return result;
    }

    @Override
    public LambdaQueryWrapper<AlarmEventDO> buildQueryWrapper(AlarmEventQueryRequest request) {
        return alarmEventDAO.wrapper()
                .eq(AlarmEventDO::getId, request.getId())
                .eq(AlarmEventDO::getSourceType, request.getSourceType())
                .eq(AlarmEventDO::getSourceId, request.getSourceId())
                .eq(AlarmEventDO::getAgentKey, request.getAgentKey())
                .eq(AlarmEventDO::getPolicyId, request.getPolicyId())
                .eq(AlarmEventDO::getMetricsId, request.getMetricsId())
                .eq(AlarmEventDO::getMetricsMeasurement, request.getMetricsMeasurement())
                .eq(AlarmEventDO::getAlarmLevel, request.getAlarmLevel())
                .eq(AlarmEventDO::getFalseAlarm, request.getFalseAlarm())
                .eq(AlarmEventDO::getHandleStatus, request.getHandleStatus())
                .like(AlarmEventDO::getHandleRemark, request.getHandleRemark())
                .eq(AlarmEventDO::getHandleUserId, request.getHandleUserId())
                .ge(AlarmEventDO::getCreateTime, Arrays1.getIfPresent(request.getCreateTimeRange(), 0))
                .le(AlarmEventDO::getCreateTime, Arrays1.getIfPresent(request.getCreateTimeRange(), 1));
    }

}
