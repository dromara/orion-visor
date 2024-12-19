/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.infra.api.DataGroupRelApi;
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
public class DataGroupRelApiImpl implements DataGroupRelApi {

    @Resource
    private DataGroupRelService dataGroupRelService;

    @Override
    public void updateGroupRel(Long groupId, List<Long> relIdList) {
        Valid.notNull(groupId);
        dataGroupRelService.updateGroupRel(groupId, relIdList);
    }

    @Override
    public void updateRelGroup(DataGroupTypeEnum type, List<Long> groupIdList, Long relId) {
        Valid.notNull(relId);
        dataGroupRelService.updateRelGroup(type.name(), Const.SYSTEM_USER_ID, groupIdList, relId);
    }

    @Override
    public void addGroupRel(DataGroupTypeEnum type, Long groupId, Long relId) {
        Valid.notNull(groupId);
        Valid.notNull(relId);
        dataGroupRelService.addGroupRel(type.name(), groupId, Const.SYSTEM_USER_ID, relId);
    }

    @Override
    public void addGroupRel(DataGroupTypeEnum type, Map<Long, List<Long>> groupRelListMap) {
        Valid.notEmpty(groupRelListMap);
        dataGroupRelService.addGroupRel(type.name(), Const.SYSTEM_USER_ID, groupRelListMap);
    }

    @Override
    public Map<Long, Set<Long>> getGroupRelList(DataGroupTypeEnum type) {
        List<DataGroupRelCacheDTO> rows = dataGroupRelService.getGroupRelListByCache(type.name(), Const.SYSTEM_USER_ID);
        return rows.stream().collect(
                Collectors.groupingBy(
                        DataGroupRelCacheDTO::getGroupId,
                        Collectors.mapping(DataGroupRelCacheDTO::getRelId, Collectors.toSet())
                ));
    }

    @Override
    public Set<Long> getGroupRelIdByGroupId(DataGroupTypeEnum type, Long groupId) {
        List<Long> rows = dataGroupRelService.getGroupRelIdListByCache(type.name(), groupId);
        return new HashSet<>(rows);
    }

    @Override
    @Async("asyncExecutor")
    public Future<Set<Long>> getGroupIdByRelIdAsync(DataGroupTypeEnum type, Long relId) {
        Set<Long> groupIdList = dataGroupRelService.getGroupRelByRelId(type.name(), Const.SYSTEM_USER_ID, relId)
                .stream()
                .map(DataGroupRelDO::getGroupId)
                .collect(Collectors.toSet());
        return CompletableFuture.completedFuture(groupIdList);
    }

    @Override
    public Integer deleteByRelId(DataGroupTypeEnum type, Long relId) {
        return dataGroupRelService.deleteByRelId(type.name(), Const.SYSTEM_USER_ID, relId);
    }

    @Override
    public Integer deleteByRelIdList(DataGroupTypeEnum type, List<Long> relIdList) {
        return dataGroupRelService.deleteByRelIdList(type.name(), Const.SYSTEM_USER_ID, relIdList);
    }

    @Override
    public Integer deleteByGroupIdList(DataGroupTypeEnum type, List<Long> groupIdList) {
        return dataGroupRelService.deleteByGroupIdList(type.name(), Const.SYSTEM_USER_ID, groupIdList);
    }

}
