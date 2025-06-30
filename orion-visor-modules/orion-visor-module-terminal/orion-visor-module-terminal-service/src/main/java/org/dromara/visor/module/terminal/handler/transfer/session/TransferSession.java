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
package org.dromara.visor.module.terminal.handler.transfer.session;

import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.FieldConst;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.terminal.handler.terminal.record.TerminalAsyncSaver;
import org.dromara.visor.module.terminal.handler.transfer.enums.TransferReceiver;
import org.dromara.visor.module.terminal.handler.transfer.model.TransferOperatorRequest;
import org.dromara.visor.module.terminal.handler.transfer.utils.TransferUtils;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

/**
 * 主机传输会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 21:12
 */
@Slf4j
public abstract class TransferSession implements ITransferSession {

    protected final SshConnectConfig connectConfig;

    protected final SessionStore sessionStore;

    protected final WebSocketSession channel;

    protected SftpExecutor executor;

    protected String channelId;

    @Getter
    protected String path;

    @Getter
    @Setter
    protected String token;

    public TransferSession(SshConnectConfig connectConfig, SessionStore sessionStore, WebSocketSession channel) {
        this.connectConfig = connectConfig;
        this.sessionStore = sessionStore;
        this.channel = channel;
        this.channelId = channel.getId();
    }

    @Override
    public void init() {
        if (executor == null) {
            // 建立连接
            this.executor = sessionStore.getSftpExecutor(connectConfig.getFileNameCharset());
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
        return connectConfig.getHostId();
    }

    /**
     * 保存操作日志
     *
     * @param logId logId
     * @param type  type
     * @param paths paths
     */
    protected void saveOperatorLog(Long logId, String type, List<String> paths) {
        String path = String.join(Const.LF, paths);
        int count = paths.size();
        // 获取操作日志
        OperatorLogModel model = TransferUtils.getOperatorLogModel(type, path, count, connectConfig, WebSockets.getAttr(channel, FieldConst.PROPS));
        // 保存操作日志
        TerminalAsyncSaver.saveOperatorLog(model);
    }

}
