package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpBaseRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpBaseResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * sftp 删除文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpRemoveHandler extends AbstractTerminalHandler<SftpBaseRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpBaseRequest payload) {
        // 获取会话
        ISftpSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        String[] paths = payload.getPath().split("\\|");
        log.info("SftpRemoveHandler-handle session: {}, path: {}", payload.getSessionId(), paths);
        Exception ex = null;
        // 删除
        try {
            session.remove(paths);
        } catch (Exception e) {
            log.error("SftpRemoveHandler-handle error", e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_REMOVE,
                SftpBaseResponse.builder()
                        .sessionId(payload.getSessionId())
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(this.getErrorMessage(ex))
                        .build());
    }

}
