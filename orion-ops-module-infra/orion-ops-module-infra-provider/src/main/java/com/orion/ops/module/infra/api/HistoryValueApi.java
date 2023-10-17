package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.history.HistoryValueCreateDTO;
import com.orion.ops.module.infra.entity.dto.history.HistoryValueDTO;
import com.orion.ops.module.infra.enums.HistoryValueTypeEnum;

import java.util.List;

/**
 * 历史归档 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 17:33
 */
public interface HistoryValueApi {

    /**
     * 创建历史归档
     *
     * @param type type
     * @param dto  dto
     * @return id
     */
    Long createHistoryValue(HistoryValueTypeEnum type, HistoryValueCreateDTO dto);

    /**
     * 查询历史归档
     *
     * @param id id
     * @return row
     */
    HistoryValueDTO getHistoryValueById(Long id);

    /**
     * 查询历史归档
     *
     * @param id    id
     * @param relId relId
     * @param type  type
     * @return row
     */
    HistoryValueDTO getHistoryValueByRelId(Long id, Long relId, HistoryValueTypeEnum type);

    /**
     * 删除历史归档
     *
     * @param type  type
     * @param relId relId
     * @return rows
     */
    Integer deleteByRelId(HistoryValueTypeEnum type, Long relId);

    /**
     * 删除历史归档
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(HistoryValueTypeEnum type, List<Long> relIdList);

}
