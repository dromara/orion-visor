/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.handler.host.transfer.session;

import com.orion.lang.define.wrapper.Ref;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpFile;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.module.asset.define.AssetThreadPools;
import com.orion.visor.module.asset.define.operator.TerminalOperatorType;
import com.orion.visor.module.asset.entity.dto.TerminalConnectDTO;
import com.orion.visor.module.asset.handler.host.transfer.enums.TransferReceiver;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import com.orion.visor.module.asset.handler.host.transfer.utils.TransferUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.socket.WebSocketSession;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 下载会话实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:25
 */
@Slf4j
public class DownloadSession extends TransferSession implements StreamingResponseBody {

    private static final int BUFFER_SIZE = Const.BUFFER_KB_32;

    private static final int FLUSH_COUNT = Const.BUFFER_KB_1 * Const.BUFFER_KB_1 / Const.BUFFER_KB_32;

    protected InputStream inputStream;

    private Long fileSize;

    public DownloadSession(TerminalConnectDTO connectInfo, SessionStore sessionStore, WebSocketSession channel) {
        super(connectInfo, sessionStore, channel);
        this.fileSize = 0L;
    }

    @Override
    public void onStart(TransferOperatorRequest request) {
        try {
            super.onStart(request);
            log.info("DownloadSession.startDownload open start channelId: {}, path: {}", channelId, path);
            // 保存操作日志
            this.saveOperatorLog(TerminalOperatorType.SFTP_DOWNLOAD, path);
            // 检查连接
            this.init();
            // 检查文件是否存在
            SftpFile file = executor.getFile(path);
            Valid.notNull(file, ErrorMessage.FILE_ABSENT);
            // 验证非文件夹
            Valid.isTrue(!file.isDirectory(), ErrorMessage.UNABLE_DOWNLOAD_FOLDER);
            if ((this.fileSize = file.getSize()) == 0L) {
                // 文件为空
                log.info("DownloadSession.startDownload file empty channelId: {}, path: {}", channelId, path);
                TransferUtils.sendMessage(channel, TransferReceiver.FINISH, null);
                return;
            }
            // 打开输入流
            this.inputStream = executor.openInputStream(path);
            // 响应开始下载
            TransferUtils.sendMessage(channel, TransferReceiver.START, null, e -> {
                e.setChannelId(channelId);
                e.setTransferToken(token);
            });
            log.info("DownloadSession.startDownload open success channelId: {}, path: {}", channelId, path);
        } catch (Exception e) {
            log.error("DownloadSession.startDownload open error channelId: {}, path: {}", channelId, path, e);
            // 响应下载失败
            TransferUtils.sendMessage(channel, TransferReceiver.ERROR, e);
        }
    }

    @Override
    public void onAbort(TransferOperatorRequest request) {
        log.info("TransferSession.abort channelId: {}, path: {}", channelId, path);
        // 关闭流
        this.closeStream();
        // download 的 abort 无需发送回调
    }

    @Override
    public void writeTo(OutputStream outputStream) {
        Ref<Exception> ex = new Ref<>();
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            int i = 0;
            long size = 0;
            // 响应文件内容
            while (this.inputStream != null && (len = this.inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                size += len;
                // 不要每次都 flush 和 send > 1mb
                if (i == FLUSH_COUNT) {
                    i = 0;
                }
                // 首次触发
                if (i == 0) {
                    outputStream.flush();
                    this.sendProgress(size, fileSize);
                }
                i++;
            }
            // 最后一次也要 flush
            if (i != 0) {
                outputStream.flush();
                this.sendProgress(size, fileSize);
            }
            log.info("DownloadSession.download finish channelId: {}, path: {}", channelId, path);
        } catch (Exception e) {
            log.error("DownloadSession.download error channelId: {}, path: {}", channelId, path, e);
            ex.set(e);
        }
        // 传输结束 异步处理
        AssetThreadPools.TERMINAL_OPERATOR.execute(() -> this.onTransferFinish(ex.getValue()));
    }

    /**
     * 发送进度
     *
     * @param currentSize currentSize
     * @param totalSize   totalSize
     */
    protected void sendProgress(Long currentSize, Long totalSize) {
        // send
        TransferUtils.sendMessage(channel, TransferReceiver.PROGRESS, null, e -> {
            e.setCurrentSize(currentSize);
            e.setTotalSize(totalSize);
        });
    }

    /**
     * 传输完成时候触发
     *
     * @param e e
     */
    protected void onTransferFinish(Exception e) {
        // 关闭等待 jsch 内部处理
        Threads.sleep(100);
        this.closeStream();
        Threads.sleep(100);
        // 发送消息
        if (e == null) {
            TransferUtils.sendMessage(channel, TransferReceiver.FINISH, null);
        } else {
            TransferUtils.sendMessage(channel, TransferReceiver.ERROR, e);
        }
    }

    @Override
    protected void closeStream() {
        // 关闭 inputStream 可能会被阻塞 ???...??? 只能关闭 executor
        Streams.close(this.executor);
        this.executor = null;
        this.inputStream = null;
    }

}
