/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package org.dromara.visor.module.asset.handler.host.exec.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量执行启动主机对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 15:46
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecCommandHostDTO {

    /**
     * hostLogId
     */
    private Long hostLogId;

    /**
     * hostId
     */
    private Long hostId;

    /**
     * 主机名称
     */
    private String hostName;

    /**
     * 主机地址
     */
    private String hostAddress;

    /**
     * 日志文件路径
     */
    private String logPath;

    /**
     * 脚本路径
     */
    private String scriptPath;

    /**
     * 执行命令
     */
    private String command;

    /**
     * 主机用户
     */
    private String username;

    /**
     * 命令编码
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
