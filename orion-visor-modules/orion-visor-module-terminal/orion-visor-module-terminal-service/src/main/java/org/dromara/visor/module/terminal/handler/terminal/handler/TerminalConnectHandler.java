/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.terminal.handler.terminal.handler;

import cn.orionsec.kit.lang.exception.DisabledException;
import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.common.session.config.BaseConnectConfig;
import org.dromara.visor.common.session.config.RdpConnectConfig;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.ssh.SessionStores;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.asset.api.HostApi;
import org.dromara.visor.module.asset.api.HostConnectApi;
import org.dromara.visor.module.asset.entity.dto.host.HostDTO;
import org.dromara.visor.module.common.config.GuacdConfig;
import org.dromara.visor.module.terminal.convert.TerminalSessionConvert;
import org.dromara.visor.module.terminal.define.operator.TerminalOperatorType;
import org.dromara.visor.module.terminal.entity.domain.TerminalConnectLogDO;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalConnectLogCreateRequest;
import org.dromara.visor.module.terminal.enums.TerminalConnectStatusEnum;
import org.dromara.visor.module.terminal.enums.TerminalConnectTypeEnum;
import org.dromara.visor.module.terminal.handler.terminal.constant.SessionCloseCode;
import org.dromara.visor.module.terminal.handler.terminal.constant.TerminalMessage;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelExtra;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.config.ITerminalSessionConfig;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionRdpConfig;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionSftpConfig;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionSshConfig;
import org.dromara.visor.module.terminal.handler.terminal.model.request.TerminalConnectRequest;
import org.dromara.visor.module.terminal.handler.terminal.model.transport.TerminalConnectBody;
import org.dromara.visor.module.terminal.handler.terminal.model.transport.TerminalSetInfo;
import org.dromara.visor.module.terminal.handler.terminal.sender.IGuacdTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISftpTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISshTerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.sender.ITerminalSender;
import org.dromara.visor.module.terminal.handler.terminal.session.ITerminalSession;
import org.dromara.visor.module.terminal.handler.terminal.session.RdpSession;
import org.dromara.visor.module.terminal.handler.terminal.session.SftpSession;
import org.dromara.visor.module.terminal.handler.terminal.session.SshSession;
import org.dromara.visor.module.terminal.service.TerminalConnectLogService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 连接终端 处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:32
 */
@Slf4j
@Component
public class TerminalConnectHandler extends AbstractTerminalHandler<ITerminalSender, TerminalConnectRequest> {

    @Resource
    private GuacdConfig guacdConfig;

    @Resource
    private HostApi hostApi;

    @Resource
    private HostConnectApi hostConnectApi;

    @Resource
    private TerminalConnectLogService terminalConnectLogService;

    @Override
    public void handle(TerminalChannelProps props, ITerminalSender sender, TerminalConnectRequest payload) {
        String sessionId = props.getId();
        Long userId = props.getUserId();
        Long hostId = props.getHostId();
        long startTime = System.currentTimeMillis();
        String connectType = props.getExtra().getConnectType();
        log.info("TerminalConnectHandler-handle start userId: {}, hostId: {}, sessionId: {}", userId, hostId, sessionId);
        // 检查会话是否存在
        if (this.checkSessionPresent(sender, sessionId)) {
            return;
        }
        // 查询终端信息
        HostDTO host = this.getConnectHost(sender, hostId, sessionId);
        if (host == null) {
            return;
        }
        // 记录终端日志
        TerminalConnectLogDO connectLog = this.saveTerminalConnectLog(props, host, startTime, connectType);
        Long logId = connectLog.getId();
        // 连接会话
        ITerminalSession session = null;
        try {
            // 获取连接配置
            BaseConnectConfig connectConfig = this.getConnectConfig(connectType, props, host);
            // 连接终端
            session = this.connect(sessionId, connectLog, connectConfig, props, sender, JSON.parseObject(payload.getBody(), TerminalConnectBody.class));
            // 添加会话到 manager
            terminalManager.addSession(sessionId, session);
            // 更新终端日志
            this.updateTerminalConnectLog(logId, null, null);
            // 发送设置信息
            sender.sendSetInfo(TerminalSetInfo.builder()
                    .logId(logId)
                    .address(connectConfig.getHostAddress())
                    .port(connectConfig.getHostPort())
                    .username(connectConfig.getUsername())
                    .build()
                    .toJsonString());
            // 发送已连接
            sender.sendConnected();
        } catch (Exception e) {
            String errorMessage = this.getErrorMessage(e);
            // 更新终端日志
            this.updateTerminalConnectLog(logId, e, errorMessage);
            if (session == null) {
                // 发送关闭并且关闭
                this.sendClosedAndClose(sender, errorMessage);
            } else {
                // 设置关闭状态
                session.setCloseStatus(SessionCloseCode.NORMAL, errorMessage);
                // 关闭会话 - 关闭时候会发送关闭
                terminalManager.closeSession(sessionId);
                // 关闭会话 - 兜底
                Streams.close(session);
            }
        }
    }

