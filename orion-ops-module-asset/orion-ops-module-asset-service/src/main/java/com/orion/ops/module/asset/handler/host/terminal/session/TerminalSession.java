package com.orion.ops.module.asset.handler.host.terminal.session;

import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.ssh.TerminalType;
import com.orion.net.host.ssh.shell.ShellExecutor;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.define.AssetThreadPools;
import com.orion.ops.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.ops.module.asset.handler.host.terminal.model.TerminalConfig;
import com.orion.ops.module.asset.handler.host.terminal.model.response.TerminalOutputResponse;
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
    private final String token;

    @Getter
    private final WebSocketSession session;

    private final TerminalConfig config;

    private final SessionStore sessionStore;

    private ShellExecutor executor;

    @Getter
    private String lastLine;

    private volatile boolean close;

    public TerminalSession(String token,
                           WebSocketSession session,
                           SessionStore sessionStore,
                           TerminalConfig config) {
        this.token = token;
        this.session = session;
        this.sessionStore = sessionStore;
        this.config = config;
    }

    @Override
    public void connect(int cols, int rows) {
        config.setCols(cols);
        config.setRows(rows);
        // 打开 shell
        this.executor = sessionStore.getShellExecutor();
        executor.terminalType(TerminalType.XTERM_256_COLOR);
        executor.size(cols, rows);
        executor.streamHandler(this::streamHandler);
        executor.connect();
        // 开始监听输出
        AssetThreadPools.TERMINAL_SCHEDULER.execute(executor);
    }

    @Override
    public void resize(int cols, int rows) {
        if (!executor.isConnected()) {
            executor.connect();
        }
        config.setCols(cols);
        config.setRows(rows);
        executor.size(cols, rows);
        executor.resize();
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
    public void close() {
        if (close) {
            return;
        }
        this.close = true;
        try {
            Streams.close(executor);
            Streams.close(sessionStore);
        } catch (Exception e) {
            log.error("terminal 断开连接 失败 token: {}", token, e);
        }
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
            while (session.isOpen() && (read = in.read(bs)) != -1) {
                String body = lastLine = new String(bs, 0, read, config.getCharset());
                // 响应
                TerminalOutputResponse resp = TerminalOutputResponse.builder()
                        .session(token)
                        .type(OutputTypeEnum.OUTPUT.getType())
                        .body(body)
                        .build();
                WebSockets.sendText(session, OutputTypeEnum.OUTPUT.format(resp));
            }
        } catch (IOException ex) {
            log.error("terminal 读取流失败", ex);
        }
        // eof
        if (close) {
            return;
        }
        log.info("terminal eof回调 {}", token);
    }

}
