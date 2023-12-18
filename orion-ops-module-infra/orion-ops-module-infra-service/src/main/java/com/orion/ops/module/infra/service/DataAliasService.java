package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.data.DataAliasUpdateRequest;

import java.util.Map;

/**
 * 数据别名 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
public interface DataAliasService {

    /**
     * 更新数据别名
     *
     * @param request request
     * @return effect
     */
    Integer updateDataAlias(DataAliasUpdateRequest request);

    /**
     * 查询数据别名
     *
     * @param userId userId
     * @param type   type
     * @param relId  relId
     * @return aliasName
     */
    String getDataAlias(Long userId, String type, Long relId);

    /**
     * 查询数据别名
     *
     * @param userId userId
     * @param type   type
     * @return relId:aliasName
     */
    Map<Long, String> getDataAlias(Long userId, String type);

    /**
     * 删除数据别名
     *
     * @param userId userId
     * @return effect
     */
    Integer deleteDataAliasByUserId(Long userId);

    /**
     * 删除数据别名
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteDataAliasByRelId(String type, Long relId);

}
