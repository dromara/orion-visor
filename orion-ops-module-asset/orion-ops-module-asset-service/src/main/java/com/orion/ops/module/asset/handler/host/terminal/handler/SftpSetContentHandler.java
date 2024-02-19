package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpSetContentRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpBaseResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * sftp 设置文件内容
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpSetContentHandler extends AbstractTerminalHandler<SftpSetContentRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpSetContentRequest payload) {
        // 获取会话
        ISftpSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        String path = payload.getPath();
        log.info("SftpSetContentHandler-handle session: {}, path: {}", payload.getSessionId(), path);
        Exception ex = null;
        // 修改权限
        try {
            session.setContent(path, payload.getContent());
        } catch (Exception e) {
            log.error("SftpSetContentHandler-handle error", e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_SET_CONTENT,
                SftpBaseResponse.builder()
                        .sessionId(payload.getSessionId())
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(ex == null ? null : ex.getMessage())
                        .build());
    }

}
