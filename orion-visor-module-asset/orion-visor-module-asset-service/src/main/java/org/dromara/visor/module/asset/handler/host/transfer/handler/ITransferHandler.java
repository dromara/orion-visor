/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.module.asset.handler.host.transfer.handler;

import cn.orionsec.kit.lang.able.SafeCloseable;
import org.dromara.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import org.dromara.visor.module.asset.handler.host.transfer.session.ITransferSession;

/**
 * 传输处理器定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:46
 */
public interface ITransferHandler extends SafeCloseable {

    /**
     * 处理文本消息
     *
     * @param payload payload
     */
    void handleMessage(TransferOperatorRequest payload);

    /**
     * 处理二进制消息
     *
     * @param content content
     */
    void handleMessage(byte[] content);

    /**
     * 通过 token 获取 session
     *
     * @param token token
     * @return session
     */
    ITransferSession getSessionByToken(String token);

}
