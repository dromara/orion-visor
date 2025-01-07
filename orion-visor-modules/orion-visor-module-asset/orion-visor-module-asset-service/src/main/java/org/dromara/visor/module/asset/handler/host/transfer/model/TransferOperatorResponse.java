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
package org.dromara.visor.module.asset.handler.host.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件操作响应 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOperatorResponse {

    /**
     * channelId
     */
    private String channelId;

    /**
     * type
     */
    private String type;

    /**
     * 主机id
     */
    private Long hostId;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 传输的大小
     */
    private Long currentSize;

    /**
     * 文件总大小
     */
    private Long totalSize;

    /**
     * transferToken
     */
    private String transferToken;

    /**
     * 消息
     */
    private String msg;

}
