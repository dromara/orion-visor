package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.valid.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.host.*;
import com.orion.ops.module.asset.entity.vo.HostConfigVO;
import com.orion.ops.module.asset.entity.vo.HostVO;
import com.orion.ops.module.asset.service.HostConfigService;
import com.orion.ops.module.asset.service.HostService;
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
 * 主机 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Tag(name = "asset - 主机服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HostController {

    @Resource
    private HostService hostService;

    @Resource
    private HostConfigService hostConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建主机")
    @PreAuthorize("@ss.hasPermission('asset:host:create')")
    public Long createHost(@Validated @RequestBody HostCreateRequest request) {
        return hostService.createHost(request);
    }

    @PutMapping("/update")
    @Operation(summary = "通过 id 更新主机")
    @PreAuthorize("@ss.hasPermission('asset:host:update')")
    public Integer updateHost(@Validated @RequestBody HostUpdateRequest request) {
        return hostService.updateHostById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "通过 id 查询主机")
    @Parameter(name = "id", description = "id", required = true)
    @Parameter(name = "extra", description = "是否查询额外信息")
    @Parameter(name = "config", description = "是否查询配置信息")
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public HostVO getHost(@RequestParam("id") Long id,
                          @RequestParam(name = "extra", required = false) boolean extra,
                          @RequestParam(name = "config", required = false) boolean config) {
        HostQueryRequest request = new HostQueryRequest();
        request.setId(id);
        request.setExtra(extra);
        request.setConfig(config);
        return hostService.getHostById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list-all")
    @Operation(summary = "查询主机")
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public List<HostVO> getHostListAll(@Validated @RequestBody HostQueryRequest request) {
        return hostService.getHostList(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询主机")
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public DataGrid<HostVO> getHostPage(@Validated(Page.class) @RequestBody HostQueryRequest request) {
        return hostService.getHostPage(request);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除主机")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:delete')")
    public Integer deleteHost(@RequestParam("id") Long id) {
        return hostService.deleteHostById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-config")
    @Operation(summary = "查询主机配置")
    @Parameter(name = "hostId", description = "hostId", required = true)
    @Parameter(name = "type", description = "配置类型", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public HostConfigVO getHostConfig(@RequestParam("hostId") Long hostId,
                                      @RequestParam(name = "type") String type) {
        return hostConfigService.getHostConfig(hostId, type);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-config-all")
    @Operation(summary = "查询主机配置 - 全部")
    @Parameter(name = "hostId", description = "hostId", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public List<HostConfigVO> getHostConfig(@RequestParam("hostId") Long hostId) {
        return hostConfigService.getHostConfig(hostId);
    }

    @PutMapping("/update-config")
    @Operation(summary = "更新主机配置")
    @PreAuthorize("@ss.hasPermission('asset:host:update-config')")
    public Integer updateHostConfig(@Validated @RequestBody HostConfigUpdateRequest request) {
        return hostConfigService.updateHostConfig(request);
    }

    @PutMapping("/update-config-status")
    @Operation(summary = "更新主机配置状态")
    @PreAuthorize("@ss.hasPermission('asset:host:update-config')")
    public Integer updateHostConfigStatus(@Validated @RequestBody HostConfigUpdateStatusRequest request) {
        return hostConfigService.updateHostConfigStatus(request);
    }

}

