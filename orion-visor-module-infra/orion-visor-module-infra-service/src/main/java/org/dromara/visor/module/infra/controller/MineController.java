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
package org.dromara.visor.module.infra.controller;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.common.validator.group.Page;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.define.operator.AuthenticationOperatorType;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import org.dromara.visor.module.infra.entity.request.user.SystemUserUpdateRequest;
import org.dromara.visor.module.infra.entity.request.user.UserSessionOfflineRequest;
import org.dromara.visor.module.infra.entity.request.user.UserUpdatePasswordRequest;
import org.dromara.visor.module.infra.entity.vo.LoginHistoryVO;
import org.dromara.visor.module.infra.entity.vo.OperatorLogVO;
import org.dromara.visor.module.infra.entity.vo.SystemUserVO;
import org.dromara.visor.module.infra.entity.vo.UserSessionVO;
import org.dromara.visor.module.infra.service.MineService;
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

    @DemoDisableApi
    @OperatorLog(AuthenticationOperatorType.UPDATE_PASSWORD)
    @Operation(summary = "修改当前用户密码")
    @PutMapping("/update-password")
    public Boolean updateCurrentUserPassword(@Validated @RequestBody UserUpdatePasswordRequest request) {
        mineService.updateCurrentUserPassword(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/login-history")
    @Operation(summary = "查询当前用户登录日志")
    public List<LoginHistoryVO> getCurrentLoginHistory(@RequestParam("count") Integer count) {
        return mineService.getCurrentLoginHistory(count);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/user-session")
    @Operation(summary = "获取当前用户会话列表")
    public List<UserSessionVO> getCurrentUserSessionList() {
        return mineService.getCurrentUserSessionList();
    }

    @DemoDisableApi
    @PutMapping("/offline-session")
    @Operation(summary = "下线当前用户会话")
    public Boolean offlineCurrentUserSession(@Validated @RequestBody UserSessionOfflineRequest request) {
        mineService.offlineCurrentUserSession(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query-operator-log")
    @Operation(summary = "查询当前用户操作日志")
    public DataGrid<OperatorLogVO> getCurrentUserOperatorLog(@Validated(Page.class) @RequestBody OperatorLogQueryRequest request) {
        return mineService.getCurrentUserOperatorLog(request);
    }

}
