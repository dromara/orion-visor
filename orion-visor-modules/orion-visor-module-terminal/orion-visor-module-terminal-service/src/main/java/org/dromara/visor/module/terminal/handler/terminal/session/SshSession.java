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
package org.dromara.visor.module.terminal.handler.terminal.session;

import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.ssh.shell.ShellExecutor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.terminal.define.TerminalThreadPools;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionSshConfig;
import org.dromara.visor.module.terminal.handler.terminal.sender.ISshTerminalSender;

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
public class SshSession extends AbstractTerminalSession<ISshTerminalSender, TerminalSessionSshConfig> implements ISshSession {

    private final SessionStore sessionStore;

    private ShellExecutor executor;

    public SshSession(TerminalChannelProps props,
                      ISshTerminalSender sender,
                      TerminalSessionSshConfig config,
                      SessionStore sessionStore) {
        super(props, sender, config);
        this.sessionStore = sessionStore;
    }

    @Override
    public void connect() {
        String terminalType = config.getTerminalType();
        Integer width = config.getWidth();
        Integer height = config.getHeight();
        config.setWidth(width);
        config.setHeight(height);
        // 打开 shell
        this.executor = sessionStore.getShellExecutor();
        executor.size(width, height);
        executor.terminalType(terminalType);
        executor.streamHandler(this::streamHandler);
        executor.callback(this::close);
        // 连接会话
        executor.connect();
        this.connected = true;
        // 开始监听输出
        TerminalThreadPools.TERMINAL_STDOUT.execute(executor);
    }

    /**
     * 标准输出处理
     *
     * @param inputStream stream
     */
    private void streamHandler(InputStream inputStream) {
        int bufferSize = Const.BUFFER_KB_4;
        byte[] bs = new byte[bufferSize];
        BufferedInputStream in = new BufferedInputStream(inputStream, bufferSize);
        int read;
        try {
            while (sender.isOpen() && (read = in.read(bs)) != -1) {
                // 响应
                this.sendOutput(new String(bs, 0, read, config.getCharset()));
            }
        } catch (IOException ex) {
            log.error("terminal 读取流失败", ex);
        }
    }

    @Override
    public void resize(int width, int height) {
        // 大小发生变化 则修改大小
        if (width != config.getWidth() || height != config.getHeight()) {
            config.setWidth(width);
            config.setHeight(height);
            // 设置会话大小
            executor.size(width, height);
            executor.resize();
        }
    }

    @Override
    public void write(String in) {
        executor.write(in);
    }

    @Override
    public void sendOutput(String body) {
        // 响应会话
        sender.sendOutput(body);
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
        Streams.close(sender);
    }

}
