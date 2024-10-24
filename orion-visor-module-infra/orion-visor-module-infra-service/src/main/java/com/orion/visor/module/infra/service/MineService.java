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
import com.orion.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import com.orion.visor.module.infra.entity.request.user.SystemUserUpdateRequest;
import com.orion.visor.module.infra.entity.request.user.UserSessionOfflineRequest;
import com.orion.visor.module.infra.entity.request.user.UserUpdatePasswordRequest;
import com.orion.visor.module.infra.entity.vo.LoginHistoryVO;
import com.orion.visor.module.infra.entity.vo.OperatorLogVO;
import com.orion.visor.module.infra.entity.vo.SystemUserVO;
import com.orion.visor.module.infra.entity.vo.UserSessionVO;

import java.util.List;

/**
 * 个人服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/1 0:25
 */
public interface MineService {

    /**
     * 获取当前登录用户信息
     *
     * @return user
     */
    SystemUserVO getCurrentUserInfo();

    /**
     * 更新当前登录用户信息
     *
     * @param request request
     * @return effect
     */
    Integer updateCurrentUser(SystemUserUpdateRequest request);

    /**
     * 修改当前用户密码
     *
     * @param request request
     */
    void updateCurrentUserPassword(UserUpdatePasswordRequest request);

    /**
     * 获取当前用户登录日志
     *
     * @param count count
     * @return 登录日志
     */
    List<LoginHistoryVO> getCurrentLoginHistory(Integer count);

    /**
     * 获取当前用户会话列表
     *
     * @return 会话列表
     */
    List<UserSessionVO> getCurrentUserSessionList();

    /**
     * 下线当前用户会话
     *
     * @param request request
     */
    void offlineCurrentUserSession(UserSessionOfflineRequest request);

    /**
     * 查询当前用户操作日志
     *
     * @param request request
     * @return rows
     */
    DataGrid<OperatorLogVO> getCurrentUserOperatorLog(OperatorLogQueryRequest request);

}
