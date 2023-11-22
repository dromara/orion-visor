package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.data.*;

import java.util.List;

/**
 * 数据权限 对外服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
public interface DataPermissionApi {

    /**
     * 创建数据权限
     *
     * @param dto dto
     * @return id
     */
    Long createDataPermission(DataPermissionCreateDTO dto);

    /**
     * 更新数据权限
     *
     * @param dto dto
     * @return effect
     */
    Integer updateDataPermissionById(DataPermissionUpdateDTO dto);

    /**
     * 根据条件更新数据权限
     *
     * @param query  query
     * @param update update
     * @return effect
     */
    Integer updateDataPermission(DataPermissionQueryDTO query, DataPermissionUpdateDTO update);

    /**
     * 查询数据权限
     *
     * @param id id
     * @return row
     */
    DataPermissionDTO getDataPermissionById(Long id);

    /**
     * 批量查询数据权限
     *
     * @param idList idList
     * @return rows
     */
    List<DataPermissionDTO> getDataPermissionByIdList(List<Long> idList);

    /**
     * 查询全部数据权限
     *
     * @param dto dto
     * @return rows
     */
    List<DataPermissionDTO> getDataPermissionList(DataPermissionQueryDTO dto);

    /**
     * 查询数据权限数量
     *
     * @param dto dto
     * @return count
     */
    Long getDataPermissionCount(DataPermissionQueryDTO dto);

    /**
     * 删除数据权限
     *
     * @param id id
     * @return effect
     */
    Integer deleteDataPermissionById(Long id);

    /**
     * 批量删除数据权限
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteDataPermissionByIdList(List<Long> idList);

    /**
     * 根据条件删除数据权限
     *
     * @param dto dto
     * @return effect
     */
    Integer deleteDataPermission(DataPermissionQueryDTO dto);

}
