package com.orion.ops.module.infra.service.impl;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.crypto.Signatures;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.dao.SystemUserDAO;
import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.module.infra.entity.request.operator.OperatorLogQueryRequest;
import com.orion.ops.module.infra.entity.request.user.SystemUserUpdateRequest;
import com.orion.ops.module.infra.entity.request.user.UserResetPasswordRequest;
import com.orion.ops.module.infra.entity.request.user.UserSessionOfflineRequest;
import com.orion.ops.module.infra.entity.request.user.UserUpdatePasswordRequest;
import com.orion.ops.module.infra.entity.vo.LoginHistoryVO;
import com.orion.ops.module.infra.entity.vo.OperatorLogVO;
import com.orion.ops.module.infra.entity.vo.SystemUserVO;
import com.orion.ops.module.infra.entity.vo.UserSessionVO;
import com.orion.ops.module.infra.service.MineService;
import com.orion.ops.module.infra.service.OperatorLogService;
import com.orion.ops.module.infra.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
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
        Valid.notNull(record, ErrorMessage.USER_ABSENT);
        // 对比原始密码
        String beforePassword = Signatures.md5(request.getBeforePassword());
        Valid.eq(beforePassword, record.getPassword(), ErrorMessage.BEFORE_PASSWORD_ERROR);
        // 重置密码
        UserResetPasswordRequest reset = new UserResetPasswordRequest();
        reset.setId(userId);
        reset.setPassword(request.getPassword());
        systemUserService.resetPassword(reset);
    }

    @Override
    public List<LoginHistoryVO> getCurrentLoginHistory() {
        String username = SecurityUtils.getLoginUsername();
        return operatorLogService.getLoginHistory(username);
    }

    @Override
    public List<UserSessionVO> getCurrentUserSessionList() {
        return systemUserService.getUserSessionList(SecurityUtils.getLoginUserId());
    }

    @Override
    public void offlineCurrentUserSession(UserSessionOfflineRequest request) {
        request.setUserId(SecurityUtils.getLoginUserId());
        systemUserService.offlineUserSession(request);
    }

    @Override
    public DataGrid<OperatorLogVO> getCurrentUserOperatorLog(OperatorLogQueryRequest request) {
        request.setUserId(SecurityUtils.getLoginUserId());
        return operatorLogService.getOperatorLogPage(request);
    }

}
