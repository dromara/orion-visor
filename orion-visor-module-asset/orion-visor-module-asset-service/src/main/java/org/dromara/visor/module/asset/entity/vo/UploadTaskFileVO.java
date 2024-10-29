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
package org.dromara.visor.module.asset.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 上传任务文件 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-8 10:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UploadTaskFileVO", description = "上传任务文件 视图响应对象")
public class UploadTaskFileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户id")
    private Long taskId;

    @Schema(description = "主机id")
    private Long hostId;

    @Schema(description = "文件id")
    private String fileId;

    @Schema(description = "文件路径")
    private String filePath;

    @Schema(description = "实际文件路径")
    private String realFilePath;

    @Schema(description = "文件大小")
    private Long fileSize;

    @Schema(description = "额外信息")
    private String extraInfo;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "传输进度")
    private Long current;

}
