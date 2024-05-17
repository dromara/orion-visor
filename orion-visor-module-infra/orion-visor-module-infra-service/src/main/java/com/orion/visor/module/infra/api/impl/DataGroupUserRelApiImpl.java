package com.orion.visor.module.infra.api.impl;

import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.infra.api.DataGroupUserRelApi;
import com.orion.visor.module.infra.convert.DataGroupRelProviderConvert;
import com.orion.visor.module.infra.entity.domain.DataGroupRelDO;
import com.orion.visor.module.infra.entity.dto.DataGroupRelCacheDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupRelCreateDTO;
import com.orion.visor.module.infra.entity.request.data.DataGroupRelCreateRequest;
import com.orion.visor.module.infra.enums.DataGroupTypeEnum;
import com.orion.visor.module.infra.service.DataGroupRelService;
import lombok.extern.slf4j.Slf4j;
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
    public void addGroupRel(DataGroupTypeEnum type, Long userId, List<DataGroupRelCreateDTO> list) {
        Valid.valid(list);
        List<DataGroupRelCreateRequest> rows = DataGroupRelProviderConvert.MAPPER.toList(list);
        dataGroupRelService.addGroupRel(type.name(), userId, rows);
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
