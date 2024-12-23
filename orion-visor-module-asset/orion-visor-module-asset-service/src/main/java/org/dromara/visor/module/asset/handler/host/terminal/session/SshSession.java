/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.handler.host.terminal.session;

import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.ssh.shell.ShellExecutor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.Const;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.asset.define.AssetThreadPools;
import org.dromara.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.model.TerminalConfig;
import org.dromara.visor.module.asset.handler.host.terminal.model.response.SshOutputResponse;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 终端 ssh 会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/2 17:28
 */
@Slf4j
public class SshSession extends TerminalSession implements ISshSession {

    private final SessionStore sessionStore;

    private ShellExecutor executor;

    @Getter
    private String lastLine;

    public SshSession(String sessionId,
                      WebSocketSession channel,
                      SessionStore sessionStore,
                      TerminalConfig config) {
        super(sessionId, channel, config);
        this.sessionStore = sessionStore;
    }

    @Override
    public void connect(String terminalType, int cols, int rows) {
        config.setCols(cols);
        config.setRows(rows);
        // 打开 shell
        this.executor = sessionStore.getShellExecutor();
        executor.size(cols, rows);
        executor.terminalType(terminalType);
        executor.streamHandler(this::streamHandler);
        executor.callback(this::close);
        executor.connect();
        // 开始监听输出
        AssetThreadPools.TERMINAL_STDOUT.execute(executor);
    }

    @Override
    public void resize(int cols, int rows) {
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
            log.error("ssh keep-alive error {}", sessionId, e);
        }
    }

    @Override
    protected void releaseResource() {
        Streams.close(executor);
        Streams.close(sessionStore);
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
                SshOutputResponse resp = SshOutputResponse.builder()
                        .type(OutputTypeEnum.SSH_OUTPUT.getType())
                        .sessionId(sessionId)
                        .body(body)
                        .build();
                WebSockets.sendText(channel, OutputTypeEnum.SSH_OUTPUT.format(resp));
            }
        } catch (IOException ex) {
            log.error("terminal 读取流失败", ex);
        }
    }

}
