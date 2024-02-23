package com.orion.ops.module.asset.handler.host.transfer.session;

import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.handler.host.terminal.utils.TerminalUtils;
import com.orion.spring.SpringHolder;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * 主机传输会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 21:12
 */
public abstract class TransferHostSession implements ITransferHostSession {

    protected final HostTerminalConnectDTO connectInfo;

    protected final SessionStore sessionStore;

    protected final WebSocketSession channel;

    protected SftpExecutor executor;

    public TransferHostSession(HostTerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
        this.connectInfo = connectInfo;
        this.sessionStore = sessionStore;
        this.channel = channel;
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

}
