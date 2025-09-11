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
package org.dromara.visor.module.asset.entity.request.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.visor.common.entity.BaseQueryRequest;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 主机 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "HostQueryRequest", description = "主机 查询请求对象")
public class HostQueryRequest extends BaseQueryRequest {

    @Schema(description = "搜索")
    private String searchValue;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "id")
    private List<Long> idList;

    @Size(max = 8)
    @Schema(description = "主机类型")
    private String type;

    @Size(max = 64)
    @Schema(description = "主机名称")
    private String name;

    @Size(max = 64)
    @Schema(description = "主机编码")
    private String code;

    @Size(max = 128)
    @Schema(description = "主机地址")
    private String address;

    @Size(max = 12)
    @Schema(description = "系统类型")
    private String osType;

    @Size(max = 12)
    @Schema(description = "系统架构")
    private String archType;

    @Size(max = 8)
    @Schema(description = "主机状态")
    private String status;

    @Size(max = 255)
    @Schema(description = "描述")
    private String description;

    @Schema(description = "agentKey")
    private String agentKey;

    @Schema(description = "探针安装状态")
    private Integer agentInstallStatus;

    @Schema(description = "探针在线状态")
    private Integer agentOnlineStatus;

    @Schema(description = "通过探针排序")
    private Boolean orderByAgent;

    @Schema(description = "tag")
    private List<Long> tags;

    @Schema(description = "是否查询分组信息")
    private Boolean queryGroup;

    @Schema(description = "是否查询 tag 信息")
    private Boolean queryTag;

    @Schema(description = "是否查询规格信息")
    private Boolean querySpec;

}
