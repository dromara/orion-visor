package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.operator.OperatorLogQueryRequest;
import com.orion.ops.module.infra.entity.vo.LoginHistoryVO;
import com.orion.ops.module.infra.entity.vo.OperatorLogVO;
import com.orion.ops.module.infra.service.OperatorLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Tag(name = "infra - 操作日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/operator-log")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class OperatorLogController {

    @Resource
    private OperatorLogService operatorLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询操作日志")
    @PreAuthorize("@ss.hasPermission('infra:operator-log:query')")
    public DataGrid<OperatorLogVO> getOperatorLogPage(@Validated(Page.class) @RequestBody OperatorLogQueryRequest request) {
        return operatorLogService.getOperatorLogPage(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/login-history")
    @Operation(summary = "查询用户登录日志")
    @PreAuthorize("@ss.hasPermission('infra:system-user:login-history')")
    public List<LoginHistoryVO> getLoginHistory(@RequestParam("username") String username) {
        return operatorLogService.getLoginHistory(username);
    }

}

