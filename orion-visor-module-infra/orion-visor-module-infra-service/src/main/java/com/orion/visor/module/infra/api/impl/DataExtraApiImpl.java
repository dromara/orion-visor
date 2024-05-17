package com.orion.visor.module.infra.api.impl;

import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.infra.api.DataExtraApi;
import com.orion.visor.module.infra.convert.DataExtraProviderConvert;
import com.orion.visor.module.infra.dao.DataExtraDAO;
import com.orion.visor.module.infra.entity.domain.DataExtraDO;
import com.orion.visor.module.infra.entity.dto.data.DataExtraDTO;
import com.orion.visor.module.infra.entity.dto.data.DataExtraQueryDTO;
import com.orion.visor.module.infra.entity.dto.data.DataExtraSetDTO;
import com.orion.visor.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.visor.module.infra.entity.request.data.DataExtraSetRequest;
import com.orion.visor.module.infra.enums.DataExtraTypeEnum;
import com.orion.visor.module.infra.service.DataExtraService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
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

    @Resource
    private DataExtraDAO dataExtraDAO;

    @Override
    public Integer setExtraItem(DataExtraSetDTO dto, DataExtraTypeEnum type) {
        Valid.valid(dto);
        // 更新
        DataExtraSetRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.setExtraItem(request);
    }

    @Override
    public Long addExtraItem(DataExtraSetDTO dto, DataExtraTypeEnum type) {
        Valid.valid(dto);
        // 更新
        DataExtraSetRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.addExtraItem(request);
    }

    @Override
    public Integer updateExtraValue(Long id, String value) {
        return dataExtraService.updateExtraValue(id, value);
    }

    @Override
    public void batchUpdateExtraValue(Map<Long, String> map) {
        dataExtraService.batchUpdateExtraValue(map);
    }

    @Override
    public String getExtraValue(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        Valid.allNotNull(dto.getUserId(), dto.getRelId(), dto.getItem());
        // 查询
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraItemValue(request);
    }

    @Override
    public Map<Long, String> getExtraItemValues(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        Valid.allNotNull(dto.getUserId(), dto.getRelIdList(), dto.getItem());
        // 查询
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraItemValues(request);
    }

    @Override
    public String getExtraItemValueByCache(Long userId, DataExtraTypeEnum type, String item, Long relId) {
        Valid.allNotNull(userId, type, item, relId);
        return dataExtraService.getExtraItemValueByCache(userId, type.name(), item, relId);
    }

    @Override
    public Map<Long, String> getExtraItemValuesByCache(Long userId, DataExtraTypeEnum type, String item) {
        Valid.allNotNull(userId, type, item);
        return dataExtraService.getExtraItemValuesByCache(userId, type.name(), item);
    }

    @Override
    public Future<Map<Long, String>> getExtraItemValuesByCacheAsync(Long userId, DataExtraTypeEnum type, String item) {
        return CompletableFuture.completedFuture(this.getExtraItemValuesByCache(userId, type, item));
    }

    @Override
    public Future<List<Map<Long, String>>> getExtraItemsValuesByCacheAsync(Long userId, DataExtraTypeEnum type, List<String> items) {
        Valid.allNotNull(userId, type);
        Valid.notEmpty(items);
        return CompletableFuture.completedFuture(dataExtraService.getExtraItemsValuesByCache(userId, type.name(), items));
    }

    @Override
    public DataExtraDTO getExtraItem(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        Valid.allNotNull(dto.getUserId(), dto.getRelId(), dto.getItem());
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        DataExtraDO extraItem = dataExtraService.getExtraItem(request);
        return DataExtraProviderConvert.MAPPER.to(extraItem);
    }

    @Override
    public List<DataExtraDTO> getExtraItems(DataExtraQueryDTO dto, DataExtraTypeEnum type) {
        DataExtraQueryRequest request = DataExtraProviderConvert.MAPPER.to(dto);
        request.setType(type.name());
        return dataExtraService.getExtraItems(request)
                .stream()
                .map(DataExtraProviderConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Integer deleteByRelId(DataExtraTypeEnum type, Long relId) {
        return dataExtraService.deleteByRelId(type.name(), relId);
    }

    @Override
    public int deleteHostKeyExtra(Long keyId) {
        Valid.notNull(keyId);
        return dataExtraDAO.deleteHostKey(keyId);
    }

    @Override
    public int deleteHostIdentityExtra(Long identityId) {
        Valid.notNull(identityId);
        return dataExtraDAO.deleteHostIdentity(identityId);
    }

}
