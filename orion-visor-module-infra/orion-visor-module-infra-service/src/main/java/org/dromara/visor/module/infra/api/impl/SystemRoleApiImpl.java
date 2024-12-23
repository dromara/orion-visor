/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.api.impl;

import org.dromara.visor.module.infra.api.SystemRoleApi;
import org.dromara.visor.module.infra.convert.SystemRoleProviderConvert;
import org.dromara.visor.module.infra.dao.SystemRoleDAO;
import org.dromara.visor.module.infra.entity.domain.SystemRoleDO;
import org.dromara.visor.module.infra.entity.dto.role.SystemRoleDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 角色服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 15:16
 */
@Service
public class SystemRoleApiImpl implements SystemRoleApi {

    @Resource
    private SystemRoleDAO systemRoleDAO;

    @Override
    public SystemRoleDTO getRoleById(Long id) {
        SystemRoleDO role = systemRoleDAO.selectById(id);
        if (role == null) {
            return null;
        }
        return SystemRoleProviderConvert.MAPPER.to(role);
    }

}
