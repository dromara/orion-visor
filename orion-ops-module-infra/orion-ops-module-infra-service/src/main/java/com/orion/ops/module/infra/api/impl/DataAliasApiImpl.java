package com.orion.ops.module.infra.api.impl;

import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.api.DataAliasApi;
import com.orion.ops.module.infra.entity.dto.data.DataAliasUpdateDTO;
import com.orion.ops.module.infra.entity.request.data.DataAliasUpdateRequest;
import com.orion.ops.module.infra.enums.DataExtraTypeEnum;
import com.orion.ops.module.infra.service.DataAliasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * 数据别名 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
@Slf4j
@Service
public class DataAliasApiImpl implements DataAliasApi {

    @Resource
    private DataAliasService dataAliasService;

    @Override
    public Integer updateDataAlias(DataAliasUpdateDTO dto, DataExtraTypeEnum type) {
        Valid.valid(dto);
        DataAliasUpdateRequest update = DataAliasUpdateRequest.builder()
                .userId(dto.getUserId())
                .type(type.name())
                .relId(dto.getRelId())
                .alias(dto.getAlias())
                .build();
        return dataAliasService.updateDataAlias(update);
    }

    @Override
    public String getDataAlias(Long userId, DataExtraTypeEnum type, Long relId) {
        Valid.allNotNull(userId, relId);
        return dataAliasService.getDataAlias(userId, type.name(), relId);
    }

    @Override
    public Map<Long, String> getDataAlias(Long userId, DataExtraTypeEnum type) {
        Valid.notNull(userId);
        return dataAliasService.getDataAlias(userId, type.name());
    }

    @Override
    public Future<Map<Long, String>> getDataAliasAsync(Long userId, DataExtraTypeEnum type) {
        Valid.notNull(userId);
        return CompletableFuture.completedFuture(dataAliasService.getDataAlias(userId, type.name()));
    }

}
