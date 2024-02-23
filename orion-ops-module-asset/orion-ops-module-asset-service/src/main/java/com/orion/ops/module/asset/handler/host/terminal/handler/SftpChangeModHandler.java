package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.SftpChangeModRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.SftpBaseResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

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
        long startTime = System.currentTimeMillis();
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = terminalManager.getSession(channel.getId(), sessionId);
        String path = payload.getPath();
        Integer mod = payload.getMod();
        log.info("SftpChangeModHandler-handle start sessionId: {}, path: {}, mod: {}", sessionId, path, mod);
        Exception ex = null;
        // 修改权限
        try {
            session.chmod(path, mod);
            log.info("SftpChangeModHandler-handle success sessionId: {}, path: {}, mod: {}", sessionId, path, mod);
        } catch (Exception e) {
            log.error("SftpChangeModHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_CHMOD,
                SftpBaseResponse.builder()
                        .sessionId(sessionId)
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(this.getErrorMessage(ex))
                        .build());
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        extra.put(OperatorLogs.MOD, mod);
        this.saveOperatorLog(payload, channel,
                extra, HostTerminalOperatorType.SFTP_CHMOD,
                startTime, ex);
    }

}
