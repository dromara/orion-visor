package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.entity.dto.data.DataExtraDTO;
import com.orion.visor.module.infra.entity.dto.data.DataExtraQueryDTO;
import com.orion.visor.module.infra.entity.dto.data.DataExtraSetDTO;
import com.orion.visor.module.infra.enums.DataExtraTypeEnum;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 数据拓展信息 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
public interface DataExtraApi {

    /**
     * 更新数据拓展信息 不存在则新增
     *
     * @param type type
     * @param dto  dto
     * @return effect
     */
    Integer setExtraItem(DataExtraSetDTO dto, DataExtraTypeEnum type);

    /**
     * 新增数据拓展信息
     *
     * @param dto  dto
     * @param type type
     * @return id
     */
    Long addExtraItem(DataExtraSetDTO dto, DataExtraTypeEnum type);

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
     * @param type type
     * @param dto  dto
     * @return item
     */
    String getExtraValue(DataExtraQueryDTO dto, DataExtraTypeEnum type);

    /**
     * 查询额外配置项
     *
     * @param dto  dto
     * @param type type
     * @return item
     */
    Map<Long, String> getExtraItemValues(DataExtraQueryDTO dto, DataExtraTypeEnum type);

    /**
     * 查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param item   item
     * @param relId  relId
     * @return value
     */
    String getExtraItemValueByCache(Long userId, DataExtraTypeEnum type, String item, Long relId);

    /**
     * 查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param item   item
     * @return relId:value
     */
    Map<Long, String> getExtraItemValuesByCache(Long userId, DataExtraTypeEnum type, String item);

    /**
     * 异步查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param item   item
     * @return value
     */
    Future<Map<Long, String>> getExtraItemValuesByCacheAsync(Long userId, DataExtraTypeEnum type, String item);

    /**
     * 异步查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param items  items
     * @return value
     */
    Future<List<Map<Long, String>>> getExtraItemsValuesByCacheAsync(Long userId, DataExtraTypeEnum type, List<String> items);

    /**
     * 查询额外配置
     *
     * @param dto  dto
     * @param type type
     * @return effect
     */
    DataExtraDTO getExtraItem(DataExtraQueryDTO dto, DataExtraTypeEnum type);

    /**
     * 查询额外配置
     *
     * @param dto  dto
     * @param type type
     * @return effect
     */
    List<DataExtraDTO> getExtraItems(DataExtraQueryDTO dto, DataExtraTypeEnum type);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(DataExtraTypeEnum type, Long relId);

    /**
     * 删除主机密钥
     *
     * @param keyId keyId
     * @return effect
     */
    int deleteHostKeyExtra(Long keyId);

    /**
     * 删除主机身份
     *
     * @param identityId identityId
     * @return effect
     */
    int deleteHostIdentityExtra(Long identityId);

}
