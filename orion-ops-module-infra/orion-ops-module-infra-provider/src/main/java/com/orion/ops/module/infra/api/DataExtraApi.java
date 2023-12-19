package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.data.DataExtraDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraQueryDTO;
import com.orion.ops.module.infra.entity.dto.data.DataExtraUpdateDTO;
import com.orion.ops.module.infra.enums.DataExtraTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * 数据拓展信息 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
public interface DataExtraApi {

    /**
     * 更新数据拓展信息
     *
     * @param type type
     * @param dto  dto
     * @return effect
     */
    Integer updateExtraItem(DataExtraUpdateDTO dto, DataExtraTypeEnum type);

    /**
     * 批量更新数据拓展信息
     *
     * @param map map
     */
    void batchUpdate(Map<Long, Object> map);

    /**
     * 查询额外配置项
     *
     * @param type type
     * @param dto  dto
     * @return item
     */
    String getExtraItem(DataExtraQueryDTO dto, DataExtraTypeEnum type);

    /**
     * 查询额外配置项
     *
     * @param dto  dto
     * @param type type
     * @return item
     */
    Map<Long, String> getExtraItemList(DataExtraQueryDTO dto, DataExtraTypeEnum type);

    /**
     * 查询额外配置
     *
     * @param dto  dto
     * @param type type
     * @return effect
     */
    List<DataExtraDTO> getExtraList(DataExtraQueryDTO dto, DataExtraTypeEnum type);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(DataExtraTypeEnum type, Long relId);

}
