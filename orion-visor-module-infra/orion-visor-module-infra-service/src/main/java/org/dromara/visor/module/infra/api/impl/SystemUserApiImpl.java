/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import org.dromara.visor.module.infra.api.SystemUserApi;
import org.dromara.visor.module.infra.convert.SystemUserProviderConvert;
import org.dromara.visor.module.infra.dao.SystemUserDAO;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.dto.user.SystemUserDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 15:15
 */
@Service
public class SystemUserApiImpl implements SystemUserApi {

    @Resource
    private SystemUserDAO systemUserDAO;

    @Override
    public String getUsernameById(Long id) {
        return systemUserDAO.of()
                .createWrapper()
                .select(SystemUserDO::getUsername)
                .eq(SystemUserDO::getId, id)
                .then()
                .getOne(SystemUserDO::getUsername);
    }

    @Override
    public String getNicknameById(Long id) {
        return systemUserDAO.of()
                .createWrapper()
                .select(SystemUserDO::getNickname)
                .eq(SystemUserDO::getId, id)
                .then()
                .getOne(SystemUserDO::getNickname);
    }

    @Override
    public SystemUserDTO getUserById(Long id) {
        SystemUserDO user = systemUserDAO.selectById(id);
        if (user == null) {
            return null;
        }
        return SystemUserProviderConvert.MAPPER.to(user);
    }

}
