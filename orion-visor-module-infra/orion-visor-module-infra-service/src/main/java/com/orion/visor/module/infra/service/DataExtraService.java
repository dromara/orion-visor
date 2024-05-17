package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.domain.DataExtraDO;
import com.orion.visor.module.infra.entity.request.data.DataExtraQueryRequest;
import com.orion.visor.module.infra.entity.request.data.DataExtraSetRequest;

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
     * 更新数据拓展信息 不存在则新增
     *
     * @param request request
     * @return effect
     */
    Integer setExtraItem(DataExtraSetRequest request);

    /**
     * 新增数据拓展信息
     *
     * @param request request
     * @return id
     */
    Long addExtraItem(DataExtraSetRequest request);

    /**
     * 更新数据拓展信息
     *
     * @param id    id
     * @param value value
     * @return effect
     */
    Integer updateExtraValue(Long id, String value);

    /**
     * 批量更新数据拓展信息
     *
     * @param map map
     */
    void batchUpdateExtraValue(Map<Long, String> map);

    /**
     * 查询额外配置项
     *
     * @param request request
     * @return item
     */
    String getExtraItemValue(DataExtraQueryRequest request);

    /**
     * 查询额外配置项
     *
     * @param request request
     * @return item
     */
    Map<Long, String> getExtraItemValues(DataExtraQueryRequest request);

    /**
     * 查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param item   item
     * @param relId  relId
     * @return value
     */
    String getExtraItemValueByCache(Long userId, String type, String item, Long relId);

    /**
     * 查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param item   item
     * @return relId:value
     */
    Map<Long, String> getExtraItemValuesByCache(Long userId, String type, String item);

    /**
     * 查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param items  items
     * @return [relId:value, relId:value]
     */
    List<Map<Long, String>> getExtraItemsValuesByCache(Long userId, String type, List<String> items);

    /**
     * 查询额外配置
     *
     * @param request request
     * @return rows
     */
    DataExtraDO getExtraItem(DataExtraQueryRequest request);

    /**
     * 查询额外配置
     *
     * @param request request
     * @return rows
     */
    List<DataExtraDO> getExtraItems(DataExtraQueryRequest request);

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
