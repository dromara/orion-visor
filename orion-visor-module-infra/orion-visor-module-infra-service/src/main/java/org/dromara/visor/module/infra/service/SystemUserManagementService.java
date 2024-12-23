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
package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.request.user.UserSessionOfflineRequest;
import org.dromara.visor.module.infra.entity.vo.UserSessionVO;

import java.util.List;

/**
 * 用户管理服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/22 18:17
 */
public interface SystemUserManagementService {

    /**
     * 获取用户会话列表
     *
     * @param userId userId
     * @return 会话列表
     */
    List<UserSessionVO> getUserSessionList(Long userId);

    /**
     * 下线用户会话
     *
     * @param request request
     */
    void offlineUserSession(UserSessionOfflineRequest request);

}
