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

import cn.orionsec.kit.lang.define.cache.TimedCache;
import cn.orionsec.kit.lang.define.cache.TimedCacheBuilder;
import cn.orionsec.kit.lang.utils.io.Streams;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.monitor.convert.MonitorHostConvert;
import org.dromara.visor.module.monitor.dao.MonitorHostDAO;
import org.dromara.visor.module.monitor.entity.domain.MonitorHostDO;
import org.dromara.visor.module.monitor.entity.dto.AgentMetricsDataDTO;
import org.dromara.visor.module.monitor.entity.dto.MonitorHostConfigDTO;
import org.dromara.visor.module.monitor.entity.dto.MonitorHostContextDTO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控探针上下文
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/21 17:26
 */
@Slf4j
@Component
public class MonitorAgentContext {

    /**
     * 监控主机缓存
     */
    private static final ConcurrentHashMap<String, MonitorHostContextDTO> MONITOR_HOST_CACHE = new ConcurrentHashMap<>();

    /**
     * 最后心跳时间缓存 3min
     */
    private static final TimedCache<Long> AGENT_LAST_ACTIVE_TIME = TimedCacheBuilder.<Long>create()
            .expireAfter(3 * Const.MS_S_60)
            .checkInterval(Const.MS_S_60)
            .build();

    /**
     * 最新指标数据缓存 5min
     */
    private static final TimedCache<AgentMetricsDataDTO> LATEST_METRICS_CACHE = TimedCacheBuilder.<AgentMetricsDataDTO>create()
            .expireAfter(5 * Const.MS_S_60)
            .checkInterval(Const.MS_S_60)
            .build();

    @Resource
    private MonitorHostDAO monitorHostDAO;

    /**
     * 初始化监控上下文
     */
    @PostConstruct
    public void initMonitorContext() {
        // 初始化监控主机
        log.info("MonitorContext-init hosts start.");
        this.loadMonitorHost();
        log.info("MonitorContext-init hosts end.");
    }

    @PreDestroy
    public void destroyMonitorContext() {
        Streams.close(AGENT_LAST_ACTIVE_TIME);
        Streams.close(LATEST_METRICS_CACHE);
    }

    /**
     * 加载监控主机
     */
    private void loadMonitorHost() {
        MONITOR_HOST_CACHE.clear();
        // 查询全部主机
        List<MonitorHostDO> hosts = monitorHostDAO.selectList(null);
        // 设置缓存
        for (MonitorHostDO host : hosts) {
            this.setMonitorHost(host.getAgentKey(), host);
        }
    }

    // ----------------------- 监控主机 ----------------------

    /**
     * 获取监控主机
     *
     * @param agentKey agentKey
     * @return host
     */
    public MonitorHostContextDTO getMonitorHost(String agentKey) {
        return MONITOR_HOST_CACHE.get(agentKey);
    }

    /**
     * 重新加载监控主机
     *
     * @param agentKey agentKey
     */
    public void reloadMonitorHost(String agentKey) {
        this.setMonitorHost(agentKey, monitorHostDAO.selectByAgentKey(agentKey));
    }

    /**
     * 移除监控主机
     *
     * @param agentKey agentKey
     */
    public void removeMonitorHost(String agentKey) {
        MONITOR_HOST_CACHE.remove(agentKey);
    }

    /**
     * 设置监控主机
     *
     * @param agentKey agentKey
     * @param host     host
     */
    private void setMonitorHost(String agentKey, MonitorHostDO host) {
        MONITOR_HOST_CACHE.remove(agentKey);
        if (host == null) {
            return;
        }
        MonitorHostContextDTO context = MonitorHostConvert.MAPPER.toContext(host);
        context.setConfig(JSON.parseObject(host.getMonitorConfig(), MonitorHostConfigDTO.class));
        MONITOR_HOST_CACHE.put(agentKey, context);
    }

    // ----------------------- 指标数据 ----------------------

    /**
     * 设置指标数据
     *
     * @param agentKey agentKey
     * @param metrics  指标
     */
    public void setAgentMetrics(String agentKey, AgentMetricsDataDTO metrics) {
        // 设置指标数据
        LATEST_METRICS_CACHE.put(agentKey, metrics);
        // 更新心跳时间
        AGENT_LAST_ACTIVE_TIME.put(agentKey, System.currentTimeMillis());
    }

    /**
     * 获取指标数据
     *
     * @param agentKey agentKey
     * @return key
     */
    public AgentMetricsDataDTO getAgentMetrics(String agentKey) {
        return LATEST_METRICS_CACHE.get(agentKey);
    }

    // ----------------------- 在线状态 ----------------------

    /**
     * 设置已下线
     *
     * @param agentKey agentKey
     */
    public void setAgentOffline(String agentKey) {
        // 删除指标数据
        LATEST_METRICS_CACHE.remove(agentKey);
        // 删除心跳时间
        AGENT_LAST_ACTIVE_TIME.remove(agentKey);
    }

    /**
     * 是否在线
     *
     * @param agentKey agentKey
     * @return online
     */
    public boolean isAgentOnline(String agentKey) {
        if (agentKey == null) {
            return false;
        }
        return AGENT_LAST_ACTIVE_TIME.get(agentKey) != null;
    }

}
