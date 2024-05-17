package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.request.user.UserSessionOfflineRequest;
import com.orion.visor.module.infra.entity.vo.UserSessionVO;

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
