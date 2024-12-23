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

import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.ErrorMessage;
import org.dromara.visor.framework.common.utils.Valid;
import org.dromara.visor.module.infra.api.AuthenticationApi;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.dto.user.SystemUserAuthDTO;
import org.dromara.visor.module.infra.service.AuthenticationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 认证服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/14 21:37
 */
@Slf4j
@Service
public class AuthenticationApiImpl implements AuthenticationApi {

    @Resource
    private AuthenticationService authenticationService;

    @Override
    public SystemUserAuthDTO authByPassword(String username, String password, boolean addFailedCount) {
        SystemUserAuthDTO result = new SystemUserAuthDTO();
        try {
            // 登录预检
            SystemUserDO user = authenticationService.preCheckLogin(username, password);
            result.setId(user.getId());
            result.setUsername(user.getUsername());
            result.setNickname(user.getNickname());
            // 检查用户密码
            boolean passRight = authenticationService.checkUserPassword(user, password, addFailedCount);
            result.setPassRight(passRight);
            Valid.isTrue(passRight, ErrorMessage.USERNAME_PASSWORD_ERROR);
            // 检查用户状态
            authenticationService.checkUserStatus(user);
            result.setAuthed(true);
        } catch (Exception e) {
            result.setAuthed(false);
            result.setErrorMessage(e.getMessage());
        }
        return result;
    }

}
