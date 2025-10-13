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
import cn.orionsec.kit.lang.function.Functions;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.common.entity.chart.TimeChartSeries;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.influxdb.core.query.FluxQueryBuilder;
import org.dromara.visor.framework.influxdb.core.utils.InfluxdbUtils;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.module.asset.api.HostAgentApi;
import org.dromara.visor.module.asset.api.HostApi;
import org.dromara.visor.module.asset.entity.dto.host.HostAgentLogDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostQueryDTO;
import org.dromara.visor.module.asset.enums.AgentOnlineStatusEnum;
import org.dromara.visor.module.infra.api.SystemUserApi;
import org.dromara.visor.module.monitor.constant.MetricsConst;
import org.dromara.visor.module.monitor.context.MonitorAgentContext;
import org.dromara.visor.module.monitor.convert.MonitorHostConvert;
import org.dromara.visor.module.monitor.dao.AlarmPolicyDAO;
import org.dromara.visor.module.monitor.dao.MonitorHostDAO;
import org.dromara.visor.module.monitor.entity.domain.AlarmPolicyDO;
import org.dromara.visor.module.monitor.entity.domain.MonitorHostDO;
import org.dromara.visor.module.monitor.entity.dto.*;
import org.dromara.visor.module.monitor.entity.request.host.*;
import org.dromara.visor.module.monitor.entity.vo.MonitorHostMetricsDataVO;
import org.dromara.visor.module.monitor.entity.vo.MonitorHostVO;
import org.dromara.visor.module.monitor.enums.AlarmSwitchEnum;
import org.dromara.visor.module.monitor.enums.MeasurementEnum;
import org.dromara.visor.module.monitor.service.MonitorHostService;
import org.dromara.visor.module.monitor.service.MonitorMetricsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 监控主机 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-8-14 16:27
 */
@Slf4j
@Service
public class MonitorHostServiceImpl implements MonitorHostService {

    @Resource
    private MonitorHostDAO monitorHostDAO;

    @Resource
    private AlarmPolicyDAO alarmPolicyDAO;

    @Resource
    private HostApi hostApi;

    @Resource
    private HostAgentApi hostAgentApi;

    @Resource
    private SystemUserApi systemUserApi;

    @Resource
    private MonitorMetricsService monitorMetricsService;

    @Resource
    private MonitorAgentContext monitorAgentContext;

