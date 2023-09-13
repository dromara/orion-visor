package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.annotation.IgnoreLog;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.framework.common.constant.IgnoreLogMode;
import com.orion.ops.framework.common.valid.group.Id;
import com.orion.ops.framework.common.valid.group.Page;
import com.orion.ops.module.asset.entity.request.host.HostCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostVO;
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

    @PostMapping("/create")
    @Operation(summary = "创建主机")
    @PreAuthorize("@ss.hasPermission('asset:host:create')")
    public Long createHost(@Validated @RequestBody HostCreateRequest request) {
        return hostService.createHost(request);
    }

    @PutMapping("/update")
    @Operation(summary = "通过 id 更新主机")
    @PreAuthorize("@ss.hasPermission('asset:host:update')")
    public Integer updateHost(@Validated(Id.class) @RequestBody HostUpdateRequest request) {
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

}

