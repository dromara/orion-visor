package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.entity.request.*;
import com.orion.ops.module.infra.convert.*;

import java.util.List;

/**
 * 角色菜单关联 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
public interface SystemRoleMenuService {

    /**
     * 创建角色菜单关联
     *
     * @param request request
     * @return id
     */
    Long createSystemRoleMenu(SystemRoleMenuCreateRequest request);

    /**
     * 通过 id 更新角色菜单关联
     *
     * @param request request
     * @return effect
     */
    Integer updateSystemRoleMenu(SystemRoleMenuUpdateRequest request);

    /**
     * 通过 id 查询角色菜单关联
     *
     * @param id id
     * @return row
     */
    SystemRoleMenuVO getSystemRoleMenu(Long id);

    /**
     * 通过 id 批量查询角色菜单关联
     *
     * @param idList idList
     * @return rows
     */
    List<SystemRoleMenuVO> getSystemRoleMenuList(List<Long> idList);

    /**
     * 分页查询角色菜单关联
     *
     * @param request request
     * @return rows
     */
    DataGrid<SystemRoleMenuVO> getSystemRoleMenuPage(SystemRoleMenuQueryRequest request);

    /**
     * 通过 id 删除角色菜单关联
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemRoleMenu(Long id);

    /**
     * 通过 id 批量删除角色菜单关联
     *
     * @param idList idList
     * @return effect
     */
    Integer batchDeleteSystemRoleMenu(List<Long> idList);

}