    @Override
    public DataGrid<MonitorHostVO> getMonitorHostPage(MonitorHostQueryRequest request) {
        // 转换查询条件
        HostQueryDTO hostQuery = MonitorHostConvert.MAPPER.toHostQuery(request);
        hostQuery.setQueryTag(true);
        hostQuery.setOrderByAgent(true);
        List<MonitorHostDO> monitorHosts = null;
        // 查询监控主机数据
        Integer alarmSwitch = request.getAlarmSwitch();
        Long ownerUserId = request.getOwnerUserId();
        Long policyId = request.getPolicyId();
        if (!Objects1.isAllNull(alarmSwitch, ownerUserId, policyId)) {
            monitorHosts = monitorHostDAO.of()
                    .createValidateWrapper()
                    .eq(MonitorHostDO::getAlarmSwitch, alarmSwitch)
                    .eq(MonitorHostDO::getOwnerUserId, ownerUserId)
                    .eq(MonitorHostDO::getPolicyId, policyId)
                    .then()
                    .list();
            if (monitorHosts.isEmpty()) {
                return new DataGrid<>();
            }
            hostQuery.setIdList(Lists.map(monitorHosts, MonitorHostDO::getHostId));
        }
        // 查询主机
        DataGrid<HostDTO> hosts = hostApi.getHostPage(hostQuery);
        if (hosts.isEmpty()) {
            return new DataGrid<>(Lists.empty(), hosts.getTotal());
        }
        List<Long> hostIdList = hosts.stream()
                .map(HostDTO::getId)
                .collect(Collectors.toList());
        // 若未查询过监控主机表则查询
        if (monitorHosts == null) {
            monitorHosts = monitorHostDAO.selectByHostIdList(hostIdList);
        }
        Map<Long, MonitorHostDO> monitorHostMap = monitorHosts.stream()
                .collect(Collectors.toMap(MonitorHostDO::getHostId,
                        Function.identity(),
                        Functions.right()));
        // 查询安装日志
        Map<Long, HostAgentLogDTO> agentInstallLogMap = hostAgentApi.selectAgentInstallLog(hostIdList)
                .stream()
                .collect(Collectors.toMap(HostAgentLogDTO::getHostId,
                        Function.identity(),
                        Functions.right()));
        String latestVersion = hostAgentApi.getAgentVersion();
        // 给主机进行赋值
        DataGrid<MonitorHostVO> dataGrid = hosts.map(s -> {
            MonitorHostVO vo = MonitorHostConvert.MAPPER.to(s);
            // 设置监控信息
            MonitorHostDO monitorHost = monitorHostMap.get(s.getId());
            if (monitorHost != null) {
                vo.setId(monitorHost.getId());
                vo.setPolicyId(monitorHost.getPolicyId());
                vo.setAlarmSwitch(monitorHost.getAlarmSwitch());
                vo.setOwnerUserId(monitorHost.getOwnerUserId());
                vo.setOwnerUsername(monitorHost.getOwnerUsername());
                // 反序列化元数据
                vo.setMeta(JSON.parseObject(monitorHost.getMonitorMeta(), MonitorHostMetaDTO.class));
                // 反序列化配置
                vo.setConfig(JSON.parseObject(monitorHost.getMonitorConfig(), MonitorHostConfigDTO.class));
            }
            // 设置安装日志
            vo.setInstallLog(agentInstallLogMap.get(s.getId()));
            // 设置最新版本
            vo.setLatestVersion(latestVersion);
            // 设置指标信息
            if (AgentOnlineStatusEnum.ONLINE.getValue().equals(vo.getAgentOnlineStatus())) {
                vo.setMetricsData(this.getHostMetricsData(vo.getAgentKey(), vo.getConfig()));
            } else {
                vo.setMetricsData(MonitorHostMetricsDataVO.noData(vo.getAgentKey()));
            }
            return vo;
        });
        // 查询策略名称
        this.setAlarmPolicyName(dataGrid.getRows());
        return dataGrid;
    }

