package com.orion.ops.module.asset.service;

import com.orion.net.host.SessionStore;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.dto.HostTerminalAccessDTO;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;

/**
 * 主机终端服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 14:22
 */
public interface HostTerminalService {

    /**
     * 获取主机终端访问 accessToken
     *
     * @param userId userId
     * @return session
     */
    String getHostTerminalAccessToken(Long userId);

    /**
     * 通过 accessToken 获取主机终端访问信息
     *
     * @param token token
     * @return config
     */
    HostTerminalAccessDTO getAccessInfoByToken(String token);

    /**
     * 使用用户配置获取连接信息
     *
     * @param hostId hostId
     * @param userId userId
     * @return session
     */
    HostTerminalConnectDTO getTerminalConnectInfo(Long userId, Long hostId);

    /**
     * 使用用户配置获取连接信息
     *
     * @param host   host
     * @param userId userId
     * @return session
     */
    HostTerminalConnectDTO getTerminalConnectInfo(Long userId, HostDO host);

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
    SessionStore openSessionStore(HostTerminalConnectDTO conn);

}
