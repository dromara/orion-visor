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
package org.dromara.visor.module.monitor.context;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.monitor.convert.MonitorMetricsConvert;
import org.dromara.visor.module.monitor.dao.MonitorMetricsDAO;
import org.dromara.visor.module.monitor.entity.domain.MonitorMetricsDO;
import org.dromara.visor.module.monitor.entity.dto.MonitorMetricsContextDTO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控指标上下文
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/10/12 19:39
 */
@Slf4j
@Component
public class MonitorMetricsContext {

    /**
     * 监控指标缓存
     */
    private static final ConcurrentHashMap<Long, MonitorMetricsContextDTO> MONITOR_METRICS_CACHE = new ConcurrentHashMap<>();

    /**
     * 监控指标引用缓存
     */
    private static final ConcurrentHashMap<String, Long> MONITOR_METRICS_KEY_REL = new ConcurrentHashMap<>();

    @Resource
    private MonitorMetricsDAO monitorMetricsDAO;

    /**
     * 初始化监控上下文
     */
    @PostConstruct
    public void initMonitorContext() {
        // 初始化监控指标
        log.info("MetricsContext-init start.");
        this.loadMonitorMetrics();
        log.info("MetricsContext-init end.");
    }

    /**
     * 加载监控指标
     */
    public void loadMonitorMetrics() {
        MONITOR_METRICS_CACHE.clear();
        // 查询全部指标
        List<MonitorMetricsDO> metrics = monitorMetricsDAO.selectList(null);
        metrics.forEach(s -> MONITOR_METRICS_CACHE.put(s.getId(), MonitorMetricsConvert.MAPPER.toContext(s)));
        metrics.forEach(s -> MONITOR_METRICS_KEY_REL.put(this.getMonitorMetricsKey(s.getMeasurement(), s.getValue()), s.getId()));
    }


    // ----------------------- 监控指标 ----------------------

    /**
     * 重新加载监控指标
     *
     * @param id id
     */
    public void reloadMonitorMetrics(Long id) {
        // 删除指标缓存
        MONITOR_METRICS_CACHE.remove(id);
        // 删除指标引用
        MONITOR_METRICS_KEY_REL.entrySet().removeIf(entry -> entry.getValue().equals(id));
        // 重新加载指标
        MonitorMetricsDO metrics = monitorMetricsDAO.selectById(id);
        if (metrics == null) {
            return;
        }
        MONITOR_METRICS_CACHE.put(metrics.getId(), MonitorMetricsConvert.MAPPER.toContext(metrics));
        MONITOR_METRICS_KEY_REL.put(this.getMonitorMetricsKey(metrics.getMeasurement(), metrics.getValue()), metrics.getId());
    }

    /**
     * 获取监控指标
     *
     * @param id id
     * @return cache
     */
    public MonitorMetricsContextDTO getMonitorMetrics(Long id) {
        return MONITOR_METRICS_CACHE.get(id);
    }

    /**
     * 获取监控指标
     *
     * @param measurement measurement
     * @param field       field
     * @return cache
     */
    public MonitorMetricsContextDTO getMonitorMetrics(String measurement, String field) {
        Long id = MONITOR_METRICS_KEY_REL.get(this.getMonitorMetricsKey(measurement, field));
        if (id == null) {
            return null;
        }
        return MONITOR_METRICS_CACHE.get(id);
    }

    /**
     * 获取监控指标 id
     *
     * @param measurement measurement
     * @param field       field
     * @return id
     */
    public Long getMonitorMetricsId(String measurement, String field) {
        return MONITOR_METRICS_KEY_REL.get(this.getMonitorMetricsKey(measurement, field));
    }

    /**
     * 获取监控指标 key
     *
     * @param measurement measurement
     * @param field       field
     * @return key
     */
    private String getMonitorMetricsKey(String measurement, String field) {
        return measurement + "_" + field;
    }

}
