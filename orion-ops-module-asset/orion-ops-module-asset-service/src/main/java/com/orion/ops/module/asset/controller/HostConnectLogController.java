package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostConnectLogVO;
import com.orion.ops.module.asset.service.HostConnectLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 主机连接日志 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Tag(name = "asset - 主机连接日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-connect-log")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HostConnectLogController {

    @Resource
    private HostConnectLogService hostConnectLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询主机连接日志")
    @PreAuthorize("@ss.hasPermission('asset:host-connect-log:management:query')")
    public DataGrid<HostConnectLogVO> getHostConnectLogPage(@Validated(Page.class) @RequestBody HostConnectLogQueryRequest request) {
        return hostConnectLogService.getHostConnectLogPage(request);
    }

}

