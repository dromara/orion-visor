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
package org.dromara.visor.module.infra.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户 聚合响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/19 12:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserAggregateVO", description = "用户 聚合响应对象")
public class UserAggregateVO {

    @Schema(description = "用户信息")
    private SystemUserBaseVO user;

    @Schema(description = "该用户已启用的角色")
    private Collection<String> roles;

    @Schema(description = "该用户已启用的权限")
    private List<String> permissions;

    @Schema(description = "更新密码")
    private UserUpdatePasswordVO updatePassword;

    @Schema(description = "系统偏好")
    private Map<String, Object> systemPreference;

    @Schema(description = "已经提示的key")
    private List<String> tippedKeys;

}
