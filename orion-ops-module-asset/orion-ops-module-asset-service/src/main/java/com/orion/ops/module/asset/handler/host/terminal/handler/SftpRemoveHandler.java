package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpRemoveRequest;
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
public class SftpRemoveHandler extends AbstractTerminalHandler<SftpRemoveRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpRemoveRequest payload) {
    }

}
