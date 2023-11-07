package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.data.*;

import java.util.List;

/**
 * 数据分组关联 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupRelApi {

    /**
     * 创建数据分组关联
     *
     * @param dto dto
     * @return id
     */
    Long createDataGroupRel(DataGroupRelCreateDTO dto);

    /**
     * 更新数据分组关联
     *
     * @param dto dto
     * @return effect
     */
    Integer updateDataGroupRelById(DataGroupRelUpdateDTO dto);

    /**
     * 根据条件更新数据分组关联
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateDataGroupRel(DataGroupRelQueryDTO query, DataGroupRelUpdateDTO update);

    /**
     * 查询数据分组关联
     *
     * @param id id
     * @return row
     */
    DataGroupRelDTO getDataGroupRelById(Long id);

    /**
     * 批量查询数据分组关联
     *
     * @param idList idList
     * @return rows
     */
    List<DataGroupRelDTO> getDataGroupRelByIdList(List<Long> idList);

    /**
     * 查询全部数据分组关联
     *
     * @param dto dto
     * @return rows
     */
    List<DataGroupRelDTO> getDataGroupRelList(DataGroupRelQueryDTO dto);

    /**
     * 通过缓存查询数据分组关联
     *
     * @return rows
     */
    List<DataGroupRelDTO> getDataGroupRelListByCache();

    /**
     * 查询数据分组关联数量
     *
     * @param dto dto
     * @return count
     */
    Long getDataGroupRelCount(DataGroupRelQueryDTO dto);

    /**
     * 删除数据分组关联
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataGroupRelById(Long id);

    /**
     * 批量删除数据分组关联
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupRelByIdList(List<Long> idList);

    /**
     * 根据条件删除数据分组关联
     *
     * @param dto dto
     * @return effect
     */
    Integer deleteDataGroupRel(DataGroupRelQueryDTO dto);

}
