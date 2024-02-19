package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpMoveRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpBaseResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * sftp 移动文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpMoveHandler extends AbstractTerminalHandler<SftpMoveRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpMoveRequest payload) {
        // 获取会话
        ISftpSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        String path = payload.getPath();
        String target = payload.getTarget();
        log.info("SftpMoveHandler-handle session: {}, path: {}, target: {}", payload.getSessionId(), path, target);
        Exception ex = null;
        // 移动
        try {
            session.move(path, target);
        } catch (Exception e) {
            log.error("SftpMoveHandler-handle error", e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_MOVE,
                SftpBaseResponse.builder()
                        .sessionId(payload.getSessionId())
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(ex == null ? null : ex.getMessage())
                        .build());
    }

}
