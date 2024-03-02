package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.ops.module.asset.enums.HostConnectStatusEnum;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.request.TerminalCheckRequest;
import com.orion.ops.module.asset.handler.host.terminal.model.response.TerminalCheckResponse;
import com.orion.ops.module.asset.handler.host.terminal.session.ITerminalSession;
import com.orion.ops.module.asset.handler.host.terminal.utils.TerminalUtils;
import com.orion.ops.module.asset.service.HostConnectLogService;
import com.orion.ops.module.asset.service.HostTerminalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 主机连接检查
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalCheckHandler extends AbstractTerminalHandler<TerminalCheckRequest> {

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostTerminalService hostTerminalService;

    @Resource
    private HostConnectLogService hostConnectLogService;

    @Resource
    private OperatorLogFrameworkService operatorLogFrameworkService;

    @Override
    public void handle(WebSocketSession channel, TerminalCheckRequest payload) {
        Long hostId = payload.getHostId();
        Long userId = WebSockets.getAttr(channel, ExtraFieldConst.USER_ID);
        long startTime = System.currentTimeMillis();
        HostConnectTypeEnum connectType = HostConnectTypeEnum.of(payload.getConnectType());
        String sessionId = payload.getSessionId();
        log.info("TerminalCheckHandler-handle start userId: {}, hostId: {}, sessionId: {}", userId, hostId, sessionId);
        // 检查 session 是否存在
        if (this.checkSession(channel, payload)) {
            log.info("TerminalCheckHandler-handle present session userId: {}, hostId: {}, sessionId: {}", userId, hostId, sessionId);
            return;
        }
        // 获取主机信息
        HostDO host = this.checkHost(channel, payload, hostId);
        if (host == null) {
            log.info("TerminalCheckHandler-handle unknown host userId: {}, hostId: {}, sessionId: {}", userId, hostId, sessionId);
            return;
        }
        Exception ex = null;
        try {
            // 获取连接信息
            HostTerminalConnectDTO connect = hostTerminalService.getTerminalConnectInfo(userId, host, connectType);
            // 设置到缓存中
            channel.getAttributes().put(sessionId, connect);
            log.info("TerminalCheckHandler-handle success userId: {}, hostId: {}, sessionId: {}", userId, hostId, sessionId);
        } catch (InvalidArgumentException e) {
            ex = e;
            log.error("TerminalCheckHandler-handle error userId: {}, hostId: {}, sessionId: {}", userId, hostId, sessionId, e);
        } catch (Exception e) {
            ex = Exceptions.runtime(ErrorMessage.CONNECT_ERROR);
            log.error("TerminalCheckHandler-handle exception userId: {}, hostId: {}, sessionId: {}", userId, hostId, sessionId, e);
        }
        // 记录主机日志
        this.saveHostLog(channel, userId, host, startTime, ex, sessionId, connectType);
        // 响应检查结果
        this.send(channel,
                OutputTypeEnum.CHECK,
                TerminalCheckResponse.builder()
                        .sessionId(payload.getSessionId())
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(ex == null ? null : ex.getMessage())
                        .build());
    }

    /**
     * 检查会话是否存在
     *
     * @param channel channel
     * @param payload payload
     * @return 是否存在
     */
    private boolean checkSession(WebSocketSession channel, TerminalCheckRequest payload) {
        ITerminalSession session = terminalManager.getSession(channel.getId(), payload.getSessionId());
        if (session != null) {
            this.sendCheckFailedMessage(channel, payload, ErrorMessage.SESSION_PRESENT);
            return true;
        }
        return false;
    }

    /**
     * 获取主机信息
     *
     * @param channel channel
     * @param payload payload
     * @param hostId  hostId
     * @return host
     */
    private HostDO checkHost(WebSocketSession channel, TerminalCheckRequest payload, Long hostId) {
        // 查询主机信息
        HostDO host = hostDAO.selectById(hostId);
        // 不存在返回错误信息
        if (host == null) {
            this.sendCheckFailedMessage(channel, payload, ErrorMessage.HOST_ABSENT);
        }
        return host;
    }

    /**
     * 发送检查失败消息
     *
     * @param channel channel
     * @param payload payload
     * @param msg     msg
     */
    private void sendCheckFailedMessage(WebSocketSession channel, TerminalCheckRequest payload, String msg) {
        TerminalCheckResponse resp = TerminalCheckResponse.builder()
                .sessionId(payload.getSessionId())
                .result(BooleanBit.FALSE.getValue())
                .msg(msg)
                .build();
        // 发送
        this.send(channel, OutputTypeEnum.CHECK, resp);
    }

    /**
     * 记录主机日志
     *
     * @param channel     channel
     * @param userId      userId
     * @param host        host
     * @param startTime   startTime
     * @param ex          ex
     * @param sessionId   sessionId
     * @param connectType connectType
     */
    private void saveHostLog(WebSocketSession channel,
                             Long userId,
                             HostDO host,
                             long startTime,
                             Exception ex,
                             String sessionId,
                             HostConnectTypeEnum connectType) {
        Long hostId = host.getId();
        String hostName = host.getName();
        String username = WebSockets.getAttr(channel, ExtraFieldConst.USERNAME);
        // 额外参数
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.HOST_ID, hostId);
        extra.put(OperatorLogs.HOST_NAME, hostName);
        extra.put(OperatorLogs.CONNECT_TYPE, connectType.name());
        extra.put(OperatorLogs.CHANNEL_ID, channel.getId());
        extra.put(OperatorLogs.SESSION_ID, sessionId);
        // 日志参数
        OperatorLogModel logModel = TerminalUtils.getOperatorLogModel(channel, extra,
                HostTerminalOperatorType.CONNECT, startTime, ex);
        // 记录操作日志
        operatorLogFrameworkService.insert(logModel);
        // 记录连接日志
        HostConnectLogCreateRequest connectLog = HostConnectLogCreateRequest.builder()
                .userId(userId)
                .username(username)
                .hostId(hostId)
                .hostName(hostName)
                .hostAddress(host.getAddress())
                .status(ex == null ? HostConnectStatusEnum.CONNECTING.name() : HostConnectStatusEnum.FAILED.name())
                .token(sessionId)
                .extra(extra)
                .build();
        // 填充其他信息
        extra.put(OperatorLogs.TRACE_ID, logModel.getTraceId());
        extra.put(OperatorLogs.ADDRESS, logModel.getAddress());
        extra.put(OperatorLogs.LOCATION, logModel.getLocation());
        extra.put(OperatorLogs.USER_AGENT, logModel.getUserAgent());
        extra.put(OperatorLogs.ERROR_MESSAGE, logModel.getErrorMessage());
        // 记录连接日志
        hostConnectLogService.create(connectType, connectLog);
    }

}
