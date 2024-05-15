package com.orion.visor.module.asset.handler.host.terminal.handler;

import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.enums.BooleanBit;
import com.orion.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.visor.module.asset.handler.host.terminal.model.request.SftpBaseRequest;
import com.orion.visor.module.asset.handler.host.terminal.model.response.SftpGetContentResponse;
import com.orion.visor.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * sftp 获取文件内容
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpGetContentHandler extends AbstractTerminalHandler<SftpBaseRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpBaseRequest payload) {
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = terminalManager.getSession(channel.getId(), sessionId);
        String path = payload.getPath();
        log.info("SftpGetContentHandler-handle start sessionId: {}, path: {}", sessionId, path);
        String content = Const.EMPTY;
        Exception ex = null;
        // 获取内容
        try {
            content = session.getContent(path);
            log.info("SftpGetContentHandler-handle success sessionId: {}, path: {}", sessionId, path);
        } catch (Exception e) {
            log.error("SftpGetContentHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_GET_CONTENT,
                SftpGetContentResponse.builder()
                        .sessionId(sessionId)
                        .result(BooleanBit.of(ex == null).getValue())
                        .content(content)
                        .msg(this.getErrorMessage(ex))
                        .build());
    }

}
