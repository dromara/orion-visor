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
package com.orion.visor.module.infra.entity.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 修改密码请求
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/17 12:19
 */
@Data
@Schema(name = "UserUpdatePasswordRequest", description = "修改密码请求")
public class UserUpdatePasswordRequest {

    @NotEmpty
    @Schema(description = "原密码")
    private String beforePassword;

    @NotEmpty
    @Schema(description = "新密码")
    private String password;

}
