package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.data.*;

import java.util.List;

/**
 * 数据分组 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupApi {

    /**
     * 创建数据分组
     *
     * @param dto dto
     * @return id
     */
    Long createDataGroup(DataGroupCreateDTO dto);

    /**
     * 更新数据分组
     *
     * @param dto dto
     * @return effect
     */
    Integer updateDataGroupById(DataGroupUpdateDTO dto);

    /**
     * 根据条件更新数据分组
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateDataGroup(DataGroupQueryDTO query, DataGroupUpdateDTO update);

    /**
     * 查询数据分组
     *
     * @param id id
     * @return row
     */
    DataGroupDTO getDataGroupById(Long id);

    /**
     * 批量查询数据分组
     *
     * @param idList idList
     * @return rows
     */
    List<DataGroupDTO> getDataGroupByIdList(List<Long> idList);

    /**
     * 查询全部数据分组
     *
     * @param dto dto
     * @return rows
     */
    List<DataGroupDTO> getDataGroupList(DataGroupQueryDTO dto);

    /**
     * 通过缓存查询数据分组
     *
     * @return rows
     */
    List<DataGroupDTO> getDataGroupListByCache();

    /**
     * 查询数据分组数量
     *
     * @param dto dto
     * @return count
     */
    Long getDataGroupCount(DataGroupQueryDTO dto);

    /**
     * 删除数据分组
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataGroupById(Long id);

    /**
     * 批量删除数据分组
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteDataGroupByIdList(List<Long> idList);

    /**
     * 根据条件删除数据分组
     *
     * @param dto dto
     * @return effect
     */
    Integer deleteDataGroup(DataGroupQueryDTO dto);

}
