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
package org.dromara.visor.module.asset.handler.host.transfer.session;

import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.spring.SpringHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.module.asset.entity.dto.TerminalConnectDTO;
import org.dromara.visor.module.asset.handler.host.terminal.utils.TerminalUtils;
import org.dromara.visor.module.asset.handler.host.transfer.enums.TransferReceiver;
import org.dromara.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import org.dromara.visor.module.asset.handler.host.transfer.utils.TransferUtils;
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

    protected final TerminalConnectDTO connectInfo;

    protected final SessionStore sessionStore;

    protected final WebSocketSession channel;

    protected SftpExecutor executor;

    protected String channelId;

    @Getter
    protected String path;

    @Getter
    @Setter
    protected String token;

    public TransferSession(TerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
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
