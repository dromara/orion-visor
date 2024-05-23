package com.orion.visor.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.common.validator.group.Id;
import com.orion.visor.framework.common.validator.group.Page;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.DemoDisableApi;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.define.operator.HostConnectLogOperatorType;
import com.orion.visor.module.asset.entity.request.host.HostConnectLogQueryRequest;
import com.orion.visor.module.asset.entity.vo.HostConnectLogVO;
import com.orion.visor.module.asset.service.HostConnectLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/session")
    @Operation(summary = "查询全部主机连接会话")
    @PreAuthorize("@ss.hasPermission('asset:host-connect-session:management:query')")
    public List<HostConnectLogVO> getHostConnectSessions(@Validated @RequestBody HostConnectLogQueryRequest request) {
        return hostConnectLogService.getHostConnectSessions(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/latest-connect")
    @Operation(summary = "查询用户最近连接的主机")
    public List<Long> getLatestConnectHostId(@RequestBody HostConnectLogQueryRequest request) {
        return hostConnectLogService.getLatestConnectHostId(request);
    }

    @OperatorLog(HostConnectLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除主机连接日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-connect-log:management:delete')")
    public Integer deleteHostConnectLog(@RequestParam("idList") List<Long> idList) {
        return hostConnectLogService.deleteHostConnectLog(idList);
    }

    @PostMapping("/query-count")
    @Operation(summary = "查询主机连接日志数量")
    @PreAuthorize("@ss.hasPermission('asset:host-connect-log:management:clear')")
    public Long getHostConnectLogCount(@RequestBody HostConnectLogQueryRequest request) {
        return hostConnectLogService.getHostConnectLogCount(request);
    }

    @OperatorLog(HostConnectLogOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清空主机连接日志")
    @PreAuthorize("@ss.hasPermission('asset:host-connect-log:management:clear')")
    public Integer clearHostConnectLog(@RequestBody HostConnectLogQueryRequest request) {
        return hostConnectLogService.clearHostConnectLog(request);
    }

    @DemoDisableApi
    @OperatorLog(HostConnectLogOperatorType.FORCE_OFFLINE)
    @PutMapping("/force-offline")
    @Operation(summary = "强制断开主机连接")
    @PreAuthorize("@ss.hasAnyPermission('asset:host-connect-log:management:force-offline', 'asset:host-connect-session:management:force-offline')")
    public Integer forceOffline(@Validated(Id.class) @RequestBody HostConnectLogQueryRequest request) {
        return hostConnectLogService.forceOffline(request);
    }

}

