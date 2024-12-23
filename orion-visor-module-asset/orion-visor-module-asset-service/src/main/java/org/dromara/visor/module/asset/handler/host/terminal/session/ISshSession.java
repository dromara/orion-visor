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

/**
 * ssh 会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/4 16:47
 */
public interface ISshSession extends ITerminalSession {

    /**
     * 连接
     *
     * @param terminalType terminalType
     * @param cols         cols
     * @param rows         rows
     */
    void connect(String terminalType, int cols, int rows);

    /**
     * 重置大小
     *
     * @param cols cols
     * @param rows rows
     */
    void resize(int cols, int rows);

    /**
     * 写入内容
     *
     * @param b b
     */
    void write(String b);

    /**
     * 写入内容
     *
     * @param b b
     */
    void write(byte[] b);

}
