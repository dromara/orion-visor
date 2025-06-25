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

import cn.orionsec.kit.lang.able.SafeCloseable;

/**
 * 消息发送器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/31 21:55
 */
public interface ITerminalSender extends SafeCloseable {

    /**
     * 发送消息
     *
     * @param body body
     */
    default void sendMessage(String body) {
    }

    /**
     * 发送设置id
     *
     * @param sessionId sessionId
     */
    default void sendSetId(String sessionId) {
    }

    /**
     * 发送设置信息
     *
     * @param info info
     */
    default void sendSetInfo(String info) {
    }

    /**
     * 发送 修改大小
     *
     * @param width  width
     * @param height height
     */
    default void sendResize(int width, int height) {
    }

    /**
     * 发送 pong
     */
    default void sendPong() {
    }

    /**
     * 发送已连接
     */
    default void sendConnected() {
    }

    /**
     * 发送关闭结果
     *
     * @param code    code
     * @param message message
     */
    default void sendClosed(int code, String message) {
    }

    /**
     * 设置为已关闭
     */
    default void setClosed() {
    }

    /**
     * 是否为打开状态
     *
     * @return isOpen
     */
    default boolean isOpen() {
        return false;
    }

}
