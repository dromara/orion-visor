package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpListRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpFileVO;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpListResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

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

    @Override
    public void handle(WebSocketSession channel, SftpListRequest payload) {
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = terminalManager.getSession(channel.getId(), sessionId);
        String path = payload.getPath();
        log.info("SftpListHandler-handle start sessionId: {}, path: {}", sessionId, path);
        Exception ex = null;
        List<SftpFileVO> list = Lists.empty();
        try {
            // 空目录则直接获取 home 目录
            if (Strings.isBlank(path)) {
                path = session.getHome();
            }
            // 文件列表
            list = session.list(path, BooleanBit.toBoolean(payload.getShowHiddenFile()));
            log.info("SftpListHandler-handle success sessionId: {}, path: {}", sessionId, path);
        } catch (Exception e) {
            log.error("SftpListHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_LIST,
                SftpListResponse.builder()
                        .sessionId(sessionId)
                        .result(BooleanBit.of(ex == null).getValue())
                        .path(path)
                        .body(JSON.toJSONString(list))
                        .build());
    }

}
