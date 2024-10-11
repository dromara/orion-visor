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
package com.orion.visor.module.infra.api;

import com.orion.visor.module.infra.entity.dto.user.SystemUserAuthDTO;

/**
 * 认证服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/14 21:37
 */
public interface AuthenticationApi {

    /**
     * 通过密码认证
     *
     * @param username       username
     * @param password       password
     * @param addFailedCount addFailedCount
     * @return result
     */
    SystemUserAuthDTO authByPassword(String username, String password, boolean addFailedCount);

}
