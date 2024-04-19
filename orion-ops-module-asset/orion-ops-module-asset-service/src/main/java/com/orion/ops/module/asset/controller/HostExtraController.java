package com.orion.ops.module.asset.controller;

import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.host.HostExtraQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostExtraUpdateRequest;
import com.orion.ops.module.asset.service.HostExtraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 主机拓展信息 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Tag(name = "asset - 主机拓展信息服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-extra")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HostExtraController {

    @Resource
    private HostExtraService hostExtraService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "获取主机拓展信息")
    @Parameter(name = "hostId", description = "hostId", required = true)
    @Parameter(name = "item", description = "item", required = true)
    public Map<String, Object> getHostExtra(@RequestParam("hostId") Long hostId, @RequestParam("item") String item) {
        return hostExtraService.getHostExtra(hostId, item);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list")
    @Operation(summary = "获取多个主机拓展信息")
    public Map<String, Map<String, Object>> getHostExtraList(@Validated @RequestBody HostExtraQueryRequest request) {
        return hostExtraService.getHostExtraList(request);
    }

    @PutMapping("/update")
    @Operation(summary = "修改主机拓展信息")
    public Integer updateHostExtra(@Validated @RequestBody HostExtraUpdateRequest request) {
        return hostExtraService.updateHostExtra(request);
    }

}

