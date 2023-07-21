package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.common.annotation.IgnoreLog;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.framework.common.constant.IgnoreLogMode;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.infra.entity.request.user.UserLoginRequest;
import com.orion.ops.module.infra.entity.request.user.UserResetPasswordRequest;
import com.orion.ops.module.infra.entity.vo.UserLoginVO;
import com.orion.ops.module.infra.service.AuthenticationService;
import com.orion.ops.module.infra.service.SystemUserService;
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

    @Resource
    private SystemUserService systemUserService;

    @PermitAll
    @Operation(summary = "登陆")
    @PostMapping("/login")
    public UserLoginVO login(@Validated @RequestBody UserLoginRequest request,
                             HttpServletRequest servletRequest) {
        String token = authenticationService.login(request, servletRequest);
        return UserLoginVO.builder().token(token).build();
    }

    @PermitAll
    @IgnoreLog(IgnoreLogMode.RET)
    @Operation(summary = "登出")
    @GetMapping("/logout")
    public HttpWrapper<?> logout(HttpServletRequest servletRequest) {
        authenticationService.logout(servletRequest);
        return HttpWrapper.ok();
    }

    @Operation(summary = "修改密码")
    @PutMapping("/update-password")
    public HttpWrapper<?> updatePassword(@Validated @RequestBody UserResetPasswordRequest request) {
        // 当前用户id
        request.setId(SecurityUtils.getLoginUserId());
        systemUserService.resetPassword(request);
        return HttpWrapper.ok();
    }

}
