package com.orion.visor.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.common.validator.group.Page;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.IgnoreWrapper;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.visor.module.asset.entity.request.host.HostSftpLogQueryRequest;
import com.orion.visor.module.asset.entity.vo.HostSftpLogVO;
import com.orion.visor.module.asset.service.HostSftpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * SFTP 操作服务 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Tag(name = "asset - SFTP 操作服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-sftp")
public class HostSftpLogController {

    @Resource
    private HostSftpService hostSftpService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query-log")
    @Operation(summary = "分页查询 SFTP 操作日志")
    @PreAuthorize("@ss.hasAnyPermission('infra:operator-log:query', 'asset:host-sftp-log:management:query')")
    public DataGrid<HostSftpLogVO> getHostSftpLogPage(@Validated(Page.class) @RequestBody HostSftpLogQueryRequest request) {
        return hostSftpService.getHostSftpLogPage(request);
    }

    @OperatorLog(HostTerminalOperatorType.DELETE_SFTP_LOG)
    @DeleteMapping("/delete-log")
    @Operation(summary = "删除 SFTP 操作日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasAnyPermission('infra:operator-log:delete', 'asset:host-sftp-log:management:delete')")
    public Integer deleteHostSftpLog(@RequestParam("idList") List<Long> idList) {
        return hostSftpService.deleteHostSftpLog(idList);
    }

    @PermitAll
    @IgnoreWrapper
    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/download")
    @Operation(summary = "下载文件")
    @Parameter(name = "channelId", description = "channelId", required = true)
    @Parameter(name = "transferToken", description = "transferToken", required = true)
    public StreamingResponseBody downloadWithTransferToken(@RequestParam("channelId") String channelId,
                                                           @RequestParam("transferToken") String transferToken,
                                                           HttpServletResponse response) {
        return hostSftpService.downloadWithTransferToken(channelId, transferToken, response);
    }

}

