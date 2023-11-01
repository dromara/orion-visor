package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.user.UserSessionOfflineRequest;
import com.orion.ops.module.infra.entity.request.user.SystemUserUpdateRequest;
import com.orion.ops.module.infra.entity.request.user.UserUpdatePasswordRequest;
import com.orion.ops.module.infra.entity.vo.LoginHistoryVO;
import com.orion.ops.module.infra.entity.vo.SystemUserVO;
import com.orion.ops.module.infra.entity.vo.UserSessionVO;

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
     * @return 登录日志
     */
    List<LoginHistoryVO> getCurrentLoginHistory();

    /**
     * 获取当前用户会话列表
     *
     * @return 回话列表
     */
    List<UserSessionVO> getCurrentUserSessionList();

    /**
     * 下线当前用户会话
     *
     * @param request request
     */
    void offlineCurrentUserSession(UserSessionOfflineRequest request);

}
