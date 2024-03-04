package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.host.HostSftpLogQueryRequest;
import com.orion.ops.module.asset.entity.vo.HostSftpLogVO;
import com.orion.ops.module.asset.service.HostSftpLogService;
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
 * SFTP 操作日志服务 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Tag(name = "asset - SFTP 操作日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/sftp-log")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HostSftpLogController {

    @Resource
    private HostSftpLogService hostSftpLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询 SFTP 操作日志")
    @PreAuthorize("@ss.hasPermission('infra:operator-log:query')")
    public DataGrid<HostSftpLogVO> querySftpLogPage(@Validated(Page.class) @RequestBody HostSftpLogQueryRequest request) {
        return hostSftpLogService.querySftpLogPage(request);
    }

}

