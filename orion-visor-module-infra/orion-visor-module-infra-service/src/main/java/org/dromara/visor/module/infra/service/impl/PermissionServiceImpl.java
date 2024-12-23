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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import org.dromara.visor.module.infra.dao.SystemRoleDAO;
import org.dromara.visor.module.infra.define.RoleDefine;
import org.dromara.visor.module.infra.entity.domain.SystemRoleDO;
import org.dromara.visor.module.infra.enums.RoleStatusEnum;
import org.dromara.visor.module.infra.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/19 15:29
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private SystemRoleDAO systemRoleDAO;

    @Override
    public boolean isAdminUser(Long userId) {
        return this.hasAnyRole(userId, Lists.of(RoleDefine.ADMIN_CODE));
    }

    @Override
    public boolean hasRole(Long userId, String role) {
        return this.hasAnyRole(userId, Lists.of(role));
    }

    @Override
    public boolean hasAnyRole(Long userId, List<String> roles) {
        return !systemRoleDAO.getRoleIdByUserIdAndRoleCode(userId, roles).isEmpty();
    }

    @Override
    public boolean hasPermission(Long userId, String permission) {
        return this.hasAnyPermission(userId, Lists.singleton(permission));
    }

    @Override
    public boolean hasAnyPermission(Long userId, List<String> permissions) {
        // 查询用户角色
        List<SystemRoleDO> roles = systemRoleDAO.selectRoleByUserId(userId);
        roles.removeIf(s -> !RoleStatusEnum.ENABLED.getStatus().equals(s.getStatus()));
        if (roles.isEmpty()) {
            return false;
        }
        // 判断是否为 admin
        boolean isAdmin = roles.stream().anyMatch(s -> s.getCode().equals(RoleDefine.ADMIN_CODE));
        if (isAdmin) {
            return true;
        }
        List<Long> roleIdList = roles.stream()
                .map(SystemRoleDO::getId)
                .collect(Collectors.toList());
        return !systemRoleDAO.getPermissionByRoleIdAndPermission(roleIdList, permissions).isEmpty();
    }

}
