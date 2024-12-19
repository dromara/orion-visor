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
package org.dromara.visor.module.asset.handler.host.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 终端连接参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 23:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminalConfig {

    /**
     * logId
     */
    private Long logId;

    /**
     * 主机id
     */
    private Long hostId;

    /**
     * 主机名称
     */
    private String hostName;

    /**
     * 主机地址
     */
    private String address;

    /**
     * cols
     */
    private Integer cols;

    /**
     * rows
     */
    private Integer rows;

    /**
     * SSH输出编码
     */
    private String charset;

    /**
     * 文件名称编码
     */
    private String fileNameCharset;

    /**
     * 文件内容编码
     */
    private String fileContentCharset;

}
