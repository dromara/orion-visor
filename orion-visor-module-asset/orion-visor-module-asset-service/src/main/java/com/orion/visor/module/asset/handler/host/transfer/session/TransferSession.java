package com.orion.visor.module.asset.handler.host.transfer.session;

import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.spring.SpringHolder;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.module.asset.define.config.AppSftpConfig;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.handler.host.terminal.utils.TerminalUtils;
import com.orion.visor.module.asset.handler.host.transfer.enums.TransferReceiver;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import com.orion.visor.module.asset.handler.host.transfer.utils.TransferUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * 主机传输会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 21:12
 */
@Slf4j
public abstract class TransferSession implements ITransferSession {

    protected static final AppSftpConfig SFTP_CONFIG = SpringHolder.getBean(AppSftpConfig.class);

    protected final HostTerminalConnectDTO connectInfo;

    protected final SessionStore sessionStore;

    protected final WebSocketSession channel;

    protected SftpExecutor executor;

    protected String channelId;

    @Getter
    protected String path;

    @Getter
    @Setter
    protected String token;

    public TransferSession(HostTerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
        this.connectInfo = connectInfo;
        this.sessionStore = sessionStore;
        this.channel = channel;
        this.channelId = channel.getId();
    }

    @Override
    public void init() {
        if (executor == null) {
            // 建立连接
            this.executor = sessionStore.getSftpExecutor(connectInfo.getFileNameCharset());
            executor.connect();
        } else {
            // 检查连接
            if (!this.executor.isConnected()) {
                executor.connect();
            }
        }
    }

    @Override
    public void handleBinary(byte[] bytes) {
    }

    @Override
    public void onStart(TransferOperatorRequest request) {
        this.path = request.getPath();
    }

    @Override
    public void onFinish(TransferOperatorRequest request) {
        log.info("TransferSession.uploadFinish channelId: {}", channelId);
        this.closeStream();
        // 响应结果
        TransferUtils.sendMessage(channel, TransferReceiver.FINISH, null);
    }

    @Override
    public void onError(TransferOperatorRequest request) {
        log.error("TransferSession.uploadError channelId: {}", channelId);
        this.closeStream();
        // 响应结果
        TransferUtils.sendMessage(channel, TransferReceiver.ERROR, new InvalidArgumentException(request.getErrorMessage()));
    }

    @Override
    public void onAbort(TransferOperatorRequest request) {
        log.info("TransferSession.abort channelId: {}, path: {}", channelId, path);
        // 关闭流
        this.closeStream();
        // 响应结果
        TransferUtils.sendMessage(channel, TransferReceiver.ABORT, null);
    }

    /**
     * 关闭流
     */
    protected abstract void closeStream();

    @Override
    public void close() {
        this.closeStream();
        Streams.close(executor);
        Streams.close(sessionStore);
    }

    @Override
    public Long getHostId() {
        return connectInfo.getHostId();
    }

    /**
     * 保存操作日志
     *
     * @param type type
     * @param path path
     */
    protected void saveOperatorLog(String type, String path) {
        // 设置参数
        Map<String, Object> extra = Maps.newMap();
        extra.put(OperatorLogs.PATH, path);
        extra.put(OperatorLogs.HOST_ID, connectInfo.getHostId());
        extra.put(OperatorLogs.HOST_NAME, connectInfo.getHostName());
        extra.put(OperatorLogs.ADDRESS, connectInfo.getHostAddress());
        // 获取日志
        OperatorLogModel model = TerminalUtils.getOperatorLogModel(this.channel, extra, type, System.currentTimeMillis(), null);
        // 保存
        SpringHolder.getBean(OperatorLogFrameworkService.class).insert(model);
    }

}
