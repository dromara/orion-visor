package com.orion.ops.module.infra.service.impl;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisLists;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.framework.redis.core.utils.RedisUtils;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.module.infra.convert.DataGroupRelConvert;
import com.orion.ops.module.infra.dao.DataGroupDAO;
import com.orion.ops.module.infra.dao.DataGroupRelDAO;
import com.orion.ops.module.infra.define.cache.DataGroupCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.DataGroupDO;
import com.orion.ops.module.infra.entity.domain.DataGroupRelDO;
import com.orion.ops.module.infra.entity.dto.DataGroupRelCacheDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupRelCreateRequest;
import com.orion.ops.module.infra.service.DataGroupRelService;
import com.orion.spring.SpringHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
            List<Long> persetRelIdList = records.stream()
                    .map(DataGroupRelDO::getRelId)
                    .collect(Collectors.toList());
            relIdList.removeIf(persetRelIdList::contains);
            if (!relIdList.isEmpty()) {
                List<DataGroupRelDO> insertRecords = relIdList.stream()
                        .map(s -> DataGroupRelDO.builder()
                                .groupId(groupId)
                                .type(type)
                                .relId(s)
                                .build())
                        .collect(Collectors.toList());
                dataGroupRelDAO.insertBatch(insertRecords);
            }
        }
        // 删除缓存
        this.deleteCache(type, Lists.of(groupId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGroupRel(String type, List<Long> groupIdList, Long relId) {
        Valid.notNull(relId);
        // 删除引用
        this.deleteByRelId(type, relId);
        // 插入引用
        if (!Lists.isEmpty(groupIdList)) {
            List<DataGroupRelDO> relList = groupIdList.stream()
                    .map(s -> DataGroupRelDO.builder()
                            .type(type)
                            .groupId(s)
                            .relId(relId)
                            .build())
                    .collect(Collectors.toList());
            dataGroupRelDAO.insertBatch(relList);
            // 删除缓存
            this.deleteCache(type, groupIdList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroupRel(Long groupId, Long relId) {
        DataGroupRelCreateRequest record = DataGroupRelCreateRequest.builder()
                .groupId(Valid.notNull(groupId))
                .relId(Valid.notNull(relId))
                .build();
        // 插入
        SpringHolder.getBean(DataGroupRelService.class)
                .addGroupRel(Lists.singleton(record));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroupRel(List<DataGroupRelCreateRequest> list) {
        if (Lists.isEmpty(list)) {
            return;
        }
        // 通过 groupId 分组
        Map<Long, List<DataGroupRelCreateRequest>> groupMapping = list.stream()
                .collect(Collectors.groupingBy(DataGroupRelCreateRequest::getGroupId));
        // 查询分组信息
        List<DataGroupDO> groups = dataGroupDAO.selectBatchIds(groupMapping.keySet());
        Valid.eq(groups.size(), groupMapping.size(), ErrorMessage.GROUP_ABSENT);
        String type = groups.get(0).getType();
        // 查询关联是否存在
        groupMapping.forEach((k, v) -> {
            List<Long> relIdList = v.stream()
                    .map(DataGroupRelCreateRequest::getRelId)
                    .collect(Collectors.toList());
            // 查询关联
            List<Long> presentRelIdList = dataGroupRelDAO.of()
                    .createWrapper()
                    .eq(DataGroupRelDO::getGroupId, k)
                    .in(DataGroupRelDO::getRelId, relIdList)
                    .then()
                    .stream()
                    .map(DataGroupRelDO::getRelId)
                    .distinct()
                    .collect(Collectors.toList());
            if (!presentRelIdList.isEmpty()) {
                // 删除待插入的重复数据
                v.removeIf(s -> presentRelIdList.contains(s.getRelId()));
            }
        });
        // 构建插入数据
        List<DataGroupRelDO> records = new ArrayList<>();
        groupMapping.forEach((k, v) -> {
            v.forEach(s -> records.add(DataGroupRelDO.builder()
                    .groupId(k)
                    .type(type)
                    .relId(s.getRelId())
                    .build()));
        });
        // 插入
        dataGroupRelDAO.insertBatch(records);
        // 删除缓存
        this.deleteCache(type, groupMapping.keySet());
    }

    @Override
    public List<DataGroupRelCacheDTO> getGroupRelListByCache(String type) {
        String key = DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE.format(type);
        // 查询缓存
        List<DataGroupRelCacheDTO> list = RedisStrings.getJsonArray(key, DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE);
        if (Lists.isEmpty(list)) {
            // 查询数据库
            list = dataGroupRelDAO.of()
                    .createWrapper()
                    .eq(DataGroupRelDO::getType, type)
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
    public List<DataGroupRelDO> getGroupRelByRelId(String type, Long relId) {
        return dataGroupRelDAO.of()
                .createWrapper()
                .eq(DataGroupRelDO::getType, type)
                .eq(DataGroupRelDO::getRelId, relId)
                .then()
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByRelId(String type, Long relId) {
        return SpringHolder.getBean(DataGroupRelService.class)
                .deleteByRelIdList(type, Lists.singleton(relId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByRelIdList(String type, List<Long> relIdList) {
        if (Strings.isBlank(type) || Lists.isEmpty(relIdList)) {
            return 0;
        }
        // 查询需要删除的数据
        List<DataGroupRelDO> rows = dataGroupRelDAO.of()
                .createWrapper()
                .eq(DataGroupRelDO::getType, type)
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
        this.deleteCache(type, groupIdList);
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByGroupIdList(String type, List<Long> groupIdList) {
        // 删除数据库
        int effect = dataGroupRelDAO.deleteByGroupId(groupIdList);
        // 删除缓存
        this.deleteCache(type, groupIdList);
        return effect;
    }

    /**
     * 删除缓存
     *
     * @param type        type
     * @param groupIdList groupIdList
     */
    private void deleteCache(String type, Collection<Long> groupIdList) {
        // 类型缓存
        List<String> keyList = Lists.of(DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE.format(type));
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
