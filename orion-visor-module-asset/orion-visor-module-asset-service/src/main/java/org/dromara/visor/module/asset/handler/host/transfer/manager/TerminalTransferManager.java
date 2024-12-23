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
package org.dromara.visor.module.asset.handler.host.transfer.manager;

import org.dromara.visor.module.asset.handler.host.transfer.handler.ITransferHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 终端传输管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/4 17:58
 */
@Component
public class TerminalTransferManager {

    private final ConcurrentHashMap<String, ITransferHandler> handlers = new ConcurrentHashMap<>();

    /**
     * 添加处理器
     *
     * @param id      id
     * @param handler handler
     */
    public void putHandler(String id, ITransferHandler handler) {
        handlers.put(id, handler);
    }

    /**
     * 获取处理器
     *
     * @param id id
     * @return handler
     */
    public ITransferHandler getHandler(String id) {
        return handlers.get(id);
    }

    /**
     * 删除处理器
     *
     * @param id id
     * @return handler
     */
    public ITransferHandler removeHandler(String id) {
        return handlers.remove(id);
    }

}
