package com.orion.ops.module.infra.api.impl;

import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.DataGroupRelApi;
import com.orion.ops.module.infra.convert.DataGroupRelProviderConvert;
import com.orion.ops.module.infra.entity.dto.DataGroupRelCacheDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupRelCreateDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupRelUpdateDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupRelCreateRequest;
import com.orion.ops.module.infra.entity.request.data.DataGroupRelUpdateRequest;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;
import com.orion.ops.module.infra.service.DataGroupRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public void updateGroupRel(DataGroupRelUpdateDTO dto) {
        Valid.valid(dto);
        DataGroupRelUpdateRequest request = DataGroupRelProviderConvert.MAPPER.toRequest(dto);
        dataGroupRelService.updateGroupRel(request);
    }

    @Override
    public void addGroupRel(DataGroupRelCreateDTO dto) {
        Valid.valid(dto);
        DataGroupRelCreateRequest request = DataGroupRelProviderConvert.MAPPER.toRequest(dto);
        dataGroupRelService.addGroupRel(request);
    }

    @Override
    public void addGroupRel(List<DataGroupRelCreateDTO> list) {
        Valid.valid(list);
        List<DataGroupRelCreateRequest> rows = DataGroupRelProviderConvert.MAPPER.toList(list);
        dataGroupRelService.addGroupRel(rows);
    }

    @Override
    public Map<Long, Set<Long>> getGroupRelList(DataGroupTypeEnum type) {
        List<DataGroupRelCacheDTO> rows = dataGroupRelService.getGroupRelListByCache(type.name());
        return rows.stream().collect(
                Collectors.groupingBy(
                        DataGroupRelCacheDTO::getGroupId,
                        Collectors.mapping(DataGroupRelCacheDTO::getRelId, Collectors.toSet())
                ));
    }

    @Override
    public Set<Long> getGroupRelList(DataGroupTypeEnum type, Long groupId) {
        List<DataGroupRelCacheDTO> rows = dataGroupRelService.getGroupRelListByCache(type.name(), groupId);
        return rows.stream()
                .map(DataGroupRelCacheDTO::getRelId)
                .collect(Collectors.toSet());
    }

    @Override
    public Integer deleteByRelId(DataGroupTypeEnum type, Long relId) {
        return dataGroupRelService.deleteByRelId(type.name(), relId);
    }

    @Override
    public Integer deleteByRelIdList(DataGroupTypeEnum type, List<Long> relIdList) {
        return dataGroupRelService.deleteByRelIdList(type.name(), relIdList);
    }

    @Override
    public Integer deleteByGroupIdList(DataGroupTypeEnum type, List<Long> groupIdList) {
        return dataGroupRelService.deleteByGroupIdList(type.name(), groupIdList);
    }

}
