package com.orion.ops.module.asset.service;

import com.orion.net.host.SessionStore;
import com.orion.ops.module.asset.entity.dto.HostSshConnectDTO;

/**
 * 主机终端服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 14:22
 */
public interface HostTerminalService {

    /**
     * 获取主机终端连接 token
     *
     * @param hostId hostId
     * @param userId userId
     * @return session
     */
    String getHostAccessToken(Long hostId, Long userId);

    /**
     * 通过 token 获取主机终端连接信息
     *
     * @param token token
     * @return config
     */
    HostSshConnectDTO getConnectInfoByToken(String token);

    /**
     * 使用默认配置打开主机会话
     *
     * @param hostId hostId
     * @return session
     */
    SessionStore openSessionStore(Long hostId);

    /**
     * 打开主机会话
     *
     * @param conn conn
     * @return session
     */
    SessionStore openSessionStore(HostSshConnectDTO conn);

}
