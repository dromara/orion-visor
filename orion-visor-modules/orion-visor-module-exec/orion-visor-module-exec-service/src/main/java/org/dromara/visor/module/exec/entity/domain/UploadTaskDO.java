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
package org.dromara.visor.module.exec.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

import java.util.Date;

/**
 * 上传任务 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "upload_task", autoResultMap = true)
@Schema(name = "UploadTaskDO", description = "上传任务 实体对象")
public class UploadTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "远程路径")
    @TableField("remote_path")
    private String remotePath;

    @Schema(description = "描述")
    @TableField("description")
    private String description;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

    @Schema(description = "额外信息")
    @TableField("extra_info")
    private String extraInfo;

    @Schema(description = "文件数量")
    @TableField("file_count")
    private Integer fileCount;

    @Schema(description = "主机数量")
    @TableField("host_count")
    private Integer hostCount;

    @Schema(description = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "结束时间")
    @TableField("end_time")
    private Date endTime;

}
