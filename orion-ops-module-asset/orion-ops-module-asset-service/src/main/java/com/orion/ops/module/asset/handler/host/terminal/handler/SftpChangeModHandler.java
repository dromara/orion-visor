package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpChangeModRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpBaseResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * sftp 修改文件权限
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpChangeModHandler extends AbstractTerminalHandler<SftpChangeModRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpChangeModRequest payload) {
        // 获取会话
        ISftpSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        String path = payload.getPath();
        Integer mod = payload.getMod();
        log.info("SftpChangeModHandler-handle session: {}, path: {}, mod: {}", payload.getSessionId(), path, mod);
        Exception ex = null;
        // 修改权限
        try {
            session.chmod(path, mod);
        } catch (Exception e) {
            log.error("SftpChangeModHandler-handle error", e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_CHMOD,
                SftpBaseResponse.builder()
                        .sessionId(payload.getSessionId())
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(ex == null ? null : ex.getMessage())
                        .build());
    }

}