    /**
     * 检查 session 是否存在
     *
     * @param sender    sender
     * @param sessionId sessionId
     * @return 是否存在
     */
    private boolean checkSessionPresent(ITerminalSender sender, String sessionId) {
        // 检查 session 是否存在
        ITerminalSession session = terminalManager.getSession(sessionId);
        if (session != null) {
            log.info("TerminalConnectHandler-handle present session sessionId: {}", sessionId);
            // 关闭
            this.sendClosedAndClose(sender, ErrorMessage.SESSION_PRESENT);
            return true;
        }
        return false;
    }

    /**
     * 获取连接的主机
     *
     * @param sender    sender
     * @param hostId    hostId
     * @param sessionId sessionId
     * @return host
     */
    private HostDTO getConnectHost(ITerminalSender sender, Long hostId, String sessionId) {
        HostDTO host = hostApi.selectById(hostId);
        if (host == null) {
            log.info("TerminalConnectHandler-handle unknown host hostId: {}, sessionId: {}", hostId, sessionId);
            // 关闭
            this.sendClosedAndClose(sender, ErrorMessage.HOST_ABSENT);
            return null;
        }
        return host;
    }

    /**
     * 连接终端
     *
     * @param sessionId     sessionId
     * @param connectLog    connectLog
     * @param connectConfig connectConfig
     * @param props         props
     * @param sender        sender
     * @param connectParams connectParams
     * @return channel
     */
    private ITerminalSession connect(String sessionId,
                                     TerminalConnectLogDO connectLog,
                                     BaseConnectConfig connectConfig,
                                     TerminalChannelProps props,
                                     ITerminalSender sender,
                                     TerminalConnectBody connectParams) {
        TerminalChannelExtra extra = props.getExtra();
        String connectType = extra.getConnectType();
        try {
            ITerminalSession session;
            // 获取连接信息
            if (TerminalConnectTypeEnum.SSH.name().equals(connectType) || (TerminalConnectTypeEnum.SFTP.name().equals(connectType))) {
                // 建立连接
                SshConnectConfig sshConnectConfig = (SshConnectConfig) connectConfig;
                SessionStore sessionStore = SessionStores.openSessionStore(sshConnectConfig);
                if (TerminalConnectTypeEnum.SSH.name().equals(connectType)) {
                    // 打开 ssh 会话
                    TerminalSessionSshConfig config = TerminalSessionConvert.MAPPER.toSsh(sshConnectConfig);
                    config.setTerminalType(extra.getTerminalType());
                    this.setBaseSessionConfig(config, connectLog, connectParams);
                    session = new SshSession(props, (ISshTerminalSender) sender, config, sessionStore);
                } else {
                    // 打开 sftp 会话
                    TerminalSessionSftpConfig config = TerminalSessionConvert.MAPPER.toSftp(sshConnectConfig);
                    config.setFilePreviewSize(extra.getFilePreviewSize());
                    this.setBaseSessionConfig(config, connectLog, connectParams);
                    session = new SftpSession(props, (ISftpTerminalSender) sender, config, sessionStore);
                }
            } else if (TerminalConnectTypeEnum.RDP.name().equals(connectType)) {
                // 打开 rdp 会话
                TerminalSessionRdpConfig config = TerminalSessionConvert.MAPPER.toRdp((RdpConnectConfig) connectConfig);
                config.setDpi(connectParams.getDpi());
                this.setBaseSessionConfig(config, connectLog, connectParams);
                session = new RdpSession(props, (IGuacdTerminalSender) sender, config, guacdConfig);
            } else {
                throw Exceptions.unsupported();
            }
            // 连接
            session.connect();
            log.info("TerminalConnectHandler-handle success sessionId: {}", sessionId);
            return session;
        } catch (Exception e) {
            log.error("TerminalConnectHandler-handle error sessionId: {}", sessionId, e);
            throw e;
        }
    }

