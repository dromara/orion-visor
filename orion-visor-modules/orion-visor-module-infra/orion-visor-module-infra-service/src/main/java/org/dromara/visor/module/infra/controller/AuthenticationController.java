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
package org.dromara.visor.module.infra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.biz.operator.log.core.enums.ReturnType;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.define.operator.AuthenticationOperatorType;
import org.dromara.visor.module.infra.entity.request.user.UserLoginRequest;
import org.dromara.visor.module.infra.entity.vo.UserLoginVO;
import org.dromara.visor.module.infra.service.AuthenticationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

/**
 * 认证服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 11:20
 */
@Tag(name = "infra - 认证服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/auth")
public class AuthenticationController {

    @Resource
    private AuthenticationService authenticationService;

    @OperatorLog(value = AuthenticationOperatorType.LOGIN, ret = ReturnType.IGNORE)
    @PermitAll
    @Operation(summary = "登录")
    @PostMapping("/login")
    public UserLoginVO login(@Validated @RequestBody UserLoginRequest request,
                             HttpServletRequest servletRequest) {
        return authenticationService.login(request, servletRequest);
    }

    @OperatorLog(AuthenticationOperatorType.LOGOUT)
    @PermitAll
    @IgnoreLog(IgnoreLogMode.RET)
    @Operation(summary = "登出")
    @GetMapping("/logout")
    public Boolean logout(HttpServletRequest servletRequest) {
        authenticationService.logout(servletRequest);
        return true;
    }

}
