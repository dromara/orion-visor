package com.orion.ops.module.asset.service;

import com.orion.net.host.SessionStore;

/**
 * 主机连接服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 14:22
 */
public interface HostConnectService {

    /**
     * 打开主机会话
     * 鉴权并且读取用户配置
     *
     * @param hostId hostId
     * @param userId userId
     * @return session
     */
    SessionStore openSessionStore(Long hostId, Long userId);

    /**
     * 打开主机会话
     * 使用默认配置 不鉴权
     *
     * @param hostId hostId
     * @return session
     */
    SessionStore openSessionStore(Long hostId);

}
