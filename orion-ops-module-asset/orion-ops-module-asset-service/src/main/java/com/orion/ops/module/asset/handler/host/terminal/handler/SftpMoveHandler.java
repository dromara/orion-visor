package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpMoveRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpBaseResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

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
        long startTime = System.currentTimeMillis();
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = terminalManager.getSession(channel.getId(), sessionId);
        String path = payload.getPath();
        String target = payload.getTarget();
        log.info("SftpMoveHandler-handle start sessionId: {}, path: {}, target: {}", sessionId, path, target);
        Exception ex = null;
        // 移动
        try {
            session.move(path, target);
            log.info("SftpMoveHandler-handle success sessionId: {}, path: {}, target: {}", sessionId, path, target);
        } catch (Exception e) {
            log.error("SftpMoveHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_MOVE,
                SftpBaseResponse.builder()
                        .sessionId(sessionId)
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(this.getErrorMessage(ex))
                        .build());
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        extra.put(OperatorLogs.TARGET, target);
        this.saveOperatorLog(payload, channel,
                extra, HostTerminalOperatorType.SFTP_MOVE,
                startTime, ex);
    }

}
