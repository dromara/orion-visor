package com.orion.ops.module.asset.controller;

import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.ops.module.asset.entity.request.host.HostTerminalConnectRequest;
import com.orion.ops.module.asset.service.HostTerminalService;
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
 * 主机终端 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Tag(name = "asset - 主机终端服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-terminal")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HostTerminalController {

    @Resource
    private HostTerminalService hostTerminalService;

    @OperatorLog(HostTerminalOperatorType.ACCESS)
    @PostMapping("/access")
    @Operation(summary = "获取主机终端连接 token")
    @PreAuthorize("@ss.hasPermission('asset:host-terminal:access')")
    public String getHostAccessToken(@Validated @RequestBody HostTerminalConnectRequest request) {
        return hostTerminalService.getHostAccessToken(request.getHostId(), SecurityUtils.getLoginUserId());
    }

}

