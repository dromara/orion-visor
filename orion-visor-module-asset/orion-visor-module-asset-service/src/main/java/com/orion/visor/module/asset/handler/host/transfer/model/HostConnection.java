package com.orion.visor.module.asset.handler.host.transfer.model;

import com.orion.net.host.SessionStore;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机连接信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/12 23:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostConnection {

    /**
     * connectInfo
     */
    private HostTerminalConnectDTO connectInfo;

    /**
     * sessionStore
     */
    private SessionStore sessionStore;

}
