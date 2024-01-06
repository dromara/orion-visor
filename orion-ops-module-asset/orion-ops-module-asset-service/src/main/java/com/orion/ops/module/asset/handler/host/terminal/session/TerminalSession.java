package com.orion.ops.module.asset.handler.host.terminal.session;

import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.ssh.shell.ShellExecutor;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.enums.HostConnectStatusEnum;
import com.orion.ops.module.asset.handler.host.terminal.constant.TerminalMessage;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalConfig;
import com.orion.ops.module.asset.handler.host.terminal.model.response.TerminalCloseResponse;
import com.orion.ops.module.asset.handler.host.terminal.model.response.TerminalOutputResponse;
import com.orion.ops.module.asset.service.HostConnectLogService;
import com.orion.spring.SpringHolder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 终端会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
@Slf4j
public class TerminalSession implements ITerminalSession {

    @Getter
    private final String sessionId;

    @Getter
    private final WebSocketSession channel;

    private final TerminalConfig config;

    private final SessionStore sessionStore;

    private ShellExecutor executor;

    @Getter
    private String lastLine;

    private volatile boolean close;

    public TerminalSession(String sessionId,
                           WebSocketSession channel,
                           SessionStore sessionStore,
                           TerminalConfig config) {
        this.sessionId = sessionId;
        this.channel = channel;
        this.sessionStore = sessionStore;
        this.config = config;
    }

    @Override
    public void connect(int cols, int rows) {
        config.setCols(cols);
        config.setRows(rows);
        // 打开 shell
        this.executor = sessionStore.getShellExecutor();
        executor.size(cols, rows);
        executor.streamHandler(this::streamHandler);
        executor.callback(this::eofCallback);
        executor.connect();
        // 开始监听输出
        AssetThreadPools.TERMINAL_SCHEDULER.execute(executor);
    }

    @Override
    public void resize(int cols, int rows) {
        if (!executor.isConnected()) {
            executor.connect();
        }
        // 大小发生变化 则修改大小
        if (cols != config.getCols() ||
                rows != config.getRows()) {
            config.setCols(cols);
            config.setRows(rows);
            executor.size(cols, rows);
            executor.resize();
        }
    }

    @Override
    public void write(String b) {
        executor.write(b);
    }

    @Override
    public void write(byte[] b) {
        executor.write(b);
    }

    @Override
    public void keepAlive() {
        try {
            // 发送个信号 保证 socket 不自动关闭
            executor.sendSignal(Const.EMPTY);
        } catch (Exception e) {
            log.error("terminal keep-alive error {}", sessionId, e);
        }
    }

    @Override
    public void close() {
        log.info("terminal close {}", sessionId);
        if (close) {
            return;
        }
        this.close = true;
        // 关闭流
        try {
            Streams.close(executor);
            Streams.close(sessionStore);
        } catch (Exception e) {
            log.error("terminal 断开连接失败 {}", sessionId, e);
        }
        // 修改状态
        SpringHolder.getBean(HostConnectLogService.class).updateStatusByToken(sessionId, HostConnectStatusEnum.COMPLETE);
    }

    /**
     * 标准输出处理
     *
     * @param inputStream stream
     */
    private void streamHandler(InputStream inputStream) {
        byte[] bs = new byte[Const.BUFFER_KB_4];
        BufferedInputStream in = new BufferedInputStream(inputStream, Const.BUFFER_KB_4);
        int read;
        try {
            while (channel.isOpen() && (read = in.read(bs)) != -1) {
                String body = lastLine = new String(bs, 0, read, config.getCharset());
                // 响应
                TerminalOutputResponse resp = TerminalOutputResponse.builder()
                        .type(OutputTypeEnum.OUTPUT.getType())
                        .sessionId(sessionId)
                        .body(body)
                        .build();
                WebSockets.sendText(channel, OutputTypeEnum.OUTPUT.format(resp));
            }
        } catch (IOException ex) {
            log.error("terminal 读取流失败", ex);
        }
    }

    /**
     * eof 回调
     */
    private void eofCallback() {
        log.info("terminal eof回调 {}, forClose: {}", sessionId, this.close);
        // 发送关闭信息
        TerminalCloseResponse resp = TerminalCloseResponse.builder()
                .type(OutputTypeEnum.CLOSE.getType())
                .sessionId(this.sessionId)
                .msg(TerminalMessage.CLOSED_CONNECTION)
                .build();
        WebSockets.sendText(channel, OutputTypeEnum.CLOSE.format(resp));
        // 需要调用关闭 - 可能是 logout 需要手动触发
        this.close();
    }

}
