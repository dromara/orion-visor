/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.infra.service.impl;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.spring.SpringHolder;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.redis.core.utils.RedisLists;
import com.orion.visor.framework.redis.core.utils.RedisStrings;
import com.orion.visor.framework.redis.core.utils.RedisUtils;
import com.orion.visor.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.visor.module.infra.convert.DataGroupRelConvert;
import com.orion.visor.module.infra.dao.DataGroupDAO;
import com.orion.visor.module.infra.dao.DataGroupRelDAO;
import com.orion.visor.module.infra.define.cache.DataGroupCacheKeyDefine;
import com.orion.visor.module.infra.entity.domain.DataGroupDO;
import com.orion.visor.module.infra.entity.domain.DataGroupRelDO;
import com.orion.visor.module.infra.entity.dto.DataGroupRelCacheDTO;
import com.orion.visor.module.infra.service.DataGroupRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据分组关联 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Slf4j
@Service
public class DataGroupRelServiceImpl implements DataGroupRelService {

    @Resource
    private DataGroupDAO dataGroupDAO;

    @Resource
    private DataGroupRelDAO dataGroupRelDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGroupRel(Long groupId, List<Long> relIdList) {
        Valid.notNull(groupId);
        // 查询分组
        DataGroupDO group = dataGroupDAO.selectById(groupId);
        Valid.notNull(group, ErrorMessage.GROUP_ABSENT);
        String type = group.getType();
        Long userId = group.getUserId();
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.GROUP_NAME, group.getName());
        if (Lists.isEmpty(relIdList)) {
            // 为空删除
            dataGroupRelDAO.deleteByGroupId(groupId);
        } else {
            // 差异变更
            List<DataGroupRelDO> records = dataGroupRelDAO.of()
                    .createWrapper()
                    .eq(DataGroupRelDO::getGroupId, group.getId())
                    .then()
                    .list();
            // 查询删除的部分
            List<Long> deleteIdList = records.stream()
                    .filter(s -> !relIdList.contains(s.getRelId()))
                    .map(DataGroupRelDO::getId)
                    .collect(Collectors.toList());
            if (!deleteIdList.isEmpty()) {
                dataGroupRelDAO.deleteBatchIds(deleteIdList);
            }
            // 查询新增的部分
            List<Long> presentRelIdList = records.stream()
                    .map(DataGroupRelDO::getRelId)
                    .collect(Collectors.toList());
            relIdList.removeIf(presentRelIdList::contains);
            if (!relIdList.isEmpty()) {
                List<DataGroupRelDO> insertRecords = relIdList.stream()
                        .map(s -> DataGroupRelDO.builder()
                                .type(type)
                                .userId(userId)
                                .groupId(groupId)
                                .relId(s)
                                .build())
                        .collect(Collectors.toList());
                dataGroupRelDAO.insertBatch(insertRecords);
            }
        }
        // 删除缓存
        this.deleteCache(type, userId, Lists.singleton(groupId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRelGroup(String type, Long userId, List<Long> groupIdList, Long relId) {
        Valid.notNull(relId);
        // 删除引用
        this.deleteByRelId(type, userId, relId);
        // 插入引用
        if (!Lists.isEmpty(groupIdList)) {
            List<DataGroupRelDO> relList = groupIdList.stream()
                    .map(s -> DataGroupRelDO.builder()
                            .type(type)
                            .userId(userId)
                            .groupId(s)
                            .relId(relId)
                            .build())
                    .collect(Collectors.toList());
            dataGroupRelDAO.insertBatch(relList);
            // 删除缓存
            this.deleteCache(type, userId, groupIdList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroupRel(String type, Long userId, Long groupId, Long relId) {
        Map<Long, List<Long>> map = new HashMap<>();
        map.put(groupId, Lists.singleton(relId));
        // 插入
        SpringHolder.getBean(DataGroupRelService.class).addGroupRel(type, userId, map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroupRel(String type, Long userId, Map<Long, List<Long>> groupRelListMap) {
        if (Maps.isEmpty(groupRelListMap)) {
            return;
        }
        // 构建插入数据
        List<DataGroupRelDO> records = new ArrayList<>();
        groupRelListMap.forEach((k, v) -> {
            v.forEach(s -> records.add(DataGroupRelDO.builder()
                    .groupId(k)
                    .type(type)
                    .userId(userId)
                    .relId(s)
                    .build()));
        });
        // 插入
        dataGroupRelDAO.insertBatch(records);
        // 删除缓存
        this.deleteCache(type, userId, groupRelListMap.keySet());
    }

    @Override
    public List<DataGroupRelCacheDTO> getGroupRelListByCache(String type, Long userId) {
        String key = DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE.format(type, userId);
        // 查询缓存
        List<DataGroupRelCacheDTO> list = RedisStrings.getJsonArray(key, DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE);
        if (Lists.isEmpty(list)) {
            // 查询数据库
            list = dataGroupRelDAO.of()
                    .createWrapper()
                    .eq(DataGroupRelDO::getType, type)
                    .eq(DataGroupRelDO::getUserId, userId)
                    .then()
                    .list(DataGroupRelConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, DataGroupRelCacheDTO::new);
            // 设置缓存
            RedisStrings.setJson(key, DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE, list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        return list;
    }

    @Override
    public List<Long> getGroupRelIdListByCache(String type, Long groupId) {
        String key = DataGroupCacheKeyDefine.DATA_GROUP_REL_GROUP.format(groupId);
        // 查询缓存
        List<Long> list = RedisLists.range(key, Long::valueOf);
        if (Lists.isEmpty(list)) {
            // 查询数据库
            list = dataGroupRelDAO.of()
                    .createWrapper()
                    .eq(DataGroupRelDO::getType, type)
                    .eq(DataGroupRelDO::getGroupId, groupId)
                    .then()
                    .stream()
                    .map(DataGroupRelDO::getRelId)
                    .collect(Collectors.toList());
            // 设置屏障 防止穿透
            CacheBarriers.LIST.check(list);
            // 设置缓存
            RedisLists.pushAll(key, DataGroupCacheKeyDefine.DATA_GROUP_REL_GROUP, list, Object::toString);
        }
        // 删除屏障
        CacheBarriers.LIST.remove(list);
        return list;
    }

    @Override
    public List<DataGroupRelDO> getGroupRelByRelId(String type, Long userId, Long relId) {
        return dataGroupRelDAO.of()
                .createWrapper()
                .eq(DataGroupRelDO::getType, type)
                .eq(DataGroupRelDO::getUserId, userId)
                .eq(DataGroupRelDO::getRelId, relId)
                .then()
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByRelId(String type, Long userId, Long relId) {
        return SpringHolder.getBean(DataGroupRelService.class)
                .deleteByRelIdList(type, userId, Lists.singleton(relId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByRelIdList(String type, Long userId, List<Long> relIdList) {
        if (Strings.isBlank(type) || Lists.isEmpty(relIdList)) {
            return 0;
        }
        // 查询需要删除的数据
        List<DataGroupRelDO> rows = dataGroupRelDAO.of()
                .createWrapper()
                .eq(DataGroupRelDO::getType, type)
                .eq(DataGroupRelDO::getUserId, userId)
                .in(DataGroupRelDO::getRelId, relIdList)
                .then()
                .list();
        if (rows.isEmpty()) {
            return 0;
        }
        // 需要删除的 id
        List<Long> idList = rows.stream()
                .map(DataGroupRelDO::getId)
                .collect(Collectors.toList());
        // 需要删除的 groupId
        List<Long> groupIdList = rows.stream()
                .map(DataGroupRelDO::getGroupId)
                .distinct()
                .collect(Collectors.toList());
        // 删除数据库
        int effect = dataGroupRelDAO.deleteBatchIds(idList);
        // 删除缓存
        this.deleteCache(type, userId, groupIdList);
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByGroupIdList(String type, Long userId, List<Long> groupIdList) {
        // 删除数据库
        int effect = dataGroupRelDAO.deleteByGroupId(groupIdList);
        // 删除缓存
        this.deleteCache(type, userId, groupIdList);
        return effect;
    }

    /**
     * 删除缓存
     *
     * @param type        type
     * @param userId      userId
     * @param groupIdList groupIdList
     */
    private void deleteCache(String type, Long userId, Collection<Long> groupIdList) {
        // 类型缓存
        List<String> keyList = Lists.of(DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE.format(type, userId));
        // 分组缓存
        if (!Lists.isEmpty(groupIdList)) {
            groupIdList.stream()
                    .map(DataGroupCacheKeyDefine.DATA_GROUP_REL_GROUP::format)
                    .forEach(keyList::add);
        }
        // 删除
        RedisUtils.delete(keyList);
    }

}
