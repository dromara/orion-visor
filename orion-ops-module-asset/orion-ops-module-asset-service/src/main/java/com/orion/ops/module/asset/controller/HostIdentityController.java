package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.annotation.IgnoreLog;
import com.orion.ops.framework.common.annotation.RestWrapper;
import com.orion.ops.framework.common.constant.IgnoreLogMode;
import com.orion.ops.framework.common.valid.group.Page;
import com.orion.ops.module.asset.service.*;
import com.orion.ops.module.asset.entity.vo.*;
import com.orion.ops.module.asset.entity.request.host.*;
import com.orion.ops.module.asset.entity.export.*;
import com.orion.ops.module.asset.convert.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 主机身份 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Tag(name = "asset - 主机身份服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-identity")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HostIdentityController {

    @Resource
    private HostIdentityService hostIdentityService;

    @PostMapping("/create")
    @Operation(summary = "创建主机身份")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:create')")
    public Long createHostIdentity(@Validated @RequestBody HostIdentityCreateRequest request) {
        return hostIdentityService.createHostIdentity(request);
    }

    @PutMapping("/update")
    @Operation(summary = "通过 id 更新主机身份")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:update')")
    public Integer updateHostIdentity(@Validated @RequestBody HostIdentityUpdateRequest request) {
        return hostIdentityService.updateHostIdentityById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "通过 id 查询主机身份")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-identity:query')")
    public HostIdentityVO getHostIdentity(@RequestParam("id") Long id) {
        return hostIdentityService.getHostIdentityById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "通过 id 批量查询主机身份")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-identity:query')")
    public List<HostIdentityVO> getHostIdentityList(@RequestParam("idList") List<Long> idList) {
        return hostIdentityService.getHostIdentityByIdList(idList);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list-all")
    @Operation(summary = "查询主机身份")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:query')")
    public List<HostIdentityVO> getHostIdentityListAll(@Validated @RequestBody HostIdentityQueryRequest request) {
        return hostIdentityService.getHostIdentityList(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询主机身份")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:query')")
    public DataGrid<HostIdentityVO> getHostIdentityPage(@Validated(Page.class) @RequestBody HostIdentityQueryRequest request) {
        return hostIdentityService.getHostIdentityPage(request);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除主机身份")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-identity:delete')")
    public Integer deleteHostIdentity(@RequestParam("id") Long id) {
        return hostIdentityService.deleteHostIdentityById(id);
    }

    @DeleteMapping("/delete-batch")
    @Operation(summary = "通过 id 批量删除主机身份")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-identity:delete')")
    public Integer batchDeleteHostIdentity(@RequestParam("idList") List<Long> idList) {
        return hostIdentityService.batchDeleteHostIdentityByIdList(idList);
    }

    @PostMapping("/export")
    @Operation(summary = "导出主机身份")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:export')")
    public void exportHostIdentity(@Validated @RequestBody HostIdentityQueryRequest request,
                              HttpServletResponse response) throws IOException {
        hostIdentityService.exportHostIdentity(request, response);
    }

}

