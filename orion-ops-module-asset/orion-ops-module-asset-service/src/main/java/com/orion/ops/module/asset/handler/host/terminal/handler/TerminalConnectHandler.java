package com.orion.ops.module.asset.handler.host.terminal.handler;

import com.orion.lang.utils.collect.Maps;
import com.orion.net.host.SessionStore;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogFiller;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogCreateRequest;
import com.orion.ops.module.asset.enums.HostConnectTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.entity.Message;
import com.orion.ops.module.asset.handler.host.terminal.entity.request.TerminalConnectRequest;
import com.orion.ops.module.asset.handler.host.terminal.manager.TerminalSession;
import com.orion.ops.module.asset.service.HostConnectLogService;
import com.orion.ops.module.asset.service.HostTerminalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 连接主机处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalConnectHandler extends AbstractTerminalHandler<TerminalConnectRequest> {

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostTerminalService hostTerminalService;

    @Resource
    private HostConnectLogService hostConnectLogService;

    @Resource
    private OperatorLogFrameworkService operatorLogFrameworkService;

    public TerminalConnectHandler() {
        super(TerminalConnectRequest.class);
    }

    @Override
    protected void handle(WebSocketSession session, Message<TerminalConnectRequest> msg) {
        TerminalConnectRequest body = msg.getBody();
        Long hostId = body.getHostId();
        Long userId = this.getAttr(session, ExtraFieldConst.USER_ID);
        log.info("TerminalConnectHandler-handle start userId: {}, hostId: {}", userId, hostId);
        // 查询主机信息
        HostDO host = hostDAO.selectById(hostId);
        if (host == null) {
            // TODO 不存在返回错误信息

            return;
        }
        // 日志信息
        long startTime = System.currentTimeMillis();
        Exception ex = null;
        String terminalToken = null;
        try {
            // 连接主机
            HostTerminalConnectDTO connect = hostTerminalService.getTerminalConnectInfo(userId, host);
            terminalToken = connect.getToken();
            SessionStore sessionStore = hostTerminalService.openSessionStore(connect);
            TerminalSession terminalSession = new TerminalSession(session, connect, sessionStore);
            terminalSession.connect(body.getCols(), body.getRows());
            log.info("TerminalConnectHandler-handle success userId: {}, hostId: {}", userId, hostId);
            // TODO 添加到 manager

        } catch (Exception e) {
            log.error("TerminalConnectHandler-handle error userId: {}, hostId: {}", userId, hostId, e);
            ex = e;
        } finally {
            // 记录主机日志
            this.saveTerminalLog(session, userId, host, startTime, ex, terminalToken);
        }
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
                .token(terminalToken)
                .build();
        hostConnectLogService.create(HostConnectTypeEnum.SSH, connectLog);
    }

}
