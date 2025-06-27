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
package org.dromara.visor.module.infra.api.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.module.infra.api.DataGroupUserRelApi;
import org.dromara.visor.module.infra.entity.domain.DataGroupRelDO;
import org.dromara.visor.module.infra.entity.dto.DataGroupRelCacheDTO;
import org.dromara.visor.module.infra.enums.DataGroupTypeEnum;
import org.dromara.visor.module.infra.service.DataGroupRelService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 数据分组关联 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Slf4j
@Service
public class DataGroupUserRelApiImpl implements DataGroupUserRelApi {

    @Resource
    private DataGroupRelService dataGroupRelService;

    @Override
    public void updateGroupRel(DataGroupTypeEnum type, Long userId, List<Long> groupIdList, Long relId) {
        Valid.notNull(relId);
        dataGroupRelService.updateRelGroup(type.name(), userId, groupIdList, relId);
    }

    @Override
    public void addGroupRel(DataGroupTypeEnum type, Long userId, Long groupId, Long relId) {
        Valid.notNull(groupId);
        Valid.notNull(relId);
        dataGroupRelService.addGroupRel(type.name(), userId, groupId, relId);
    }

    @Override
    public void addGroupRel(DataGroupTypeEnum type, Long userId, Map<Long, List<Long>> groupRelListMap) {
        Valid.notEmpty(groupRelListMap);
        dataGroupRelService.addGroupRel(type.name(), userId, groupRelListMap);
    }

    @Override
    public Map<Long, Set<Long>> getGroupRelList(DataGroupTypeEnum type, Long userId) {
        List<DataGroupRelCacheDTO> rows = dataGroupRelService.getGroupRelListByCache(type.name(), userId);
        return rows.stream().collect(
                Collectors.groupingBy(
                        DataGroupRelCacheDTO::getGroupId,
                        Collectors.mapping(DataGroupRelCacheDTO::getRelId, Collectors.toSet())
                ));
    }

    @Override
    public Set<Long> getGroupRelIdByGroupId(DataGroupTypeEnum type, Long userId, Long groupId) {
        List<Long> rows = dataGroupRelService.getGroupRelIdListByCache(type.name(), groupId);
        return new HashSet<>(rows);
    }

    @Override
    @Async("asyncExecutor")
    public Future<Set<Long>> getGroupIdByRelIdAsync(DataGroupTypeEnum type, Long userId, Long relId) {
        Set<Long> groupIdList = dataGroupRelService.getGroupRelByRelId(type.name(), userId, relId)
                .stream()
                .map(DataGroupRelDO::getGroupId)
                .collect(Collectors.toSet());
        return CompletableFuture.completedFuture(groupIdList);
    }

    @Override
    public Map<Long, Set<Long>> getGroupIdByRelIdList(DataGroupTypeEnum type, Long userId, List<Long> relIdList) {
        return dataGroupRelService.getGroupRelByRelIdList(type.name(), userId, relIdList)
                .stream()
                .collect(Collectors.groupingBy(DataGroupRelDO::getRelId,
                        Collectors.mapping(DataGroupRelDO::getGroupId, Collectors.toSet())));
    }

    @Override
    public Integer deleteByRelId(DataGroupTypeEnum type, Long userId, Long relId) {
        return dataGroupRelService.deleteByRelId(type.name(), userId, relId);
    }

    @Override
    public Integer deleteByRelIdList(DataGroupTypeEnum type, Long userId, List<Long> relIdList) {
        return dataGroupRelService.deleteByRelIdList(type.name(), userId, relIdList);
    }

    @Override
    public Integer deleteByGroupIdList(DataGroupTypeEnum type, Long userId, List<Long> groupIdList) {
        return dataGroupRelService.deleteByGroupIdList(type.name(), userId, groupIdList);
    }

}