    @Override
    public List<MonitorHostMetricsDataVO> getMonitorHostMetrics(List<String> agentKeys) {
        return agentKeys.stream()
                .map(s -> this.getHostMetricsData(s, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<TimeChartSeries> getMonitorHostChart(MonitorHostChartRequest request) {
        List<String> agentKeys = request.getAgentKeys();
        List<String> fields = request.getFields();
        List<TimeChartSeries> seriesList = this.getChartSeries(request);
        // 查询 agentKey 对应的名称
        Map<String, String> cacheNameByAgentKey = hostAgentApi.getNameCacheByAgentKey(agentKeys);
        // 封装数据
        for (TimeChartSeries series : seriesList) {
            Map<String, Object> tags = series.getTags();
            Map<String, Object> sortedTags = new LinkedHashMap<>();
            String key = (String) tags.get(Const.KEY);
            String field = monitorMetricsService.getMetricName(request.getMeasurement(), (String) tags.get(Const.FIELD));
            tags.remove(Const.KEY);
            tags.remove(Const.FIELD);
            // 设置主机名称
            if (agentKeys.size() > 1) {
                sortedTags.put(ExtraFieldConst.HOST_NAME, cacheNameByAgentKey.get(key));
            }
            // 设置字段
            if (fields.size() > 1) {
                sortedTags.put(ExtraFieldConst.FIELD, field);
            }
            sortedTags.putAll(tags);
            // 为空需要添加 field (计算名称)
            if (sortedTags.isEmpty()) {
                sortedTags.put(ExtraFieldConst.FIELD, field);
            }
            series.setTags(sortedTags);
            // 设置名称
            String name = sortedTags.values()
                    .stream()
                    .map(Objects::toString)
                    .collect(Collectors.joining("-"));
            series.setName(name);
        }
        // 排序指标
        seriesList.sort(Comparator.comparing(TimeChartSeries::getName));
        return seriesList;
    }

    @Override
    public List<String> getMonitorHostTags(MonitorHostQueryTagRequest request) {
        MeasurementEnum measurementEnum = MeasurementEnum.of(request.getMeasurement());
        if (measurementEnum == null) {
            return Collections.emptyList();
        }
        // 映射数据
        Function<MonitorHostMetaDTO, List<String>> tagsGetter;
        if (MeasurementEnum.CPU.equals(measurementEnum)) {
            tagsGetter = MonitorHostMetaDTO::getCpus;
        } else if (MeasurementEnum.DISK.equals(measurementEnum)) {
            tagsGetter = MonitorHostMetaDTO::getDisks;
        } else if (MeasurementEnum.NETWORK.equals(measurementEnum)) {
            tagsGetter = MonitorHostMetaDTO::getNets;
        } else {
            return Collections.emptyList();
        }
        // 查询监控主机元数据
        List<MonitorHostMetaDTO> metas = monitorHostDAO.of()
                .createValidateWrapper()
                .eq(MonitorHostDO::getPolicyId, request.getPolicyId())
                .in(MonitorHostDO::getAgentKey, request.getAgentKeys())
                .then()
                .stream()
                .map(MonitorHostDO::getMonitorMeta)
                .filter(Objects::nonNull)
                .map(s -> JSON.parseObject(s, MonitorHostMetaDTO.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 获取 tag
        return metas.stream()
                .map(tagsGetter)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateMonitorHostById(MonitorHostUpdateRequest request) {
        Long id = Assert.notNull(request.getId(), ErrorMessage.ID_MISSING);
        Long policyId = request.getPolicyId();
        log.info("MonitorHostService-updateMonitorHostById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询数据
        MonitorHostDO monitorHost = monitorHostDAO.selectById(id);
        Assert.notNull(monitorHost, ErrorMessage.DATA_ABSENT);
        // 查询主机信息
        HostDTO host = hostApi.selectById(monitorHost.getHostId());
        Assert.notNull(host, ErrorMessage.HOST_ABSENT);
        // 查询用户信息
        Optional.ofNullable(request.getOwnerUserId())
                .map(systemUserApi::getUsernameById)
                .ifPresent(request::setOwnerUsername);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.NAME, host.getName());
        // 查询策略是否存在
        if (policyId != null) {
            Assert.notNull(alarmPolicyDAO.selectById(policyId), ErrorMessage.ALARM_POLICY_ABSENT);
        }
        // 转换
        MonitorHostDO updateRecord = MonitorHostConvert.MAPPER.to(request);
        // 配置信息
        MonitorHostConfigDTO config = MonitorHostConfigDTO.builder()
                .cpuName(request.getCpuName())
                .diskName(request.getDiskName())
                .networkName(request.getNetworkName())
                .build();
        updateRecord.setMonitorConfig(JSON.toJSONString(config));
        // 更新
        int effect = monitorHostDAO.updateById(updateRecord);
        // 更新策略为空
        if (policyId == null) {
            monitorHostDAO.setPolicyIdWithNullById(id);
        }
        // 重新加载监控主机上下文
        monitorAgentContext.reloadMonitorHost(host.getAgentKey());
        log.info("MonitorHostService-updateMonitorHostById effect: {}", effect);
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateMonitorHostAlarmSwitch(MonitorHostSwitchUpdateRequest request) {
        List<Long> idList = request.getIdList();
        AlarmSwitchEnum alarmSwitch = AlarmSwitchEnum.of(request.getAlarmSwitch());
        // 查询数据
        List<MonitorHostDO> monitorHostList = monitorHostDAO.selectBatchIds(idList);
        Assert.notEmpty(monitorHostList, ErrorMessage.DATA_ABSENT);
        // 查询主机信息
        List<HostDTO> hostList = hostApi.selectByIdList(Lists.map(monitorHostList, MonitorHostDO::getHostId));
        Assert.notEmpty(hostList, ErrorMessage.HOST_ABSENT);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, hostList.size());
        OperatorLogs.add(OperatorLogs.SWITCH, alarmSwitch.name());
        // 修改数据
        MonitorHostDO update = new MonitorHostDO();
        update.setAlarmSwitch(alarmSwitch.getValue());
        int effect = monitorHostDAO.update(update, Conditions.in(MonitorHostDO::getId, idList));
        log.info("MonitorHostService-updateMonitorHostAlarmSwitch effect: {}", effect);
        // 更新缓存
        for (HostDTO host : hostList) {
            monitorAgentContext.reloadMonitorHost(host.getAgentKey());
        }
        return effect;
    }

    @Override
    public Integer deleteByHostIdList(List<Long> hostIdList) {
        log.info("MonitorHostService.deleteByHostIdList start hostIdList: {}", hostIdList);
        if (Lists.isEmpty(hostIdList)) {
            return Const.N_0;
        }
        // 通过 hostId 查询
        List<MonitorHostDO> hosts = monitorHostDAO.selectByHostIdList(hostIdList);
        // 删除
        int effect = monitorHostDAO.deleteByHostIdList(hostIdList);
        // 删除缓存
        hosts.forEach(s -> monitorAgentContext.removeMonitorHost(s.getAgentKey()));
        log.info("MonitorHostService.deleteByHostIdList finish effect: {}", effect);
        return effect;
    }

    /**
     * 设置告警策略名称
     *
     * @param list list
     */
    private void setAlarmPolicyName(List<MonitorHostVO> list) {
        List<Long> policyIdList = list.stream()
                .map(MonitorHostVO::getPolicyId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (policyIdList.isEmpty()) {
            return;
        }
        // 查询告警策略
        Map<Long, String> policyMap = alarmPolicyDAO.of()
                .createWrapper()
                .select(AlarmPolicyDO::getId, AlarmPolicyDO::getName)
                .in(AlarmPolicyDO::getId, policyIdList)
                .then()
                .stream()
                .collect(Collectors.toMap(AlarmPolicyDO::getId, AlarmPolicyDO::getName));
        list.forEach(host -> {
            host.setPolicyName(policyMap.get(host.getPolicyId()));
        });
    }

    /**
     * 查询数据图表
     *
     * @param request request
     * @return values
     */
    private List<TimeChartSeries> getChartSeries(MonitorHostChartRequest request) {
        String measurement = request.getMeasurement();
        List<String> agentKeys = request.getAgentKeys();
        List<String> fields = request.getFields();
        // 获取配置信息
        List<MonitorHostConfigDTO> configList = agentKeys.stream()
                .map(monitorAgentContext::getMonitorHost)
                .map(MonitorHostContextDTO::getConfig)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 查询主机信息
        FluxQueryBuilder query = InfluxdbUtils.query();
        // 设置时间
        String range = request.getRange();
        if (range != null) {
            query.range(range);
        } else {
            Assert.notNull(request.getStart(), ErrorMessage.PARAM_MISSING);
            Assert.notNull(request.getEnd(), ErrorMessage.PARAM_MISSING);
            query.range(request.getStart(), request.getEnd());
        }
        // 设置名称
        Set<String> names = null;
        if (MeasurementEnum.CPU.getMeasurement().equals(measurement)) {
            names = configList.stream()
                    .map(MonitorHostConfigDTO::getCpuName)
                    .collect(Collectors.toSet());
        } else if (MeasurementEnum.DISK.getMeasurement().equals(measurement)) {
            names = configList.stream()
                    .map(MonitorHostConfigDTO::getDiskName)
                    .collect(Collectors.toSet());
        } else if (MeasurementEnum.NETWORK.getMeasurement().equals(measurement)) {
            names = configList.stream()
                    .map(MonitorHostConfigDTO::getNetworkName)
                    .collect(Collectors.toSet());
        }
        if (!Lists.isEmpty(names)) {
            query.name(names);
        }
        // 设置其他查询条件
        String flux = query.measurement(measurement)
                .key(agentKeys)
                .fields(fields)
                .aggregateWindow(request.getWindow(), request.getAggregate(), true)
                .pretty()
                .build();
        // 查询数据
        return InfluxdbUtils.querySeries(flux);
    }

    /**
     * 获取主机指标数据
     *
     * @param agentKey agentKey
     * @param config   config
     * @return data
     */
    public MonitorHostMetricsDataVO getHostMetricsData(String agentKey, MonitorHostConfigDTO config) {
        AgentMetricsDataDTO metrics = monitorAgentContext.getAgentMetrics(agentKey);
        // 无数据
        if (metrics == null) {
            return MonitorHostMetricsDataVO.noData(agentKey);
        }
        // 从缓存中获取配置
        if (config == null) {
            config = Optional.of(agentKey)
                    .map(monitorAgentContext::getMonitorHost)
                    .map(MonitorHostContextDTO::getConfig)
                    .orElse(null);
        }
        // 获取名称
        LinkedHashMap<String, String> metricDefaultNameMap = metrics.getMetrics()
                .stream()
                .collect(Collectors.toMap(
                        AgentMetricsDTO::getType,
                        s -> Strings.def(Maps.get(s.getTags(), Const.NAME)),
                        Functions.left(),
                        LinkedHashMap::new));
        String cpuName = Optional.ofNullable(config)
                .map(MonitorHostConfigDTO::getCpuName)
                .orElse(metricDefaultNameMap.get(MeasurementEnum.CPU.getMeasurement()));
        String diskName = Optional.ofNullable(config)
                .map(MonitorHostConfigDTO::getDiskName)
                .orElse(metricDefaultNameMap.get(MeasurementEnum.DISK.getMeasurement()));
        String networkName = Optional.ofNullable(config)
                .map(MonitorHostConfigDTO::getNetworkName)
                .orElse(metricDefaultNameMap.get(MeasurementEnum.NETWORK.getMeasurement()));
        // 指标缓存
        Map<String, JSONObject> metricTypeMap = metrics.getMetrics()
                .stream()
                .collect(Collectors.toMap(
                        s -> s.getType() + "_" + Strings.def(Maps.get(s.getTags(), Const.NAME)),
                        AgentMetricsDTO::getValues,
                        Functions.right(),
                        LinkedHashMap::new));
        MonitorHostMetricsDataVO data = MonitorHostMetricsDataVO.builder()
                .noData(false)
                .agentKey(agentKey)
                .cpuName(cpuName)
                .diskName(diskName)
                .networkName(networkName)
                .timestamp(metrics.getTimestamp())
                .build();
        // 组装指标
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.CPU,
                cpuName, MetricsConst.CPU_TOTAL_SECONDS_TOTAL, 0D,
                JSONObject::getDouble,
                data::setCpuUsagePercent);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.MEMORY,
                null, MetricsConst.MEM_USED_BYTES_TOTAL, 0L,
                JSONObject::getLong,
                data::setMemoryUsageBytes);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.MEMORY,
                null, MetricsConst.MEM_USED_PERCENT, 0D,
                JSONObject::getDouble,
                data::setMemoryUsagePercent);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.LOAD,
                null, MetricsConst.LOAD1, 0D,
                JSONObject::getDouble,
                data::setLoad1);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.LOAD,
                null, MetricsConst.LOAD5, 0D,
                JSONObject::getDouble,
                data::setLoad5);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.LOAD,
                null, MetricsConst.LOAD15, 0D,
                JSONObject::getDouble,
                data::setLoad15);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.DISK,
                diskName, MetricsConst.DISK_FS_USED_PERCENT, 0D,
                JSONObject::getDouble,
                data::setDiskUsagePercent);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.DISK,
                diskName, MetricsConst.DISK_FS_USED_BYTES_TOTAL, 0L,
                JSONObject::getLong,
                data::setDiskUsageBytes);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.NETWORK,
                networkName, MetricsConst.NET_SENT_BYTES_PER_SECOND, 0D,
                JSONObject::getDouble,
                data::setNetworkSentPreBytes);
        this.setNamedMetricValue(metricTypeMap, MeasurementEnum.NETWORK,
                networkName, MetricsConst.NET_RECV_BYTES_PER_SECOND, 0D,
                JSONObject::getDouble,
                data::setNetworkRecvPreBytes);
        return data;
    }

    /**
     * 设置指标值
     *
     * @param metricTypeMap metricTypeMap
     * @param measurement   measurement
     * @param name          name
     * @param field         field
     * @param defaultValue  defaultValue
     * @param getter        getter
     * @param setter        setter
     * @param <T>           T
     */
    private <T> void setNamedMetricValue(Map<String, JSONObject> metricTypeMap,
                                         MeasurementEnum measurement,
                                         String name,
                                         String field,
                                         T defaultValue,
                                         BiFunction<JSONObject, String, T> getter,
                                         Consumer<T> setter) {
        // 获取值
        T value = Optional.of(measurement.getMeasurement() + "_" + Strings.def(name))
                .map(metricTypeMap::get)
                .map(s -> getter.apply(s, field))
                .orElse(defaultValue);
        // 设置值
        setter.accept(value);
    }

}
