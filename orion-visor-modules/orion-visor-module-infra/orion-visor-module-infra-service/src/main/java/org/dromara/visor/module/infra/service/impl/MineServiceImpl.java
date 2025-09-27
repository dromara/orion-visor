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
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.crypto.Signatures;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.dao.SystemUserDAO;
import org.dromara.visor.module.infra.entity.domain.SystemUserDO;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import org.dromara.visor.module.infra.entity.request.user.SystemUserUpdateRequest;
import org.dromara.visor.module.infra.entity.request.user.UserResetPasswordRequest;
import org.dromara.visor.module.infra.entity.request.user.UserSessionOfflineRequest;
import org.dromara.visor.module.infra.entity.request.user.UserUpdatePasswordRequest;
import org.dromara.visor.module.infra.entity.vo.LoginHistoryVO;
import org.dromara.visor.module.infra.entity.vo.OperatorLogVO;
import org.dromara.visor.module.infra.entity.vo.SystemUserVO;
import org.dromara.visor.module.infra.entity.vo.UserSessionVO;
import org.dromara.visor.module.infra.service.MineService;
import org.dromara.visor.module.infra.service.OperatorLogService;
import org.dromara.visor.module.infra.service.SystemUserManagementService;
import org.dromara.visor.module.infra.service.SystemUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 个人服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/1 0:25
 */
@Slf4j
@Service
public class MineServiceImpl implements MineService {

    @Resource
    private SystemUserService systemUserService;

    @Resource
    private SystemUserManagementService systemUserManagementService;

    @Resource
    private OperatorLogService operatorLogService;

    @Resource
    private SystemUserDAO systemUserDAO;

    @Override
    public SystemUserVO getCurrentUserInfo() {
        return systemUserService.getSystemUserById(SecurityUtils.getLoginUserId());
    }

    @Override
    public Integer updateCurrentUser(SystemUserUpdateRequest request) {
        request.setId(SecurityUtils.getLoginUserId());
        return systemUserService.updateSystemUserById(request);
    }

    @Override
    public void updateCurrentUserPassword(UserUpdatePasswordRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        // 查询用户信息
        SystemUserDO record = systemUserDAO.selectById(userId);
        Assert.notNull(record, ErrorMessage.USER_ABSENT);
        // 对比原始密码
        String beforePassword = Signatures.md5(request.getBeforePassword());
        Assert.eq(beforePassword, record.getPassword(), ErrorMessage.BEFORE_PASSWORD_ERROR);
        // 重置密码
        UserResetPasswordRequest reset = new UserResetPasswordRequest();
        reset.setId(userId);
        reset.setPassword(request.getPassword());
        systemUserService.resetPassword(reset);
    }

    @Override
    public List<LoginHistoryVO> getCurrentLoginHistory(Integer count) {
        String username = SecurityUtils.getLoginUsername();
        return operatorLogService.getLoginHistory(username, count);
    }

    @Override
    public List<UserSessionVO> getCurrentUserSessionList() {
        return systemUserManagementService.getUserSessionList(SecurityUtils.getLoginUserId());
    }

    @Override
    public void offlineCurrentUserSession(UserSessionOfflineRequest request) {
        request.setUserId(SecurityUtils.getLoginUserId());
        systemUserManagementService.offlineUserSession(request);
    }

    @Override
    public DataGrid<OperatorLogVO> getCurrentUserOperatorLog(OperatorLogQueryRequest request) {
        request.setUserId(SecurityUtils.getLoginUserId());
        return operatorLogService.getOperatorLogPage(request);
    }

}
