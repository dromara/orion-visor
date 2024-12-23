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
package org.dromara.visor.module.asset.handler.host.exec.command.handler;

import cn.orionsec.kit.lang.able.SafeCloseable;
import org.dromara.visor.module.asset.enums.ExecHostStatusEnum;

/**
 * 命令执行器定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/12 11:31
 */
public interface IExecCommandHandler extends Runnable, SafeCloseable {

    /**
     * 写入
     *
     * @param msg msg
     */
    void write(String msg);

    /**
     * 中断执行
     */
    void interrupt();

    /**
     * 获取当前状态
     *
     * @return status
     */
    ExecHostStatusEnum getStatus();

    /**
     * 获取退出码
     *
     * @return exit code
     */
    Integer getExitCode();

    /**
     * 获取主机 id
     *
     * @return hostId
     */
    Long getHostId();

}
