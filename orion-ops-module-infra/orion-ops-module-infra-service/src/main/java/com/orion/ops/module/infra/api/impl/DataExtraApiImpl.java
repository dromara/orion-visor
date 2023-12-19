package com.orion.ops.module.infra.api.impl;

import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.DataExtraApi;
import com.orion.ops.module.infra.convert.DataExtraProviderConvert;
import com.orion.ops.module.infra.entity.dto.data.DataExtraDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraQueryDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraUpdateDTO;
import com.orion.ops.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.ops.module.infra.entity.request.data.DataExtraUpdateRequest;
import com.orion.ops.module.infra.enums.DataExtraTypeEnum;
import com.orion.ops.module.infra.service.DataExtraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据拓展信息 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
@Slf4j
@Service
public class DataExtraApiImpl implements DataExtraApi {

    @Resource
    private DataExtraService dataExtraService;

    @Override
    public Integer updateExtraItem(DataExtraUpdateDTO dto, DataExtraTypeEnum type) {
        Valid.valid(dto);
        // 更新
        DataExtraUpdateRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.updateExtraItem(request);
    }

    @Override
    public void batchUpdate(Map<Long, Object> map) {
        dataExtraService.batchUpdate(map);
    }

    @Override
    public String getExtraItem(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        Valid.valid(dto);
        Valid.allNotNull(dto.getRelId(), dto.getItem());
        // 查询
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraItem(request);
    }

    @Override
    public Map<Long, String> getExtraItemList(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        Valid.valid(dto);
        Valid.allNotNull(dto.getRelIdList(), dto.getItem());
        // 查询
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraItemList(request);
    }

    @Override
    public List<DataExtraDTO> getExtraList(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraList(request)
                .stream()
                .map(DataExtraProviderConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Integer deleteByRelId(DataExtraTypeEnum type, Long relId) {
        return dataExtraService.deleteByRelId(type.name(), relId);
    }

}
