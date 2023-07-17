package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleCreateRequest;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleQueryRequest;
import com.orion.ops.module.infra.entity.request.role.SystemUserRoleUpdateRequest;
import com.orion.ops.module.infra.entity.vo.SystemUserRoleVO;

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
     * 创建用户角色关联
     *
     * @param request request
     * @return id
     */
    Long createSystemUserRole(SystemUserRoleCreateRequest request);

    /**
     * 通过 id 更新用户角色关联
     *
     * @param request request
     * @return effect
     */
    Integer updateSystemUserRole(SystemUserRoleUpdateRequest request);

    /**
     * 通过 id 查询用户角色关联
     *
     * @param id id
     * @return row
     */
    SystemUserRoleVO getSystemUserRole(Long id);

    /**
     * 通过 id 批量查询用户角色关联
     *
     * @param idList idList
     * @return rows
     */
    List<SystemUserRoleVO> getSystemUserRoleList(List<Long> idList);

    /**
     * 分页查询用户角色关联
     *
     * @param request request
     * @return rows
     */
    DataGrid<SystemUserRoleVO> getSystemUserRolePage(SystemUserRoleQueryRequest request);

    /**
     * 通过 id 删除用户角色关联
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemUserRole(Long id);

    /**
     * 通过 id 批量删除用户角色关联
     *
     * @param idList idList
     * @return effect
     */
    Integer batchDeleteSystemUserRole(List<Long> idList);

}
