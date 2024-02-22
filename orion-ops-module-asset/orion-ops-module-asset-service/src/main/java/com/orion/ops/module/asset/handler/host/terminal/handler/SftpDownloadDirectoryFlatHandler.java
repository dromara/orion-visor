package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpDownloadDirectoryFlatRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpDownloadDirectoryFlatResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * sftp 下载文件夹 flat
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpDownloadDirectoryFlatHandler extends AbstractTerminalHandler<SftpDownloadDirectoryFlatRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpDownloadDirectoryFlatRequest payload) {
        // 获取会话
        ISftpSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        String path = payload.getPath();
        log.info("SftpDownloadDirectoryFlatHandler-handle session: {}, path: {}", payload.getSessionId(), path);
        Exception ex = null;
        // 获取文件夹内的全部文件
        try {
            // TODO

        } catch (Exception e) {
            log.error("SftpDownloadDirectoryFlatHandler-handle error", e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_DOWNLOAD_DIRECTORY_FLAT,
                SftpDownloadDirectoryFlatResponse.builder()
                        .sessionId(payload.getSessionId())
                        .currentPath(payload.getPath())
                        // TODO
                        .body("")
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(this.getErrorMessage(ex))
                        .build());
    }

}
