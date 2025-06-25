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

import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.module.common.utils.SftpUtils;
import org.dromara.visor.module.terminal.define.operator.TerminalOperatorType;
import org.dromara.visor.module.terminal.handler.transfer.enums.TransferReceiver;
import org.dromara.visor.module.terminal.handler.transfer.model.TransferOperatorRequest;
import org.dromara.visor.module.terminal.handler.transfer.utils.TransferUtils;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 上传会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:04
 */
@Slf4j
public class UploadSession extends TransferSession {

    protected OutputStream outputStream;

    public UploadSession(SshConnectConfig connectConfig, SessionStore sessionStore, WebSocketSession channel) {
        super(connectConfig, sessionStore, channel);
    }

    @Override
    public void onStart(TransferOperatorRequest request) {
        super.onStart(request);
        try {
            log.info("UploadSession.startUpload start channelId: {}, path: {}", channelId, path);
            // 保存操作日志
            this.saveOperatorLog(TerminalOperatorType.SFTP_UPLOAD, path);
            // 检查连接
            this.init();
            // 检查文件是否存在
            SftpUtils.checkUploadFilePresent(executor, path);
            // 打开输出流
            this.outputStream = executor.openOutputStream(path);
            // 响应结果
            TransferUtils.sendMessage(channel, TransferReceiver.NEXT_PART, null);
            log.info("UploadSession.startUpload transfer channelId: {}, path: {}", channelId, path);
        } catch (Exception e) {
            log.error("UploadSession.startUpload error channelId: {}, path: {}", channelId, path, e);
            this.closeStream();
            // 响应结果
            TransferUtils.sendMessage(channel, TransferReceiver.ERROR, e);
        }
    }

    @Override
    public void handleBinary(byte[] bytes) {
        try {
            // 写入内容
            outputStream.write(bytes);
            // 响应结果
            TransferUtils.sendMessage(channel, TransferReceiver.NEXT_PART, null);
        } catch (IOException e) {
            log.error("UploadSession.handleBinary error channelId: {}", channel.getId(), e);
            this.closeStream();
            // 响应结果
            TransferUtils.sendMessage(channel, TransferReceiver.ERROR, e);
        }
    }

    @Override
    protected void closeStream() {
        if (this.outputStream != null) {
            // 关闭流
            Streams.close(outputStream);
            this.outputStream = null;
        }
    }

}
