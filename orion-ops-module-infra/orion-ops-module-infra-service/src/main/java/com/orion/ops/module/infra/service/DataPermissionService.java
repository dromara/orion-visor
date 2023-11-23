package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.data.DataPermissionUpdateRequest;

import java.util.List;

/**
 * 数据权限 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-21 10:32
 */
public interface DataPermissionService {

    /**
     * 添加数据权限
     *
     * @param request request
     */
    void addDataPermission(DataPermissionUpdateRequest request);

    /**
     * 更新数据权限
     *
     * @param request request
     */
    void updateDataPermission(DataPermissionUpdateRequest request);

    /**
     * 通过 userId 查询 (不包含角色 不走缓存)
     *
     * @param type   type
     * @param userId userId
     * @return relId
     */
    List<Long> getRelIdListByUserId(String type, Long userId);

    /**
     * 通过 roleId 查询 不走缓存
     *
     * @param type   type
     * @param roleId roleId
     * @return relId
     */
    List<Long> getRelIdListByRoleId(String type, Long roleId);

    /**
     * 通过 userId 查询 (包含角色 走缓存)
     *
     * @param type   type
     * @param userId userId
     * @return relId
     */
    List<Long> getAllowRelIdList(String type, Long userId);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    int deleteByRelId(String type, Long relId);

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    int deleteByUserId(Long userId);

    /**
     * 通过 roleId 删除
     *
     * @param roleId roleId
     * @return effect
     */
    int deleteByRoleId(Long roleId);

    /**
     * 清空角色缓存
     *
     * @param roleId roleId
     */
    void clearRoleCache(Long roleId);

    /**
     * 清空用户缓存
     *
     * @param userId userId
     */
    void clearUserCache(Long userId);

    /**
     * 清空用户缓存
     *
     * @param userIdList userIdList
     */
    void clearUserCache(List<Long> userIdList);

}
