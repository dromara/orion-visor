package com.orion.ops.module.infra.service.impl;

import com.orion.lang.define.cache.CacheKeyDefine;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
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
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
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
    public void addGroupRel(DataGroupRelCreateRequest request) {
        DataGroupRelCreateRequest record = DataGroupRelCreateRequest.builder()
                .groupId(Valid.notNull(request.getGroupId()))
                .relId(Valid.notNull(request.getRelId()))
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
        Map<Long, String> groupTypeMapping = groups.stream()
                .collect(Collectors.toMap(DataGroupDO::getId, DataGroupDO::getType));
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
                    .type(groupTypeMapping.get(k))
                    .relId(s.getRelId())
                    .build()));
        });
        // 插入
        dataGroupRelDAO.insertBatch(records);
        // 删除缓存
        List<String> groupKeyList = groups.stream()
                .map(DataGroupDO::getId)
                .map(DataGroupCacheKeyDefine.DATA_GROUP_REL_GROUP::format)
                .collect(Collectors.toList());
        groups.stream()
                .map(DataGroupDO::getType)
                .distinct()
                .map(DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE::format)
                .forEach(groupKeyList::add);
        RedisStrings.delete(groupKeyList);
    }

    @Override
    public List<DataGroupRelCacheDTO> getGroupRelListByCache(String type) {
        return this.getGroupRelListByCache(
                DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE.format(type),
                DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE,
                () -> dataGroupRelDAO.of()
                        .createWrapper()
                        .eq(DataGroupRelDO::getType, type)
                        .then()
                        .list(DataGroupRelConvert.MAPPER::toCache)
        );
    }

    @Override
    public List<DataGroupRelCacheDTO> getGroupRelListByCache(String type, Long groupId) {
        return this.getGroupRelListByCache(
                DataGroupCacheKeyDefine.DATA_GROUP_REL_GROUP.format(type),
                DataGroupCacheKeyDefine.DATA_GROUP_REL_GROUP,
                () -> dataGroupRelDAO.of()
                        .createWrapper()
                        .eq(DataGroupRelDO::getType, type)
                        .eq(DataGroupRelDO::getGroupId, groupId)
                        .then()
                        .list(DataGroupRelConvert.MAPPER::toCache)
        );
    }

    /**
     * 查询分组引用缓存
     *
     * @param key           key
     * @param define        define
     * @param valueSupplier valueSupplier
     * @return values
     */
    public List<DataGroupRelCacheDTO> getGroupRelListByCache(String key,
                                                             CacheKeyDefine define,
                                                             Supplier<List<DataGroupRelCacheDTO>> valueSupplier) {
        // 查询缓存
        List<DataGroupRelCacheDTO> list = RedisStrings.getJsonArray(key, define);
        if (list.isEmpty()) {
            // 查询数据库
            list = valueSupplier.get();
            // 添加默认值 防止穿透
            if (list.isEmpty()) {
                list.add(DataGroupRelCacheDTO.builder()
                        .id(Const.NONE_ID)
                        .build());
            }
            // 设置缓存
            RedisStrings.setJson(key, define, list);
        }
        // 删除默认值
        list.removeIf(s -> s.getId().equals(Const.NONE_ID));
        return list;
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
        // 查询 group
        List<Long> groupIdList = dataGroupRelDAO.of()
                .createWrapper()
                .eq(DataGroupRelDO::getType, type)
                .in(DataGroupRelDO::getRelId, relIdList)
                .then()
                .stream()
                .map(DataGroupRelDO::getGroupId)
                .distinct()
                .collect(Collectors.toList());
        // 删除数据库
        int effect = dataGroupRelDAO.deleteBatchIds(relIdList);
        // 获取缓存 key
        List<String> keyList = Lists.of(DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE.format(type));
        groupIdList.stream()
                .map(DataGroupCacheKeyDefine.DATA_GROUP_REL_GROUP::format)
                .forEach(keyList::add);
        // 删除缓存
        RedisStrings.delete(keyList);
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteByGroupIdList(String type, List<Long> groupIdList) {
        // 删除数据库
        int effect = dataGroupRelDAO.deleteByGroupId(groupIdList);
        // 删除缓存
        List<String> keyList = Lists.of(DataGroupCacheKeyDefine.DATA_GROUP_REL_TYPE.format(type));
        groupIdList.stream()
                .map(DataGroupCacheKeyDefine.DATA_GROUP_REL_GROUP::format)
                .forEach(keyList::add);
        // 删除缓存
        RedisStrings.delete(keyList);
        return effect;
    }

}
