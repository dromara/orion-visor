package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Strings;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalManager;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpListRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpFileResponse;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpListResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.List;

/**
 * sftp 文件列表
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class SftpListHandler extends AbstractTerminalHandler<SftpListRequest> {

    @Resource
    private TerminalManager terminalManager;

    @Override
    public void handle(WebSocketSession channel, SftpListRequest payload) {
        // 获取会话
        ITerminalSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        String path = payload.getPath();
        log.info("SftpListHandler-handle session: {}, path: {}", payload.getSessionId(), path);
        if (session instanceof ISftpSession) {
            Exception ex = null;
            List<SftpFileResponse> list = null;
            try {
                // 空目录则直接获取 home 目录
                if (Strings.isBlank(path)) {
                    path = ((ISftpSession) session).getHome();
                }
                // 文件列表
                list = ((ISftpSession) session).list(path);
            } catch (Exception e) {
                log.error("SftpListHandler-handle error", e);
                ex = e;
            }
            // 返回
            this.send(channel,
                    OutputTypeEnum.SFTP_LIST,
                    SftpListResponse.builder()
                            .sessionId(payload.getSessionId())
                            .result(BooleanBit.of(ex == null).getValue())
                            .path(path)
                            .body(list == null ? null : JSON.toJSONString(list))
                            .build());
        }
    }

}
