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
package org.dromara.visor.module.terminal.handler.terminal.sender;

import org.apache.guacamole.protocol.GuacamoleInstruction;

/**
 * guacd 终端发送器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/4/1 10:36
 */
public interface IGuacdTerminalSender extends ITerminalSender {

    /**
     * 发送指令
     *
     * @param instruction instruction
     */
    default void sendInstruction(String instruction) {
    }

    /**
     * 发送指令
     *
     * @param instruction instruction
     */
    default void sendInstruction(GuacamoleInstruction instruction) {
    }

}
