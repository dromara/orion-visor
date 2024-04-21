package com.orion.ops.module.asset.handler.host.transfer.session;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Booleans;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.net.host.sftp.SftpFile;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.module.asset.define.config.AppSftpConfig;
import com.orion.ops.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.ops.module.asset.handler.host.terminal.utils.TerminalUtils;
import com.orion.ops.module.asset.handler.host.transfer.model.SftpFileBackupParams;
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

    protected static final AppSftpConfig SFTP_CONFIG = SpringHolder.getBean(AppSftpConfig.class);

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
     * 检查文件是否存在 并且执行响应策略
     *
     * @param path path
     */
    protected void doCheckFilePresent(String path) {
        // 重复不备份
        if (!Booleans.isTrue(SFTP_CONFIG.getUploadPresentBackup())) {
            return;
        }
        // 检查文件是否存在
        SftpFile file = executor.getFile(path);
        if (file != null) {
            // 文件存在则备份
            SftpFileBackupParams backupParams = new SftpFileBackupParams(file.getName(), System.currentTimeMillis());
            String target = Strings.format(SFTP_CONFIG.getBackupFileName(), JSON.parseObject(JSON.toJSONString(backupParams)));
            // 移动
            executor.move(path, target);
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
