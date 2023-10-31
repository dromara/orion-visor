package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.define.operator.AuthenticationOperatorType;
import com.orion.ops.module.infra.entity.request.user.UserLoginRequest;
import com.orion.ops.module.infra.entity.request.user.UserUpdatePasswordRequest;
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

    @OperatorLog(AuthenticationOperatorType.LOGIN)
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
    public HttpWrapper<?> logout(HttpServletRequest servletRequest) {
        authenticationService.logout(servletRequest);
        return HttpWrapper.ok();
    }

    @OperatorLog(AuthenticationOperatorType.UPDATE_PASSWORD)
    @Operation(summary = "修改密码")
    @PutMapping("/update-password")
    public HttpWrapper<?> updatePassword(@Validated @RequestBody UserUpdatePasswordRequest request) {
        authenticationService.updatePassword(request);
        return HttpWrapper.ok();
    }

}
