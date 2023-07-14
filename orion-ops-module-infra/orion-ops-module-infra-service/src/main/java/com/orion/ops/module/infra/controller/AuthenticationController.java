package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.common.annotation.IgnoreLog;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.UserLoginRequest;
import com.orion.ops.module.infra.entity.vo.UserLoginVO;
import com.orion.ops.module.infra.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class AuthenticationController {

    @Resource
    private AuthenticationService authenticationService;

    @PermitAll
    @Operation(summary = "登陆")
    @PostMapping("/login")
    public UserLoginVO login(@Validated @RequestBody UserLoginRequest request,
                             HttpServletRequest servletRequest) {
        // 验证登陆
        String token = authenticationService.login(request, servletRequest);
        return UserLoginVO.builder().token(token).build();
    }

    @IgnoreLog
    @PermitAll
    @Operation(summary = "登出")
    @GetMapping("/logout")
    public HttpWrapper<?> logout(HttpServletRequest servletRequest) {
        // 登出
        authenticationService.logout(servletRequest);
        return HttpWrapper.ok();
    }

}
