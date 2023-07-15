package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.entity.request.*;
import com.orion.ops.module.infra.convert.*;

import java.util.List;

/**
 * 角色 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
public interface SystemRoleService {

    /**
     * 创建角色
     *
     * @param request request
     * @return id
     */
    Long createSystemRole(SystemRoleCreateRequest request);

    /**
     * 通过 id 更新角色
     *
     * @param request request
     * @return effect
     */
    Integer updateSystemRole(SystemRoleUpdateRequest request);

    /**
     * 通过 id 查询角色
     *
     * @param id id
     * @return row
     */
    SystemRoleVO getSystemRole(Long id);

    /**
     * 通过 id 批量查询角色
     *
     * @param idList idList
     * @return rows
     */
    List<SystemRoleVO> getSystemRoleList(List<Long> idList);

    /**
     * 分页查询角色
     *
     * @param request request
     * @return rows
     */
    DataGrid<SystemRoleVO> getSystemRolePage(SystemRoleQueryRequest request);

    /**
     * 通过 id 删除角色
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemRole(Long id);

    /**
     * 通过 id 批量删除角色
     *
     * @param idList idList
     * @return effect
     */
    Integer batchDeleteSystemRole(List<Long> idList);

}
