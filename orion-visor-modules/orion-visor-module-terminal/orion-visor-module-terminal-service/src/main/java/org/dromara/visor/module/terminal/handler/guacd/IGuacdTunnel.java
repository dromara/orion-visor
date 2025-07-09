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

import cn.orionsec.kit.lang.able.Executable;
import cn.orionsec.kit.lang.able.SafeCloseable;
import cn.orionsec.kit.lang.support.Attempt;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.io.GuacamoleReader;
import org.apache.guacamole.protocol.GuacamoleClientInformation;
import org.apache.guacamole.protocol.GuacamoleConfiguration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * guacd tunnel 定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/31 0:51
 */
public interface IGuacdTunnel extends Runnable, Executable, SafeCloseable {

    /**
     * 设置主机地址
     *
     * @param address address
     * @param port    port
     */
    void remote(String address, int port);

    /**
     * 设置远程连接认证
     *
     * @param username username
     * @param password password
     */
    void auth(String username, String password);

    /**
     * 大小
     *
     * @param width  width
     * @param height height
     */
    void size(int width, int height);

    /**
     * dpi
     *
     * @param dpi dpi
     */
    void dpi(int dpi);

    /**
     * 设置时区
     *
     * @param timezone 时区
     */
    void timezone(String timezone);

    /**
     * 设置参数
     *
     * @param key   key
     * @param value value
     */
    void setParameter(String key, Object value);

    /**
     * 设置参数
     *
     * @param params params
     */
    void setParameter(Map<String, Object> params);

    /**
     * 设置音频类型
     *
     * @param mimeTypes mimeTypes
     */
    void setAudioMimeTypes(List<String> mimeTypes);

    /**
     * 设置视频类型
     *
     * @param mimeTypes mimeTypes
     */
    void setVideoMimeTypes(List<String> mimeTypes);

    /**
     * 设置图像类型
     *
     * @param mimeTypes mimeTypes
     */
    void setImageMimeTypes(List<String> mimeTypes);

    /**
     * 设置连接的id
     *
     * @param connectionId connectionId
     */
    void setConnectionId(String connectionId);

    /**
     * 设置回调
     *
     * @param callback callback
     */
    void callback(Runnable callback);

    /**
     * 设置传输
     */
    void transfer(Consumer<String> out);

    /**
     * 设置 streamHandler
     *
     * @param streamHandler streamHandler
     */
    void streamHandler(Attempt.UncheckedConsumer<GuacamoleReader, GuacamoleException> streamHandler);

    /**
     * 连接
     *
     * @throws GuacdException GuacdCodeException
     */
    void connect() throws GuacdException;

    /**
     * 输入命令
     *
     * @param data data
     * @throws GuacdException GuacdCodeException
     */
    void write(String data) throws GuacdException;

    /**
     * 输入命令
     *
     * @param data data
     * @throws GuacdException GuacdCodeException
     */
    void write(byte[] data) throws GuacdException;

    /**
     * 输入命令
     *
     * @param data data
     * @throws GuacdException GuacdCodeException
     */
    void write(char[] data) throws GuacdException;

    /**
     * 写入指令
     *
     * @param opcode opcode
     * @param args   args
     * @throws GuacdException GuacdCodeException
     */
    void writeInstruction(String opcode, String... args) throws GuacdException;

    /**
     * 写入指令
     *
     * @param opcode opcode
     * @param args   args
     * @throws GuacdException GuacdCodeException
     */
    void writeInstruction(String opcode, List<String> args) throws GuacdException;

    /**
     * 获取 connectionId
     *
     * @return connectionId
     */
    String getConnectionId();

    /**
     * 获取服务端配置
     *
     * @return config
     */
    GuacamoleConfiguration getServerConfig();

    /**
     * 获取客户端配置
     *
     * @return config
     */
    GuacamoleClientInformation getClientConfig();

    /**
     * 是否开启会话
     *
     * @return open
     */
    boolean isOpen();

}
