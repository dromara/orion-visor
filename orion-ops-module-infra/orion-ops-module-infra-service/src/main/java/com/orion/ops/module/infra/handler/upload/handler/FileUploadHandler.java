package com.orion.ops.module.infra.handler.upload.handler;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.io.Streams;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.file.FileClient;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.infra.handler.upload.enums.FileUploadReceiverType;
import com.orion.ops.module.infra.handler.upload.model.FileUploadResponse;
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
