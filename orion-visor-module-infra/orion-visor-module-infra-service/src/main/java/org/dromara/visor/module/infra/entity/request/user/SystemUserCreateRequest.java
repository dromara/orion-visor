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
package org.dromara.visor.module.infra.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.framework.common.constant.ValidConst;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户 创建请求对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 00:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemUserCreateRequest", description = "用户 创建请求对象")
public class SystemUserCreateRequest implements Serializable {

    @NotBlank
    @Size(max = 32)
    @Pattern(regexp = ValidConst.USERNAME_4_32_PATTERN, message = ValidConst.USERNAME_4_32_MESSAGE)
    @Schema(description = "用户名")
    private String username;

    @NotBlank
    @Size(max = 64)
    @Schema(description = "密码")
    private String password;

    @NotBlank
    @Size(max = 16)
    @Schema(description = "花名")
    private String nickname;

    @Size(max = 15)
    @Schema(description = "手机号")
    private String mobile;

    @Size(max = 64)
    @Schema(description = "邮箱")
    private String email;

}
