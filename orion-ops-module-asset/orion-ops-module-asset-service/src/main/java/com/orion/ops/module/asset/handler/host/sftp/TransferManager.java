package com.orion.ops.module.asset.handler.host.sftp;

import com.orion.lang.define.collect.MultiConcurrentHashMap;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import org.springframework.stereotype.Component;

/**
 * 传输管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 19:05
 */
@Component
public class TransferManager {

    /**
     * 会话存储器
     */
    private final MultiConcurrentHashMap<String, Long, ITerminalSession> channelSessions = MultiConcurrentHashMap.create();


}