    /**
     * 获取会话连接配置
     *
     * @param connectType connectType
     * @param props       props
     * @param host        host
     * @return connect
     */
    @SuppressWarnings("unchecked")
    private <T extends BaseConnectConfig> T getConnectConfig(String connectType,
                                                             TerminalChannelProps props,
                                                             HostDTO host) {
        String sessionId = props.getId();
        Long userId = props.getUserId();
        Long hostId = host.getId();
        try {
            BaseConnectConfig connectConfig;
            // 获取连接信息
            if (TerminalConnectTypeEnum.SSH.name().equals(connectType)
                    || TerminalConnectTypeEnum.SFTP.name().equals(connectType)) {
                // SSH/SFTP
                connectConfig = hostConnectApi.getSshConnectConfig(host, userId);
            } else if (TerminalConnectTypeEnum.RDP.name().equals(connectType)) {
                // RDP
                connectConfig = hostConnectApi.getRdpConnectConfig(host, userId);
            } else {
                throw Exceptions.unsupported();
            }
            log.info("TerminalConnectHandler-handle success userId: {}, hostId: {}, sessionId: {}, type: {}", userId, hostId, sessionId, connectType);
            return (T) connectConfig;
        } catch (InvalidArgumentException e) {
            log.error("TerminalConnectHandler-handle start error userId: {}, hostId: {}, sessionId: {}, type: {}", userId, hostId, sessionId, connectType, e);
            throw e;
        } catch (DisabledException e) {
            log.error("TerminalConnectHandler-handle disabled error userId: {}, hostId: {}, sessionId: {}, type: {}", userId, hostId, sessionId, connectType);
            throw Exceptions.runtime(TerminalMessage.CONFIG_DISABLED);
        } catch (Exception e) {
            log.error("TerminalConnectHandler-handle exception userId: {}, hostId: {}, sessionId: {}, type: {}", userId, hostId, sessionId, connectType, e);
            throw Exceptions.runtime(TerminalMessage.CONNECTION_FAILED);
        }
    }

    /**
     * 设置基本会话配置
     *
     * @param config        config
     * @param connectLog    connectLog
     * @param connectParams connectParams
     */
    private void setBaseSessionConfig(ITerminalSessionConfig config,
                                      TerminalConnectLogDO connectLog,
                                      TerminalConnectBody connectParams) {
        config.setLogId(connectLog.getId());
        config.setWidth(connectParams.getWidth());
        config.setHeight(connectParams.getHeight());
    }

    /**
     * 保存操作日志/终端日志
     *
     * @param props       props
     * @param host        host
     * @param startTime   startTime
     * @param connectType connectType
     * @return connectLog
     */
    private TerminalConnectLogDO saveTerminalConnectLog(TerminalChannelProps props,
                                                        HostDTO host,
                                                        long startTime,
                                                        String connectType) {
        String sessionId = props.getId();
        Long hostId = host.getId();
        String hostName = host.getName();
        String username = props.getUsername();
        // 额外参数
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.HOST_ID, hostId);
        extra.put(OperatorLogs.HOST_NAME, hostName);
        extra.put(OperatorLogs.ADDRESS, host.getAddress());
        extra.put(OperatorLogs.CONNECT_TYPE, connectType);
        extra.put(OperatorLogs.CHANNEL, props.getChannel());
        extra.put(OperatorLogs.SESSION_ID, sessionId);
        // 记录操作日志
        OperatorLogModel logModel = this.saveOperatorLog(props,
                extra, TerminalOperatorType.CONNECT,
                startTime, null);
        // 记录连接日志
        TerminalConnectLogCreateRequest connectLogRequest = TerminalConnectLogCreateRequest.builder()
                .userId(props.getUserId())
                .username(username)
                .hostId(hostId)
                .hostName(hostName)
                .hostAddress(host.getAddress())
                .status(TerminalConnectStatusEnum.CONNECTING.name())
                .sessionId(sessionId)
                .extra(extra)
                .build();
        // 填充其他信息
        extra.put(OperatorLogs.TRACE_ID, logModel.getTraceId());
        extra.put(OperatorLogs.ADDRESS, logModel.getAddress());
        extra.put(OperatorLogs.LOCATION, logModel.getLocation());
        extra.put(OperatorLogs.USER_AGENT, logModel.getUserAgent());
        extra.put(OperatorLogs.ERROR_MESSAGE, logModel.getErrorMessage());
        // 记录连接日志
        return terminalConnectLogService.create(connectType, connectLogRequest);
    }

    /**
     * 更新终端日志/事件
     *
     * @param logId        logId
     * @param ex           ex
     * @param errorMessage errorMessage
     */
    public void updateTerminalConnectLog(Long logId,
                                         Exception ex,
                                         String errorMessage) {
        if (ex == null) {
            return;
        }
        // 修改日志状态
        Map<String, Object> extra = Maps.newMap(4);
        extra.put(ExtraFieldConst.ERROR_MESSAGE, errorMessage);
        terminalConnectLogService.updateStatusById(logId, TerminalConnectStatusEnum.FAILED, extra);
    }

}
