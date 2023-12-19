package com.orion.ops.module.infra.service;

import com.orion.lang.define.collect.MultiHashMap;
import com.orion.ops.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.ops.module.infra.entity.request.data.DataExtraUpdateRequest;

import java.util.Map;

/**
 * 数据拓展信息 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
public interface DataExtraService {

    /**
     * 更新数据拓展信息
     *
     * @param request request
     * @return effect
     */
    Integer updateExtraItem(DataExtraUpdateRequest request);

    /**
     * 查询额外配置项
     *
     * @param request request
     * @return items
     */
    Map<String, String> getExtraItems(DataExtraQueryRequest request);

    /**
     * 查询额外配置项
     *
     * @param request request
     * @return items
     */
    MultiHashMap<Long, String, String> getExtraItemsList(DataExtraQueryRequest request);

    /**
     * 查询额外配置项
     *
     * @param request request
     * @return item
     */
    String getExtraItem(DataExtraQueryRequest request);

    /**
     * 查询额外配置项
     *
     * @param request request
     * @return item
     */
    Map<Long, String> getExtraItemList(DataExtraQueryRequest request);

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    Integer deleteByUserId(Long userId);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(String type, Long relId);

}
