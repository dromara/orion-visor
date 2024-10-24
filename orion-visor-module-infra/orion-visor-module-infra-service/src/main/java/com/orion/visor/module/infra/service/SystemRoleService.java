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

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.module.infra.entity.request.role.SystemRoleCreateRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleQueryRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleStatusRequest;
import com.orion.visor.module.infra.entity.request.role.SystemRoleUpdateRequest;
import com.orion.visor.module.infra.entity.vo.SystemRoleVO;

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
    Integer updateSystemRoleById(SystemRoleUpdateRequest request);

    /**
     * 更新角色状态
     *
     * @param request request
     * @return effect
     */
    Integer updateRoleStatus(SystemRoleStatusRequest request);

    /**
     * 通过 id 查询角色
     *
     * @param id id
     * @return row
     */
    SystemRoleVO getSystemRoleById(Long id);

    /**
     * 查询所有角色
     *
     * @return rows
     */
    List<SystemRoleVO> getSystemRoleByIdList();

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
    Integer deleteSystemRoleById(Long id);

}
