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

import org.dromara.visor.module.terminal.define.TerminalThreadPools;
import org.dromara.visor.module.terminal.handler.guacd.IGuacdTunnel;
import org.dromara.visor.module.terminal.handler.terminal.constant.TerminalMessage;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.handler.terminal.model.config.ITerminalSessionConfig;
import org.dromara.visor.module.terminal.handler.terminal.sender.IGuacdTerminalSender;
import cn.orionsec.kit.lang.utils.Exceptions;

/**
 * guacd 会话基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/30 17:44
 */
public abstract class AbstractGuacdSession<C extends ITerminalSessionConfig>
        extends AbstractTerminalSession<IGuacdTerminalSender, C>
        implements IGuacdSession {

    protected IGuacdTunnel tunnel;

    public AbstractGuacdSession(TerminalChannelProps props,
                                IGuacdTerminalSender sender,
                                C config) {
        super(props, sender, config);
    }

    @Override
    public void connect() {
        // 创建 tunnel
        this.tunnel = this.createTunnel();
        // 设置 tunnel 参数
        this.setTunnelParams();
        try {
            // 执行连接
            this.doConnect();
        } catch (Exception e) {
            throw Exceptions.app(TerminalMessage.GUACD_SERVICE, e);
        }
    }

    /**
     * 创建 tunnel
     *
     * @return tunnel
     */
    protected abstract IGuacdTunnel createTunnel();

    /**
     * 设置 tunnel 参数
     */
    protected abstract void setTunnelParams();

    /**
     * 执行连接
     */
    protected void doConnect() {
        // 监听方式
        tunnel.transfer(sender::sendInstruction);
        // 连接
        tunnel.connect();
        // 开始监听输出
        TerminalThreadPools.TERMINAL_STDOUT.execute(tunnel);
    }

    @Override
    public void write(String data) {
        tunnel.write(data);
    }

    @Override
    public void keepAlive() {
        // guacd 有内部实现
    }

    @Override
    protected void releaseResource() {
        tunnel.close();
    }

}
