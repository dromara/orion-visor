package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.domain.DataExtraDO;
import com.orion.ops.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.ops.module.infra.entity.request.data.DataExtraUpdateRequest;

import java.util.List;
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
     * 批量更新数据拓展信息
     *
     * @param map map
     */
    void batchUpdate(Map<Long, Object> map);

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
     * 查询额外配置
     *
     * @param request request
     * @return rows
     */
    List<DataExtraDO> getExtraList(DataExtraQueryRequest request);

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
