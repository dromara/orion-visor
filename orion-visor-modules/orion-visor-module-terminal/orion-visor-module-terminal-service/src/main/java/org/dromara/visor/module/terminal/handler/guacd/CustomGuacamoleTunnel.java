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

import lombok.Getter;
import org.apache.guacamole.net.AbstractGuacamoleTunnel;
import org.apache.guacamole.net.GuacamoleSocket;

import java.util.UUID;

/**
 * 自定义 guacamole 隧道
 * <p>
 * 为了保证与 websocket 的 uuid 一致
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/4/1 0:23
 */
public class CustomGuacamoleTunnel extends AbstractGuacamoleTunnel {

    private final UUID uuid;

    @Getter
    private final GuacamoleSocket socket;

    public CustomGuacamoleTunnel(String uuid, GuacamoleSocket socket) {
        this.uuid = UUID.fromString(uuid);
        this.socket = socket;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

}
