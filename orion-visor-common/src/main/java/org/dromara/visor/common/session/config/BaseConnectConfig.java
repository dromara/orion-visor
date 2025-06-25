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
package org.dromara.visor.common.session.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 基础连接配置实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/4/1 16:59
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BaseConnectConfig", description = "基础连接参数")
public class BaseConnectConfig implements IBaseConnectConfig {

    @Schema(description = "系统类型")
    private String osType;

    @Schema(description = "系统架构")
    private String archType;

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "hostName")
    private String hostName;

    @Schema(description = "主机编码")
    private String hostCode;

    @Schema(description = "主机地址")
    private String hostAddress;

    @Schema(description = "主机端口")
    private Integer hostPort;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

}
