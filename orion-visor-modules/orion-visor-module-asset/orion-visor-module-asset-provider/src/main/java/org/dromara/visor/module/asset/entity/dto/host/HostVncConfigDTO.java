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
package org.dromara.visor.module.asset.entity.dto.host;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.common.handler.data.model.GenericsDataModel;
import org.dromara.visor.common.security.UpdatePasswordAction;

import javax.validation.constraints.*;

/**
 * 主机 VNC 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/13 16:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostVncConfigDTO", description = "主机 VNC 配置业务对象")
public class HostVncConfigDTO implements GenericsDataModel, UpdatePasswordAction {

    @NotNull
    @Min(value = 1)
    @Max(value = 65535)
    @Schema(description = "主机端口")
    private Integer port;

    @Size(max = 128)
    @Schema(description = "用户名")
    private String username;

    @NotBlank
    @Size(max = 12)
    @Schema(description = "认证方式")
    private String authType;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "身份id")
    private Long identityId;

    @Schema(description = "无用户名")
    private Boolean noUsername;

    @Schema(description = "无密码")
    private Boolean noPassword;

    @Schema(description = "时区")
    private String timezone;

    @Schema(description = "剪切板编码")
    private String clipboardEncoding;

    @Schema(description = "是否使用新密码 仅参数")
    private Boolean useNewPassword;

    @Schema(description = "是否已设置密码 仅返回")
    private Boolean hasPassword;

}
