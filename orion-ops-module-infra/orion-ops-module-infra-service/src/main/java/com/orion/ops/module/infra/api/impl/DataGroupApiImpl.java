package com.orion.ops.module.infra.api.impl;

import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.DataGroupApi;
import com.orion.ops.module.infra.convert.DataGroupProviderConvert;
import com.orion.ops.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupUpdateDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupCreateRequest;
import com.orion.ops.module.infra.entity.request.data.DataGroupUpdateRequest;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;
import com.orion.ops.module.infra.service.DataGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据分组 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Slf4j
@Service
public class DataGroupApiImpl implements DataGroupApi {

    @Resource
    private DataGroupService dataGroupService;

    @Override
    public Long createDataGroup(DataGroupTypeEnum type, DataGroupCreateDTO dto) {
        Valid.valid(dto);
        DataGroupCreateRequest request = DataGroupProviderConvert.MAPPER.toRequest(dto);
        request.setType(type.name());
        return dataGroupService.createDataGroup(request);
    }

    @Override
    public Integer renameDataGroup(DataGroupUpdateDTO dto) {
        Valid.valid(dto);
        DataGroupUpdateRequest request = DataGroupProviderConvert.MAPPER.toRequest(dto);
        return dataGroupService.renameDataGroup(request);
    }

    @Override
    public List<DataGroupDTO> getDataGroupList(DataGroupTypeEnum type) {
        List<DataGroupCacheDTO> rows = dataGroupService.getDataGroupListByCache(type.name());
        return DataGroupProviderConvert.MAPPER.toList(rows);
    }

    @Override
    public List<DataGroupDTO> getDataGroupTree(DataGroupTypeEnum type) {
        List<DataGroupCacheDTO> rows = dataGroupService.getDataGroupTreeByCache(type.name());
        return DataGroupProviderConvert.MAPPER.toList(rows);
    }

    @Override
    public Integer deleteDataGroupById(Long id) {
        return dataGroupService.deleteDataGroupById(id);
    }

}
