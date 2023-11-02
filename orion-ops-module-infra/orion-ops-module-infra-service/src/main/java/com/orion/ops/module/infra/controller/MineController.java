package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.define.operator.AuthenticationOperatorType;
import com.orion.ops.module.infra.entity.request.operator.OperatorLogQueryRequest;
import com.orion.ops.module.infra.entity.request.user.SystemUserUpdateRequest;
import com.orion.ops.module.infra.entity.request.user.UserSessionOfflineRequest;
import com.orion.ops.module.infra.entity.request.user.UserUpdatePasswordRequest;
import com.orion.ops.module.infra.entity.vo.LoginHistoryVO;
import com.orion.ops.module.infra.entity.vo.OperatorLogVO;
import com.orion.ops.module.infra.entity.vo.SystemUserVO;
import com.orion.ops.module.infra.entity.vo.UserSessionVO;
import com.orion.ops.module.infra.service.MineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 个人服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/1 0:19
 */
@Tag(name = "infra - 个人服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/mine")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class MineController {

    @Resource
    private MineService mineService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-user")
    @Operation(summary = "查询当前用户信息")
    public SystemUserVO getCurrentUserInfo() {
        return mineService.getCurrentUserInfo();
    }

    @PutMapping("/update-user")
    @Operation(summary = "更新当前用户信息")
    public Integer updateCurrentUser(@Validated @RequestBody SystemUserUpdateRequest request) {
        return mineService.updateCurrentUser(request);
    }

    @OperatorLog(AuthenticationOperatorType.UPDATE_PASSWORD)
    @Operation(summary = "修改当前用户密码")
    @PutMapping("/update-password")
    public HttpWrapper<?> updateCurrentUserPassword(@Validated @RequestBody UserUpdatePasswordRequest request) {
        mineService.updateCurrentUserPassword(request);
        return HttpWrapper.ok();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/login-history")
    @Operation(summary = "查询当前用户登录日志")
    public List<LoginHistoryVO> getCurrentLoginHistory() {
        return mineService.getCurrentLoginHistory();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/user-session")
    @Operation(summary = "获取当前用户会话列表")
    public List<UserSessionVO> getCurrentUserSessionList() {
        return mineService.getCurrentUserSessionList();
    }

    @PutMapping("/offline-session")
    @Operation(summary = "下线当前用户会话")
    public HttpWrapper<?> offlineCurrentUserSession(@Validated @RequestBody UserSessionOfflineRequest request) {
        mineService.offlineCurrentUserSession(request);
        return HttpWrapper.ok();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query-operator-log")
    @Operation(summary = "查询当前用户操作日志")
    public DataGrid<OperatorLogVO> getCurrentUserOperatorLog(@Validated(Page.class) @RequestBody OperatorLogQueryRequest request) {
        return mineService.getCurrentUserOperatorLog(request);
    }

}
