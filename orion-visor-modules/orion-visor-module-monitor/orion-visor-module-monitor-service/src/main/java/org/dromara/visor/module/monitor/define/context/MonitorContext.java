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
package org.dromara.visor.module.monitor.define.context;

import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.module.monitor.dao.MonitorHostDAO;
import org.dromara.visor.module.monitor.entity.domain.MonitorHostDO;
import org.dromara.visor.module.monitor.entity.dto.MetricsDataDTO;
import org.dromara.visor.module.monitor.entity.dto.MonitorHostConfigDTO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控上下文
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/21 17:26
 */
@Slf4j
@Component
public class MonitorContext {

    private static final int MAX_CACHE_TIME = 5 * 60 * 1000; // 5min

    private static final int CLEAN_INTERVAL = 60 * 1000; // 1min

    private static final ConcurrentHashMap<String, MetricsDataDTO> LATEST_METRICS_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, MonitorHostConfigDTO> HOST_CONFIG_CACHE = new ConcurrentHashMap<>();

    private long lastCleanTime;

    @Resource
    private MonitorHostDAO monitorHostDAO;

    /**
     * 初始化监控主机配置
     */
    @PostConstruct
    public void initMonitorHostConfig() {
        List<MonitorHostDO> hosts = monitorHostDAO.selectList(null);
        for (MonitorHostDO host : hosts) {
            String config = host.getMonitorConfig();
            if (Strings.isBlank(config)) {
                continue;
            }
            // 设置配置缓存
            this.setMonitorHostConfig(host.getAgentKey(), JSON.parseObject(config, MonitorHostConfigDTO.class));
        }
    }

    /**
     * 设置指标信息
     *
     * @param key     key
     * @param metrics metrics
     */
    public void setAgentMetrics(String key, MetricsDataDTO metrics) {
        if (metrics == null) {
            LATEST_METRICS_CACHE.remove(key);
        } else {
            LATEST_METRICS_CACHE.put(key, metrics);
        }
    }

    /**
     * 获取指标信息
     *
     * @param key key
     * @return metrics
     */
    public MetricsDataDTO getAgentMetrics(String key) {
        // 删除过期缓存
        long current = System.currentTimeMillis();
        if (current - lastCleanTime > CLEAN_INTERVAL) {
            this.lastCleanTime = current;
            LATEST_METRICS_CACHE.forEach((k, v) -> {
                if (current - v.getTimestamp() > MAX_CACHE_TIME) {
                    LATEST_METRICS_CACHE.remove(k, v);
                }
            });
        }
        return LATEST_METRICS_CACHE.get(key);
    }

    /**
     * 设置监控主机配置
     *
     * @param agentKey agentKey
     * @param config   config
     */
    public void setMonitorHostConfig(String agentKey, MonitorHostConfigDTO config) {
        HOST_CONFIG_CACHE.put(agentKey, config);
    }

    /**
     * 获取监控主机配置
     *
     * @param agentKey agentKey
     * @return config
     */
    public MonitorHostConfigDTO getMonitorHostConfig(String agentKey) {
        return HOST_CONFIG_CACHE.get(agentKey);
    }

}
