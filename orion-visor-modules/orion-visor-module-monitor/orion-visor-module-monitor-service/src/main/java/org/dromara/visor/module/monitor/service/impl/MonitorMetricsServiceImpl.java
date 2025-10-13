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
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisMaps;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.monitor.context.MonitorMetricsContext;
import org.dromara.visor.module.monitor.convert.MonitorMetricsConvert;
import org.dromara.visor.module.monitor.dao.MonitorMetricsDAO;
import org.dromara.visor.module.monitor.define.cache.MonitorMetricsCacheKeyDefine;
import org.dromara.visor.module.monitor.entity.domain.MonitorMetricsDO;
import org.dromara.visor.module.monitor.entity.dto.MonitorMetricsCacheDTO;
import org.dromara.visor.module.monitor.entity.dto.MonitorMetricsContextDTO;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsCreateRequest;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsQueryRequest;
import org.dromara.visor.module.monitor.entity.request.metrics.MonitorMetricsUpdateRequest;
import org.dromara.visor.module.monitor.entity.vo.MonitorMetricsVO;
import org.dromara.visor.module.monitor.service.AlarmPolicyRuleService;
import org.dromara.visor.module.monitor.service.MonitorMetricsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 监控指标 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-12 21:31
 */
@Slf4j
@Service
public class MonitorMetricsServiceImpl implements MonitorMetricsService {

    @Resource
    private MonitorMetricsDAO monitorMetricsDAO;

    @Resource
    private AlarmPolicyRuleService alarmPolicyRuleService;

    @Resource
    private MonitorMetricsContext monitorMetricsContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMonitorMetrics(MonitorMetricsCreateRequest request) {
        log.info("MonitorMetricsService-createMonitorMetrics request: {}", JSON.toJSONString(request));
        // 转换
        MonitorMetricsDO record = MonitorMetricsConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkMonitorMetricsPresent(record);
        // 插入
        int effect = monitorMetricsDAO.insert(record);
        Long id = record.getId();
        // 删除缓存
        RedisMaps.delete(MonitorMetricsCacheKeyDefine.MONITOR_METRICS);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.ID, id);
        // 重新加载本地缓存
        monitorMetricsContext.reloadMonitorMetrics(id);
        log.info("MonitorMetricsService-createMonitorMetrics id: {}, effect: {}", id, effect);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateMonitorMetricsById(MonitorMetricsUpdateRequest request) {
        Long id = Assert.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("MonitorMetricsService-updateMonitorMetricsById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        MonitorMetricsDO record = monitorMetricsDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        MonitorMetricsDO updateRecord = MonitorMetricsConvert.MAPPER.to(request);
        updateRecord.setMeasurement(record.getMeasurement());
        // 查询数据是否冲突
        this.checkMonitorMetricsPresent(updateRecord);
        // 更新
        int effect = monitorMetricsDAO.updateById(updateRecord);
        log.info("MonitorMetricsService-updateMonitorMetricsById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(MonitorMetricsCacheKeyDefine.MONITOR_METRICS);
        // 重新加载本地缓存
        monitorMetricsContext.reloadMonitorMetrics(id);
        return effect;
    }

    @Override
    public List<MonitorMetricsVO> getMonitorMetricsList() {
        // 查询缓存
        List<MonitorMetricsCacheDTO> list = RedisMaps.valuesJson(MonitorMetricsCacheKeyDefine.MONITOR_METRICS);
        if (list.isEmpty()) {
            // 查询数据库
            list = monitorMetricsDAO.of().list(MonitorMetricsConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, MonitorMetricsCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(MonitorMetricsCacheKeyDefine.MONITOR_METRICS, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(MonitorMetricsConvert.MAPPER::to)
                .sorted(Comparator.comparing(MonitorMetricsVO::getId))
                .collect(Collectors.toList());
    }

    @Override
    public DataGrid<MonitorMetricsVO> getMonitorMetricsPage(MonitorMetricsQueryRequest request) {
        // 条件
        LambdaQueryWrapper<MonitorMetricsDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return monitorMetricsDAO.of()
                .wrapper(wrapper)
                .page(request)
                .order(request, MonitorMetricsDO::getId)
                .dataGrid(MonitorMetricsConvert.MAPPER::to);
    }

    @Override
    public String getMetricName(String measurement, String value) {
        MonitorMetricsContextDTO metrics = monitorMetricsContext.getMonitorMetrics(measurement, value);
        if (metrics == null) {
            return value;
        }
        return metrics.getName();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteMonitorMetricsById(Long id) {
        log.info("MonitorMetricsService-deleteMonitorMetricsById id: {}", id);
        // 检查数据是否存在
        MonitorMetricsDO record = monitorMetricsDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = monitorMetricsDAO.deleteById(id);
        // 删除规则
        alarmPolicyRuleService.deleteByMetricsId(id);
        // 删除缓存
        RedisMaps.delete(MonitorMetricsCacheKeyDefine.MONITOR_METRICS, id);
        // 重新加载本地缓存
        monitorMetricsContext.reloadMonitorMetrics(id);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.NAME, record.getName());
        log.info("MonitorMetricsService-deleteMonitorMetricsById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public LambdaQueryWrapper<MonitorMetricsDO> buildQueryWrapper(MonitorMetricsQueryRequest request) {
        return monitorMetricsDAO.wrapper()
                .like(MonitorMetricsDO::getName, request.getName())
                .eq(MonitorMetricsDO::getMeasurement, request.getMeasurement())
                .eq(MonitorMetricsDO::getValue, request.getValue())
                .like(MonitorMetricsDO::getDescription, request.getDescription());
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkMonitorMetricsPresent(MonitorMetricsDO domain) {
        // 构造条件
        LambdaQueryWrapper<MonitorMetricsDO> wrapper = monitorMetricsDAO.wrapper()
                // 更新时忽略当前记录
                .ne(MonitorMetricsDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(MonitorMetricsDO::getMeasurement, domain.getMeasurement())
                .eq(MonitorMetricsDO::getValue, domain.getValue());
        // 检查是否存在
        boolean present = monitorMetricsDAO.of(wrapper).present();
        Assert.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
