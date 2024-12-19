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
package org.dromara.visor.module.asset.handler.host.upload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文件上传配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/8 14:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadConfigDTO {

    /**
     * taskId
     */
    private Long taskId;

    /**
     * hostId
     */
    private Long hostId;

    /**
     * userId
     */
    private Long userId;

    /**
     * 上传路径
     */
    private String remotePath;

    /**
     * 文件
     */
    private List<FileUploadFileItemDTO> files;

}
