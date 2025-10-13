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

import cn.orionsec.kit.lang.able.Executable;
import cn.orionsec.kit.lang.annotation.Keep;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import com.alibaba.fastjson.JSON;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.common.utils.LockerUtils;
import org.dromara.visor.framework.influxdb.core.utils.InfluxdbUtils;
import org.dromara.visor.module.asset.api.HostApi;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.infra.api.SystemUserApi;
import org.dromara.visor.module.monitor.context.MonitorAgentContext;
import org.dromara.visor.module.monitor.dao.MonitorHostDAO;
import org.dromara.visor.module.monitor.entity.domain.MonitorHostDO;
import org.dromara.visor.module.monitor.entity.dto.AgentMetricsDataDTO;
import org.dromara.visor.module.monitor.entity.dto.HostMetaDTO;
import org.dromara.visor.module.monitor.entity.dto.MonitorHostConfigDTO;
import org.dromara.visor.module.monitor.enums.AlarmSwitchEnum;
import org.dromara.visor.module.monitor.handler.alarm.IAlarmEngine;
import org.dromara.visor.module.monitor.service.MonitorAgentEndpointService;
import org.dromara.visor.module.monitor.utils.MetricsUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 监控探针端点 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/22 14:42
 */
@Slf4j
@Service
public class MonitorAgentEndpointServiceImpl implements MonitorAgentEndpointService {

    private static final String LOCK_KEY_PREFIX = "monitor:online:";

    @Resource
    private MonitorHostDAO monitorHostDAO;

    @Resource
    private HostApi hostApi;

    @Resource
    private SystemUserApi systemUserApi;

    @Resource
    private MonitorAgentContext monitorAgentContext;

    @Keep
    @Resource
    private IAlarmEngine metricsAlarmEngine;

    @Override
    @Async("metricsExecutor")
    public void addMetrics(String agentKey, AgentMetricsDataDTO newMetrics) {
        log.info("MonitorAgentEndpointService.addMetrics start agentKey: {}", agentKey);
        // 设置数据缓存
        AgentMetricsDataDTO prevMetrics = monitorAgentContext.getAgentMetrics(agentKey);
        monitorAgentContext.setAgentMetrics(agentKey, newMetrics);
        // 数据点
        List<Point> points = newMetrics.getMetrics()
                .stream()
                .map(s -> MetricsUtils.createPoint(s.getType(), s.getValues())
                        .addTag(Const.KEY, agentKey)
                        .addTags(Maps.def(s.getTags(), Maps.empty()))
                        .time(newMetrics.getTimestamp(), WritePrecision.MS))
                .collect(Collectors.toList());
        // 写入数据点
        InfluxdbUtils.writePoints(points);
        // 告警
        metricsAlarmEngine.checkAndAlarm(agentKey, prevMetrics, newMetrics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncHostMeta(String agentKey, HostMetaDTO meta) {
        // 同步逻辑
        Executable exec = () -> {
            // 查询主机是否存在
            HostDTO host = hostApi.selectByAgentKey(agentKey);
            Assert.notNull(host, ErrorMessage.HOST_ABSENT);
            // 查询数据
            MonitorHostDO monitorHost = monitorHostDAO.selectByAgentKey(agentKey);
            MonitorHostConfigDTO newConfig = null;
            if (monitorHost == null) {
                // 不存在则新增
                newConfig = this.getDefaultMonitorConfig(meta);
                monitorHost = MonitorHostDO.builder()
                        .hostId(host.getId())
                        .agentKey(agentKey)
                        .alarmSwitch(AlarmSwitchEnum.OFF.getValue())
                        .monitorMeta(JSON.toJSONString(meta))
                        .monitorConfig(JSON.toJSONString(newConfig))
                        .creator(host.getCreator())
                        .updater(host.getCreator())
                        .build();
                // 设置负责人信息
                Long userId = systemUserApi.getIdByUsername(host.getCreator());
                if (userId != null) {
                    monitorHost.setOwnerUserId(userId);
                    monitorHost.setOwnerUsername(host.getCreator());
                }
                monitorHostDAO.insert(monitorHost);
            } else {
                // 更新数据
                MonitorHostDO update = new MonitorHostDO();
                update.setId(monitorHost.getId());
                update.setMonitorMeta(JSON.toJSONString(meta));
                // 设置默认配置
                if (Strings.isBlank(monitorHost.getMonitorConfig())) {
                    newConfig = this.getDefaultMonitorConfig(meta);
                    update.setMonitorConfig(JSON.toJSONString(newConfig));
                }
                monitorHostDAO.updateById(update);
            }
            // 重新加载监控主机上下文
            if (newConfig != null) {
                monitorAgentContext.reloadMonitorHost(agentKey);
            }
        };
        // 获取锁并执行同步逻辑
        LockerUtils.lockExecute(LOCK_KEY_PREFIX + agentKey, Const.MS_S_10, exec);
    }

    /**
     * 获取默认监控配置
     *
     * @param meta meta
     * @return config
     */
    private MonitorHostConfigDTO getDefaultMonitorConfig(HostMetaDTO meta) {
        return MonitorHostConfigDTO.builder()
                .cpuName(Lists.first(meta.getCpus()))
                .diskName(Lists.first(meta.getDisks()))
                .networkName(Lists.first(meta.getNets()))
                .build();
    }

}
