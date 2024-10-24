/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.request.menu.SystemMenuCreateRequest;
import com.orion.visor.module.infra.entity.request.menu.SystemMenuQueryRequest;
import com.orion.visor.module.infra.entity.request.menu.SystemMenuUpdateRequest;
import com.orion.visor.module.infra.entity.request.menu.SystemMenuUpdateStatusRequest;
import com.orion.visor.module.infra.entity.vo.SystemMenuVO;

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
