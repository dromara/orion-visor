package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.request.user.SystemUserUpdateRoleRequest;

import java.util.List;

/**
 * 用户角色关联 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
public interface SystemUserRoleService {

    /**
     * 通过 userId 查询 roleId
     *
     * @param userId userId
     * @return roleId
     */
    List<Long> getRoleIdListByUserId(Long userId);

    /**
     * 通过 roleCode 查询 userId
     *
     * @param roleCode roleCode
     * @return userId
     */
    List<Long> getUserIdListByRoleCode(String roleCode);

    /**
     * 删除用户角色
     *
     * @param request request
     * @return effect
     */
    Integer deleteUserRoles(SystemUserUpdateRoleRequest request);

    /**
     * 更新用户角色
     *
     * @param request request
     * @return effect
     */
    Integer updateUserRoles(SystemUserUpdateRoleRequest request);

    /**
     * 删除用户缓存中的角色
     *
     * @param roleId     roleId
     * @param userIdList userIdList
     */
    void deleteUserCacheRoleAsync(Long roleId, List<Long> userIdList);

}
