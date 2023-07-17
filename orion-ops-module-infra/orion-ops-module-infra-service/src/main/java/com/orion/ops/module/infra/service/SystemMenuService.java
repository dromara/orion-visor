package com.orion.ops.module.infra.service;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuCreateRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuQueryRequest;
import com.orion.ops.module.infra.entity.request.menu.SystemMenuUpdateRequest;
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
    Integer updateSystemMenu(SystemMenuUpdateRequest request);

    /**
     * 通过 id 查询菜单
     *
     * @param id id
     * @return row
     */
    SystemMenuVO getSystemMenu(Long id);

    /**
     * 通过 id 批量查询菜单
     *
     * @param idList idList
     * @return rows
     */
    List<SystemMenuVO> getSystemMenuList(List<Long> idList);

    /**
     * 分页查询菜单
     *
     * @param request request
     * @return rows
     */
    DataGrid<SystemMenuVO> getSystemMenuPage(SystemMenuQueryRequest request);

    /**
     * 通过 id 删除菜单
     *
     * @param id id
     * @return effect
     */
    Integer deleteSystemMenu(Long id);

    /**
     * 通过 id 批量删除菜单
     *
     * @param idList idList
     * @return effect
     */
    Integer batchDeleteSystemMenu(List<Long> idList);

}
