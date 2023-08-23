package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.menu.SystemMenuCreateRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuQueryRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuUpdateRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuUpdateStatusRequest;
import com.orion.ops.module.infra.entity.vo.SystemMenuVO;

import java.util.List;

/**
 * 菜单 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-17 11:39
 */
public interface SystemMenuService {

    /**
     * 创建菜单
     *
     * @param request request
     * @return id
     */
    Long createSystemMenu(SystemMenuCreateRequest request);

    /**
     * 通过 id 更新菜单
     *
     * @param request request
     * @return effect
     */
    Integer updateSystemMenuById(SystemMenuUpdateRequest request);

    /**
     * 通过 id 查询菜单
     *
     * @param id id
     * @return row
     */
    SystemMenuVO getSystemMenuById(Long id);

    /**
     * 查询菜单
     *
     * @param request request
     * @return rows
     */
    List<SystemMenuVO> getSystemMenuByIdList(SystemMenuQueryRequest request);

    /**
     * 构建菜单树
     *
     * @param menus menus
     * @return tree
     */
    List<SystemMenuVO> buildSystemMenuTree(List<SystemMenuVO> menus);

    /**
     * 通过 id 级联更新菜单状态
     *
     * @param request request
     * @return effect
     */
    Integer updateSystemMenuStatus(SystemMenuUpdateStatusRequest request);

    /**
     * 通过 id 级联删除菜单
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemMenu(Long id);

}
