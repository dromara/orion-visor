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
package org.dromara.visor.framework.common.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 当前登录用户
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/6 18:36
 */
@Data
@Schema(name = "LoginUser", description = "当前登录用户对象")
public class LoginUser {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "花名")
    private String nickname;

    @Schema(description = "用户状态")
    private Integer status;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "登录时间戳")
    private Long timestamp;

    @Schema(description = "角色")
    private List<UserRole> roles;

}
