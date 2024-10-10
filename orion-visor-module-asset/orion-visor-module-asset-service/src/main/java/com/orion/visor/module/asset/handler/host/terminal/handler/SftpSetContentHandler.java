package com.orion.visor.module.asset.handler.host.terminal.handler;

import com.orion.lang.id.UUIds;
import com.orion.lang.utils.collect.Maps;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.enums.BooleanBit;
import com.orion.visor.framework.redis.core.utils.RedisStrings;
import com.orion.visor.module.asset.define.cache.HostTerminalCacheKeyDefine;
import com.orion.visor.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.visor.module.asset.entity.dto.SftpSetContentCacheDTO;
import com.orion.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.visor.module.asset.handler.host.terminal.model.request.SftpBaseRequest;
import com.orion.visor.module.asset.handler.host.terminal.model.response.SftpSetContentResponse;
import com.orion.visor.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * sftp 设置文件内容
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpSetContentHandler extends AbstractTerminalHandler<SftpBaseRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpBaseRequest payload) {
        long startTime = System.currentTimeMillis();
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = hostTerminalManager.getSession(channel.getId(), sessionId);
        String path = payload.getPath();
        log.info("SftpSetContentHandler-handle start sessionId: {}, path: {}", sessionId, path);
        String token = UUIds.random32();
        Exception ex = null;
        try {
            // 检查文件是否可编辑
            session.checkCanEdit(path);
            // 设置缓存
            String key = HostTerminalCacheKeyDefine.SFTP_SET_CONTENT.format(token);
            SftpSetContentCacheDTO cache = SftpSetContentCacheDTO.builder()
                    .hostId(session.getConfig().getHostId())
                    .path(path)
                    .build();
            RedisStrings.setJson(key, HostTerminalCacheKeyDefine.SFTP_SET_CONTENT, cache);
            log.info("SftpSetContentHandler-handle success sessionId: {}, path: {}", sessionId, path);
        } catch (Exception e) {
            log.error("SftpSetContentHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_SET_CONTENT,
                SftpSetContentResponse.builder()
                        .sessionId(sessionId)
                        .result(BooleanBit.of(ex == null).getValue())
                        .token(token)
                        .msg(this.getErrorMessage(ex))
                        .build());
        // 保存操作日志
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        this.saveOperatorLog(payload, channel,
                extra, HostTerminalOperatorType.SFTP_SET_CONTENT,
                startTime, ex);
    }

}
