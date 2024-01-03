package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.id.UUIds;
import com.orion.lang.utils.collect.Maps;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogFiller;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.framework.common.enums.BooleanBit;
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
    public void handle(WebSocketSession session, TerminalCheckRequest payload) {
        Long hostId = payload.getHostId();
        Long userId = this.getAttr(session, ExtraFieldConst.USER_ID);
        long startTime = System.currentTimeMillis();
        String token = UUIds.random15();
        log.info("TerminalCheckHandler-handle start userId: {}, hostId: {}, token: {}", userId, hostId, token);
        // 查询主机信息
        HostDO host = hostDAO.selectById(hostId);
        // 不存在返回错误信息
        if (host == null) {
            log.info("TerminalCheckHandler-handle unknown host userId: {}, hostId: {}", userId, hostId);
            this.send(session,
                    OutputTypeEnum.CHECK,
                    TerminalCheckResponse.builder()
                            .session(payload.getSession())
                            .token(token)
                            .result(BooleanBit.FALSE.getValue())
                            .errorMessage(ErrorMessage.HOST_ABSENT)
                            .build());
            return;
        }
        Exception ex = null;
        try {
            // 获取连接信息
            HostTerminalConnectDTO connect = hostTerminalService.getTerminalConnectInfo(userId, host);
            // 设置到缓存中
            session.getAttributes().put(token, connect);
            log.info("TerminalCheckHandler-handle success userId: {}, hostId: {}, token: {}", userId, hostId, token);
        } catch (Exception e) {
            ex = e;
            log.error("TerminalCheckHandler-handle error userId: {}, hostId: {}, token: {}", userId, hostId, token, e);
        }
        // 记录主机日志
        this.saveTerminalLog(session, userId, host, startTime, ex, token);
        // 响应检查结果
        this.send(session,
                OutputTypeEnum.CHECK,
                TerminalCheckResponse.builder()
                        .session(payload.getSession())
                        .token(token)
                        .result(BooleanBit.of(ex == null).getValue())
                        .errorMessage(ex == null ? null : ex.getMessage())
                        .build());
    }

    /**
     * 记录主机日志
     *
     * @param session       session
     * @param userId        userId
     * @param host          host
     * @param startTime     startTime
     * @param ex            ex
     * @param terminalToken terminalToken
     */
    private void saveTerminalLog(WebSocketSession session,
                                 Long userId,
                                 HostDO host,
                                 long startTime,
                                 Exception ex,
                                 String terminalToken) {
        Long hostId = host.getId();
        String hostName = host.getName();
        String username = this.getAttr(session, ExtraFieldConst.USERNAME);
        // 额外参数
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.ID, hostId);
        extra.put(OperatorLogs.NAME, hostName);
        extra.put(OperatorLogs.TOKEN, terminalToken);
        extra.put(OperatorLogs.SESSION_ID, session.getId());
        // 日志参数
        OperatorLogFiller logModel = OperatorLogFiller.create()
                // 填充用户信息
                .fillUserInfo(userId, username)
                // 填充 traceId
                .fillTraceId(this.getAttr(session, ExtraFieldConst.TRACE_ID))
                // 填充请求留痕信息
                .fillIdentity(this.getAttr(session, ExtraFieldConst.IDENTITY))
                // 填充使用时间
                .fillUsedTime(startTime)
                // 填充结果信息
                .fillResult(null, ex)
                // 填充拓展信息
                .fillExtra(extra)
                // 填充日志
                .fillLogInfo(extra, HostTerminalOperatorType.CONNECT);
        // 记录操作日志
        operatorLogFrameworkService.insert(logModel.get());
        // 记录连接日志
        HostConnectLogCreateRequest connectLog = HostConnectLogCreateRequest.builder()
                .userId(userId)
                .username(username)
                .hostId(hostId)
                .hostName(hostName)
                .hostAddress(host.getAddress())
                .status(ex == null ? HostConnectStatusEnum.CONNECTING.name() : HostConnectStatusEnum.FAILED.name())
                .token(terminalToken)
                .extra(extra)
                .build();
        hostConnectLogService.create(HostConnectTypeEnum.SSH, connectLog);
    }

}
