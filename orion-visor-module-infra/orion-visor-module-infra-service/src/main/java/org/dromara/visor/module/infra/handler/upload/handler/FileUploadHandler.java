/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.module.infra.handler.upload.handler;

import cn.orionsec.kit.lang.utils.io.Streams;
import com.alibaba.fastjson.JSON;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.common.file.FileClient;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.infra.handler.upload.enums.FileUploadReceiverType;
import org.dromara.visor.module.infra.handler.upload.model.FileUploadResponse;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 文件上传处理器实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 15:49
 */
public class FileUploadHandler implements IFileUploadHandler {

    private final WebSocketSession channel;

    private final FileClient fileClient;

    private final String endpoint;

    private String fileId;

    private String filePath;

    private OutputStream outputStream;

    private boolean closed;

    public FileUploadHandler(WebSocketSession channel, FileClient fileClient, String endpoint) {
        this.channel = channel;
        this.fileClient = fileClient;
        this.endpoint = endpoint;
    }

    @Override
    public void start(String fileId) {
        // 释放资源
        this.close();
        // 获取返回路径
        this.fileId = fileId;
        this.filePath = fileClient.getReturnPath(endpoint + Const.SLASH + fileId);
        try {
            // 打开文件流
            this.outputStream = fileClient.getContentOutputStream(filePath);
            this.closed = false;
            // 请求下一块数据
            FileUploadResponse resp = FileUploadResponse.builder()
                    .type(FileUploadReceiverType.NEXT.getType())
                    .fileId(this.fileId)
                    .build();
            this.send(resp);
        } catch (Exception e) {
            // 释放资源
            this.close();
            // 返回错误
            FileUploadResponse resp = FileUploadResponse.builder()
                    .type(FileUploadReceiverType.ERROR.getType())
                    .fileId(this.fileId)
                    .build();
            this.send(resp);
        }
    }

    @Override
    public void write(byte[] content) {
        try {
            // 写入内容
            this.outputStream.write(content);
            // 请求下一块数据
            FileUploadResponse resp = FileUploadResponse.builder()
                    .type(FileUploadReceiverType.NEXT.getType())
                    .fileId(this.fileId)
                    .build();
            this.send(resp);
        } catch (IOException e) {
            // 释放资源
            this.close();
            // 返回错误
            FileUploadResponse resp = FileUploadResponse.builder()
                    .type(FileUploadReceiverType.ERROR.getType())
                    .fileId(this.fileId)
                    .build();
            this.send(resp);
        }
    }

    @Override
    public void finish() {
        // 释放资源
        this.close();
        // 返回上传路径
        FileUploadResponse resp = FileUploadResponse.builder()
                .type(FileUploadReceiverType.FINISH.getType())
                .fileId(this.fileId)
                .path(this.filePath)
                .build();
        this.send(resp);
    }

    @Override
    public void error() {
        // 释放资源
        this.close();
        // 返回上传路径
        FileUploadResponse resp = FileUploadResponse.builder()
                .type(FileUploadReceiverType.ERROR.getType())
                .fileId(this.fileId)
                .build();
        this.send(resp);
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        this.closed = true;
        Streams.close(outputStream);
    }

    /**
     * 发送消息
     *
     * @param resp resp
     */
    private void send(FileUploadResponse resp) {
        WebSockets.sendText(channel, JSON.toJSONString(resp));
    }

}
