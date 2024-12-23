/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.request.menu.SystemRoleGrantMenuRequest;

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
     * 分配角色菜单
     *
     * @param request request
     * @return effect
     */
    Integer grantRoleMenu(SystemRoleGrantMenuRequest request);

    /**
     * 获取角色菜单 id
     *
     * @param roleId roleId
     * @return menuIdList
     */
    List<Long> getRoleMenuIdList(Long roleId);

}
