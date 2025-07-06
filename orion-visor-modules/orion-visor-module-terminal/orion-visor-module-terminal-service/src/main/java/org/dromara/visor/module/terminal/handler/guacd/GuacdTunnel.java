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
package org.dromara.visor.module.terminal.handler.guacd;

import cn.orionsec.kit.lang.support.Attempt;
import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.io.Streams;
import lombok.Getter;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.io.GuacamoleReader;
import org.apache.guacamole.io.GuacamoleWriter;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.GuacamoleTunnel;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.protocol.*;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.module.terminal.handler.guacd.constant.GuacdConst;
import org.dromara.visor.module.terminal.handler.guacd.constant.GuacdProtocol;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * guacd tunnel
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/31 0:20
 */
public class GuacdTunnel implements IGuacdTunnel {

    private final String uuid;

    private final String serverAddress;

    private final int serverPort;

    @Getter
    private final GuacamoleConfiguration serverConfig;

    @Getter
    private final GuacamoleClientInformation clientConfig;

    @Getter
    private GuacamoleSocket socket;

    @Getter
    private GuacamoleTunnel tunnel;

    private Runnable callback;

    private volatile boolean closed;

    private Attempt.UncheckedConsumer<GuacamoleReader, GuacamoleException> streamHandler;

    public GuacdTunnel(GuacdProtocol protocol, String uuid, String serverAddress, int serverPort) {
        this.uuid = uuid;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.serverConfig = new GuacamoleConfiguration();
        this.clientConfig = new GuacamoleClientInformation();
        serverConfig.setProtocol(protocol.getValue());
        clientConfig.setName(uuid);
    }

    @Override
    public void connect() throws GuacdException {
        try {
            // TODO 端口转发
            this.socket = new ConfiguredGuacamoleSocket(new InetGuacamoleSocket(serverAddress, serverPort), serverConfig, clientConfig);
            this.tunnel = new CustomGuacamoleTunnel(uuid, socket);
        } catch (GuacamoleException e) {
            throw new GuacdException("connect guacd tunnel error", e);
        }
    }

    @Override
    public void run() {
        this.exec();
    }

    @Override
    public void exec() throws GuacdException {
        Valid.notNull(socket, "server is null");
        Valid.notNull(streamHandler, "streamHandler is null");
        // 读取
        try {
            GuacamoleReader reader = tunnel.acquireReader();
            streamHandler.accept(reader);
        } catch (GuacamoleException e) {
            throw new GuacdException(e);
        } finally {
            // 释放
            tunnel.releaseReader();
        }
        // 回调
        if (callback != null) {
            try {
                callback.run();
            } catch (Exception e) {
                // ignored
            }
        }
        // 释放资源
        Streams.close(this);
    }

    @Override
    public void write(String data) throws GuacdException {
        this.write(data.toCharArray());
    }

    @Override
    public void write(byte[] data) throws GuacdException {
        this.write(new String(data));
    }

    @Override
    public void write(char[] data) throws GuacdException {
        this.write(s -> s.write(data));
    }

    @Override
    public void writeInstruction(String opcode, String... args) throws GuacdException {
        this.write(s -> s.writeInstruction(new GuacamoleInstruction(opcode, args)));
    }

    @Override
    public void writeInstruction(String opcode, List<String> args) throws GuacdException {
        this.write(s -> s.writeInstruction(new GuacamoleInstruction(opcode, args)));
    }

    /**
     * 写入
     *
     * @param writer writer
     * @throws GuacdException GuacdCodeException
     */
    private void write(Attempt.UncheckedConsumer<GuacamoleWriter, GuacamoleException> writer) throws GuacdException {
        try {
            // 创建过滤写入器
            GuacamoleWriter write = new FilteredGuacamoleWriter(tunnel.acquireWriter(), s -> {
                // 过滤交互指令
                if (GuacamoleTunnel.INTERNAL_DATA_OPCODE.equals(s.getOpcode())) {
                    return null;
                }
                return s;
            });
            // 写入
            writer.accept(write);
        } catch (GuacamoleException e) {
            throw new GuacdException(e);
        } finally {
            // 释放
            tunnel.releaseWriter();
        }
    }

    @Override
    public void remote(String address, int port) {
        this.setParameter(GuacdConst.HOSTNAME, address);
        this.setParameter(GuacdConst.PORT, port);
    }

    @Override
    public void auth(String username, String password) {
        this.setParameter(GuacdConst.USERNAME, username);
        this.setParameter(GuacdConst.PASSWORD, password);
    }

    @Override
    public void size(int width, int height) {
        clientConfig.setOptimalScreenWidth(width);
        clientConfig.setOptimalScreenHeight(height);
        this.setParameter(GuacdConst.WIDTH, width);
        this.setParameter(GuacdConst.HEIGHT, height);
    }

    @Override
    public void dpi(int dpi) {
        clientConfig.setOptimalResolution(dpi);
        this.setParameter(GuacdConst.DPI, dpi);
    }

    @Override
    public void timezone(String timezone) {
        clientConfig.setTimezone(timezone);
        this.setParameter(GuacdConst.TIMEZONE, timezone);
    }

    @Override
    public void setParameter(String key, Object value) {
        if (value != null) {
            serverConfig.setParameter(key, Objects1.toString(value));
        } else {
            serverConfig.unsetParameter(key);
        }
    }

    @Override
    public void setParameter(Map<String, Object> params) {
        params.forEach(this::setParameter);
    }

    @Override
    public void setAudioMimeTypes(List<String> mimeTypes) {
        if (Lists.isEmpty(mimeTypes)) {
            return;
        }
        clientConfig.getAudioMimetypes().addAll(mimeTypes);
    }

    @Override
    public void setVideoMimeTypes(List<String> mimeTypes) {
        if (Lists.isEmpty(mimeTypes)) {
            return;
        }
        clientConfig.getVideoMimetypes().addAll(mimeTypes);
    }

    @Override
    public void setImageMimeTypes(List<String> mimeTypes) {
        if (Lists.isEmpty(mimeTypes)) {
            return;
        }
        clientConfig.getImageMimetypes().addAll(mimeTypes);
    }

    @Override
    public void setConnectionId(String connectionId) {
        serverConfig.setConnectionID(connectionId);
    }

    @Override
    public void callback(Runnable callback) {
        this.callback = callback;
    }

    @Override
    public void transfer(Consumer<String> out) {
        this.streamHandler = (reader) -> {
            // 读取消息
            StringBuilder buffer = new StringBuilder(Const.BUFFER_KB_8);
            char[] readMessage;
            while ((readMessage = reader.read()) != null && !closed) {
                // 插入消息
                buffer.append(readMessage);
                // 响应消息
                if (!reader.available() || buffer.length() >= Const.BUFFER_KB_8) {
                    out.accept(buffer.toString());
                    buffer.setLength(0);
                }
            }
        };
    }

    @Override
    public void streamHandler(Attempt.UncheckedConsumer<GuacamoleReader, GuacamoleException> streamHandler) {
        this.streamHandler = streamHandler;
    }

    @Override
    public String getConnectionId() {
        return serverConfig.getConnectionID();
    }

    @Override
    public boolean isOpen() {
        if (tunnel != null) {
            return tunnel.isOpen();
        }
        return false;
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        this.closed = true;
        if (tunnel != null) {
            try {
                tunnel.close();
            } catch (Exception e) {
                // ignored
            }
        }
    }

}
