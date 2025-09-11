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
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.module.asset.dao.HostAgentLogDAO;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.entity.domain.HostAgentLogDO;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.vo.HostOnlineAgentConfigVO;
import org.dromara.visor.module.asset.enums.AgentInstallStatusEnum;
import org.dromara.visor.module.asset.enums.AgentLogStatusEnum;
import org.dromara.visor.module.asset.enums.AgentLogTypeEnum;
import org.dromara.visor.module.asset.enums.AgentOnlineStatusEnum;
import org.dromara.visor.module.asset.handler.host.extra.HostExtraItemEnum;
import org.dromara.visor.module.asset.handler.host.extra.model.HostSpecExtraModel;
import org.dromara.visor.module.asset.service.HostAgentEndpointService;
import org.dromara.visor.module.asset.service.HostExtraService;
import org.dromara.visor.module.monitor.api.MonitorHostApi;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 监控探针端点 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/27 16:07
 */
@Slf4j
@Service
public class HostAgentEndpointServiceImpl implements HostAgentEndpointService {

    /**
     * 标记离线阈值
     * 上报间隔(60s) * 2 + 5s
     */
    private static final int MARK_OFFLINE_THRESHOLD = (60 * 2 + 5) * 1000;

    private static final ConcurrentHashMap<String, Integer> ONLINE_STATUS_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, Long> HEARTBEAT_RECV_CACHE = new ConcurrentHashMap<>();

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostAgentLogDAO hostAgentLogDAO;

    @Resource
    private HostExtraService hostExtraService;

    @Resource
    private MonitorHostApi monitorHostApi;

    /**
     * 初始化主机在线状态
     */
    @PostConstruct
    public void initHostOnlineStatus() {
        List<HostDO> hosts = hostDAO.selectList(null);
        for (HostDO host : hosts) {
            Integer agentOnlineStatus = host.getAgentOnlineStatus();
            if (agentOnlineStatus != null) {
                ONLINE_STATUS_CACHE.put(host.getAgentKey(), agentOnlineStatus);
            }
        }
    }

    @Override
    public HostOnlineAgentConfigVO setAgentOnline(String agentKey, String version) {
        log.info("HostAgentEndpointService setAgentOnline agentKey: {}. version: {}", agentKey, version);
        try {
            // 查询主机信息
            Long hostId = hostDAO.selectIdByAgentKey(agentKey);
            Valid.notNull(hostId, ErrorMessage.HOST_ABSENT);
            // 查询主机规格信息
            HostSpecExtraModel spec = hostExtraService.getHostExtra(Const.SYSTEM_USER_ID, hostId, HostExtraItemEnum.SPEC);
            Boolean synced = Optional.ofNullable(spec)
                    .map(HostSpecExtraModel::getSynced)
                    .map(Booleans::isTrue)
                    .orElse(false);
            // 提前修正缓存
            ONLINE_STATUS_CACHE.put(agentKey, AgentOnlineStatusEnum.ONLINE.getValue());
            // 设置心跳
            this.setAgentHeartbeat(agentKey);
            // 修改状态
            HostDO update = HostDO.builder()
                    .agentVersion(version)
                    .agentOnlineStatus(AgentOnlineStatusEnum.ONLINE.getValue())
                    .agentOnlineChangeTime(new Date())
                    .agentInstallStatus(AgentInstallStatusEnum.INSTALLED.getStatus())
                    .build();
            hostDAO.updateByAgentKeys(Lists.singleton(agentKey), update);
            // 插入日志
            HostAgentLogDO agentLog = HostAgentLogDO.builder()
                    .hostId(hostId)
                    .agentKey(agentKey)
                    .type(AgentLogTypeEnum.ONLINE.name())
                    .status(AgentLogStatusEnum.SUCCESS.name())
                    .build();
            hostAgentLogDAO.insert(agentLog);
            // 返回
            return HostOnlineAgentConfigVO.builder()
                    .specSynced(synced)
                    .build();
        } catch (Exception e) {
            // 由心跳修正
            ONLINE_STATUS_CACHE.put(agentKey, AgentOnlineStatusEnum.OFFLINE.getValue());
            throw e;
        }
    }

