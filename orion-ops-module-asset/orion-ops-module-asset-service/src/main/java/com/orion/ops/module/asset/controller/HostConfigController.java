package com.orion.ops.module.asset.controller;

import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.HostOperatorType;
import com.orion.ops.module.asset.entity.request.host.HostConfigUpdateRequest;
import com.orion.ops.module.asset.entity.request.host.HostConfigUpdateStatusRequest;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;
import com.orion.ops.module.asset.service.HostConfigService;
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
 * 主机配置 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Tag(name = "asset - 主机配置服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-config")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HostConfigController {

    @Resource
    private HostConfigService hostConfigService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询主机配置")
    @Parameter(name = "hostId", description = "hostId", required = true)
    @Parameter(name = "type", description = "配置类型", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public HostConfigVO getHostConfig(@RequestParam("hostId") Long hostId,
                                      @RequestParam(name = "type") String type) {
        return hostConfigService.getHostConfig(hostId, type);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询全部主机配置")
    @Parameter(name = "hostId", description = "hostId", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public List<HostConfigVO> getHostConfigList(@RequestParam("hostId") Long hostId) {
        return hostConfigService.getHostConfigList(hostId);
    }

    @OperatorLog(HostOperatorType.UPDATE_CONFIG)
    @PutMapping("/update")
    @Operation(summary = "更新主机配置")
    @PreAuthorize("@ss.hasPermission('asset:host:update-config')")
    public Integer updateHostConfig(@Validated @RequestBody HostConfigUpdateRequest request) {
        return hostConfigService.updateHostConfig(request);
    }

    @OperatorLog(HostOperatorType.UPDATE_CONFIG_STATUS)
    @PutMapping("/update-status")
    @Operation(summary = "更新主机配置状态")
    @PreAuthorize("@ss.hasPermission('asset:host:update-config')")
    public Integer updateHostConfigStatus(@Validated @RequestBody HostConfigUpdateStatusRequest request) {
        return hostConfigService.updateHostConfigStatus(request);
    }

}

