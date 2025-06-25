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
package org.dromara.visor.module.terminal.handler.terminal.model;

import org.dromara.visor.common.entity.RequestIdentity;
import cn.orionsec.kit.lang.define.collect.MutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 终端连接属性
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/31 15:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalChannelProps {

    /**
     * 会话id
     */
    private String id;

    /**
     * hostId
     */
    private Long hostId;

    /**
     * 会话类型
     */
    private String type;

    /**
     * 连接用户
     */
    private Long userId;

    /**
     * 连接用户名
     */
    private String username;

    /**
     * 连接用户昵称
     */
    private String nickname;

    /**
     * traceId
     */
    private String traceId;

    /**
     * channel
     */
    private String channel;

    /**
     * 请求留痕信息
     */
    private RequestIdentity identity;

    /**
     * 额外配置
     */
    private TerminalChannelExtra extra;

    /**
     * 会话属性
     */
    private MutableMap<String, Object> attr;

}
