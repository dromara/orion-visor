package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.valid.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.host.HostIdentityCreateRequest;
import com.orion.ops.module.asset.entity.request.host.HostIdentityQueryRequest;
import com.orion.ops.module.asset.entity.request.host.HostIdentityUpdateRequest;
import com.orion.ops.module.asset.entity.vo.HostIdentityVO;
import com.orion.ops.module.asset.service.HostIdentityService;
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
    @Operation(summary = "查询主机身份")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-identity:query')")
    public List<HostIdentityVO> getHostIdentityList() {
        return hostIdentityService.getHostIdentityList();
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

}

