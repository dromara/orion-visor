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
package org.dromara.visor.module.asset.api.impl;

import cn.orionsec.kit.lang.define.cache.TimedCache;
import cn.orionsec.kit.lang.define.cache.TimedCacheBuilder;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.io.Streams;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.module.asset.api.HostAgentApi;
import org.dromara.visor.module.asset.convert.HostAgentLogProviderConvert;
import org.dromara.visor.module.asset.convert.HostProviderConvert;
import org.dromara.visor.module.asset.dao.HostAgentLogDAO;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.define.cache.HostCacheKeyDefine;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.dto.host.HostAgentLogDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostBaseDTO;
import org.dromara.visor.module.asset.service.HostAgentService;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 主机探针对外服务 实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/9/1 21:18
 */
@Service
public class HostAgentApiImpl implements HostAgentApi {

    private static final TimedCache<HostBaseDTO> AGENT_HOST_CACHE = TimedCacheBuilder.<HostBaseDTO>create()
            .expireAfter(30 * Const.MS_S_60)
            .checkInterval(Const.MS_S_60)
            .build();

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostAgentLogDAO hostAgentLogDAO;

    @Resource
    private HostAgentService hostAgentService;

    @PreDestroy
    public void destroyTimedCache() {
        Streams.close(AGENT_HOST_CACHE);
    }

    @Override
    public List<HostAgentLogDTO> selectAgentInstallLog(List<Long> hostIdList) {
        if (Lists.isEmpty(hostIdList)) {
            return Lists.empty();
        }
        // 查询缓存
        List<String> keys = hostIdList.stream()
                .map(HostCacheKeyDefine.HOST_INSTALL_LOG::format)
                .collect(Collectors.toList());
        List<Long> logIdList = RedisStrings.getList(keys)
                .stream()
                .filter(Strings::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        if (logIdList.isEmpty()) {
            return Lists.empty();
        }
        // 查询数据库
        return hostAgentLogDAO.selectBatchIds(logIdList)
                .stream()
                .map(HostAgentLogProviderConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getNameCacheByAgentKey(List<String> agentKeys) {
        Map<String, String> result = new HashMap<>();
        List<String> queryList = new ArrayList<>();
        // 查询缓存
        for (String agentKey : agentKeys) {
            HostBaseDTO host = AGENT_HOST_CACHE.get(agentKey);
            if (host != null) {
                result.put(agentKey, host.getName());
            } else {
                queryList.add(agentKey);
            }
        }
        if (!queryList.isEmpty()) {
            // 加载缓存
            this.loadHostCache(queryList);
            // 重新设置返回结果
            queryList.forEach(agentKey -> {
                HostBaseDTO host = AGENT_HOST_CACHE.get(agentKey);
                if (host != null) {
                    result.put(agentKey, host.getName());
                }
            });
        }
        return result;
    }

    @Override
    public HostBaseDTO getHostCacheByAgentKey(String agentKey) {
        HostBaseDTO host = AGENT_HOST_CACHE.get(agentKey);
        // 加载缓存
        if (host == null) {
            this.loadHostCache(Lists.singleton(agentKey));
            host = AGENT_HOST_CACHE.get(agentKey);
        }
        return host;
    }

    @Override
    public String getAgentVersion() {
        return hostAgentService.getAgentVersion();
    }

    /**
     * 加载主机缓存
     *
     * @param agentKeys agentKeys
     */
    private void loadHostCache(List<String> agentKeys) {
        // 查询数据并设置缓存
        hostDAO.of()
                .createWrapper()
                .select(HostDO::getId,
                        HostDO::getName,
                        HostDO::getCode,
                        HostDO::getAddress,
                        HostDO::getAgentKey)
                .in(HostDO::getAgentKey, agentKeys)
                .then()
                .list(HostProviderConvert.MAPPER::toBase)
                .forEach(s -> AGENT_HOST_CACHE.put(s.getAgentKey(), s));

    }

}
