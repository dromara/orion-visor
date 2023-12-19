package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.data.DataAliasUpdateDTO;
import com.orion.ops.module.infra.enums.DataExtraTypeEnum;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * 数据别名 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
public interface DataAliasApi {

    /**
     * 更新数据别名
     *
     * @param dto  dto
     * @param type type
     * @return effect
     */
    Integer updateDataAlias(DataAliasUpdateDTO dto, DataExtraTypeEnum type);

    /**
     * 查询数据别名
     *
     * @param userId userId
     * @param type   type
     * @param relId  relId
     * @return aliasName
     */
    String getDataAlias(Long userId, DataExtraTypeEnum type, Long relId);

    /**
     * 查询数据别名
     *
     * @param userId userId
     * @param type   type
     * @return relId:aliasName
     */
    Map<Long, String> getDataAlias(Long userId, DataExtraTypeEnum type);

    /**
     * 异步查询数据别名
     *
     * @param userId userId
     * @param type   type
     * @return relId:aliasName
     */
    Future<Map<Long, String>> getDataAliasAsync(Long userId, DataExtraTypeEnum type);

}
