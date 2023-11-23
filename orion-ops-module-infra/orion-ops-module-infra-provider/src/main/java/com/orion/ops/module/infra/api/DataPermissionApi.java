package com.orion.ops.module.infra.api;

import com.orion.ops.module.infra.entity.dto.data.DataPermissionUpdateDTO;
import com.orion.ops.module.infra.enums.DataPermissionTypeEnum;

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
     * 添加数据权限
     *
     * @param type type
     * @param dto  dto
     */
    void addDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto);

    /**
     * 更新数据权限
     *
     * @param type type
     * @param dto  dto
     */
    void updateDataPermission(DataPermissionTypeEnum type, DataPermissionUpdateDTO dto);

    /**
     * 通过 userId 查询 (不包含角色 不走缓存)
     *
     * @param type   type
     * @param userId userId
     * @return relId
     */
    List<Long> getRelIdListByUserId(DataPermissionTypeEnum type, Long userId);

    /**
     * 通过 roleId 查询 不走缓存
     *
     * @param type   type
     * @param roleId roleId
     * @return relId
     */
    List<Long> getRelIdListByRoleId(DataPermissionTypeEnum type, Long roleId);

    /**
     * 通过 userId 查询 (包含角色 走缓存)
     *
     * @param type   type
     * @param userId userId
     * @return relId
     */
    List<Long> getAllowRelIdList(DataPermissionTypeEnum type, Long userId);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    int deleteByRelId(DataPermissionTypeEnum type, Long relId);

}