    @Override
    public void setAgentOffline(String agentKey) {
        log.info("HostAgentEndpointService setAgentOffline agentKey: {}", agentKey);
        // 查询主机信息
        Long hostId = hostDAO.selectIdByAgentKey(agentKey);
        Valid.notNull(hostId, ErrorMessage.HOST_ABSENT);
        // 修改缓存
        ONLINE_STATUS_CACHE.put(agentKey, AgentOnlineStatusEnum.OFFLINE.getValue());
        HEARTBEAT_RECV_CACHE.put(agentKey, 0L);
        // 修改状态
        HostDO update = HostDO.builder()
                .agentOnlineStatus(AgentOnlineStatusEnum.OFFLINE.getValue())
                .agentOnlineChangeTime(new Date())
                .build();
        hostDAO.updateByAgentKeys(Lists.singleton(agentKey), update);
        // 插入日志
        HostAgentLogDO agentLog = HostAgentLogDO.builder()
                .hostId(hostId)
                .agentKey(agentKey)
                .type(AgentLogTypeEnum.OFFLINE.name())
                .status(AgentLogStatusEnum.SUCCESS.name())
                .build();
        hostAgentLogDAO.insert(agentLog);
        // 设置监控上下文为已下线
        monitorHostApi.setAgentOffline(Lists.singleton(agentKey));
    }

    @Override
    public void setAgentHeartbeat(String agentKey) {
        // 设置心跳时间
        HEARTBEAT_RECV_CACHE.put(agentKey, System.currentTimeMillis());
    }

    @Override
    public void checkHeartbeat() {
        long now = System.currentTimeMillis();
        List<String> markOnlineList = new ArrayList<>();
        List<String> markOfflineList = new ArrayList<>();
        // 状态检查
        ONLINE_STATUS_CACHE.forEach((key, status) -> {
            // 上次心跳时间
            Long lastHeartbeatTime = HEARTBEAT_RECV_CACHE.getOrDefault(key, 0L);
            // 超过阈值标记离线
            if (now - lastHeartbeatTime > MARK_OFFLINE_THRESHOLD) {
                // 如果当前状态是在线则标记离线
                if (AgentOnlineStatusEnum.ONLINE.getValue().equals(status)) {
                    markOfflineList.add(key);
                }
            } else {
                // 如果当前状态是离线则标记在线
                if (AgentOnlineStatusEnum.OFFLINE.getValue().equals(status)) {
                    markOnlineList.add(key);
                }
            }
        });
        // 更新在线状态
        if (!markOnlineList.isEmpty()) {
            this.markOnlineStatus(markOnlineList, AgentOnlineStatusEnum.ONLINE);
        }
        // 更新离线状态
        if (!markOfflineList.isEmpty()) {
            this.markOnlineStatus(markOfflineList, AgentOnlineStatusEnum.OFFLINE);
        }
    }

    /**
     * 标记在线状态
     *
     * @param agentKeyList agentKeyList
     * @param status       status
     */
    private void markOnlineStatus(List<String> agentKeyList, AgentOnlineStatusEnum status) {
        if (Lists.isEmpty(agentKeyList)) {
            return;
        }
        log.info("HostAgentEndpointService mark {}. count: {}, keys: {}", status, agentKeyList.size(), agentKeyList);
        // 更新数据
        HostDO update = HostDO.builder()
                .agentOnlineStatus(status.getValue())
                .agentOnlineChangeTime(new Date())
                .build();
        int effect = hostDAO.updateByAgentKeys(agentKeyList, update);
        // 更新缓存
        agentKeyList.forEach(s -> ONLINE_STATUS_CACHE.put(s, status.getValue()));
        log.info("HostAgentEndpointService mark {}. effect: {}", status, effect);
        // 插入日志
        List<HostAgentLogDO> logList = hostDAO.selectIdByAgentKeys(agentKeyList)
                .stream()
                .map(s -> {
                    HostAgentLogDO agentLog = HostAgentLogDO.builder()
                            .hostId(s.getId())
                            .agentKey(s.getAgentKey())
                            .status(AgentLogStatusEnum.SUCCESS.name())
                            .build();
                    if (AgentOnlineStatusEnum.ONLINE.equals(status)) {
                        agentLog.setType(AgentLogTypeEnum.ONLINE.name());
                    } else {
                        agentLog.setType(AgentLogTypeEnum.OFFLINE.name());
                    }
                    return agentLog;
                }).collect(Collectors.toList());
        if (!logList.isEmpty()) {
            hostAgentLogDAO.insertBatch(logList);
        }
        // 设置监控上下文为已下线
        if (AgentOnlineStatusEnum.OFFLINE.equals(status)) {
            monitorHostApi.setAgentOffline(agentKeyList);
        }
    }

}
