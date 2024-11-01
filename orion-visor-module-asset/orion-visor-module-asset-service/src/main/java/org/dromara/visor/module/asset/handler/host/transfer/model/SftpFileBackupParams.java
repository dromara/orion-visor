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
package org.dromara.visor.module.asset.handler.host.transfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sftp 文件备份参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/15 23:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SftpFileBackupParams {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 时间戳
     */
    private Long timestamp;

}
