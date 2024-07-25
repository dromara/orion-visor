package com.orion.visor.module.asset.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.common.validator.group.Page;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.DemoDisableApi;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.define.operator.HostKeyOperatorType;
import com.orion.visor.module.asset.entity.request.host.HostKeyCreateRequest;
import com.orion.visor.module.asset.entity.request.host.HostKeyQueryRequest;
import com.orion.visor.module.asset.entity.request.host.HostKeyUpdateRequest;
import com.orion.visor.module.asset.entity.vo.HostKeyVO;
import com.orion.visor.module.asset.service.HostKeyService;
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
 * 主机密钥 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Tag(name = "asset - 主机密钥服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-key")
public class HostKeyController {

    @Resource
    private HostKeyService hostKeyService;

    @DemoDisableApi
    @OperatorLog(HostKeyOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建主机密钥")
    @PreAuthorize("@ss.hasPermission('asset:host-key:create')")
    public Long createHostKey(@Validated @RequestBody HostKeyCreateRequest request) {
        return hostKeyService.createHostKey(request);
    }

    @DemoDisableApi
    @OperatorLog(HostKeyOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "通过 id 更新主机密钥")
    @PreAuthorize("@ss.hasPermission('asset:host-key:update')")
    public Integer updateHostKey(@Validated @RequestBody HostKeyUpdateRequest request) {
        return hostKeyService.updateHostKeyById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询主机密钥详情")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasAnyPermission('asset:host-key:query-detail', 'asset:host-key:update')")
    public HostKeyVO getHostKey(@RequestParam("id") Long id) {
        return hostKeyService.getHostKeyById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询主机密钥")
    @PreAuthorize("@ss.hasPermission('asset:host-key:query')")
    public List<HostKeyVO> getHostKeyList() {
        return hostKeyService.getHostKeyList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询主机密钥")
    @PreAuthorize("@ss.hasPermission('asset:host-key:query')")
    public DataGrid<HostKeyVO> getHostKeyPage(@Validated(Page.class) @RequestBody HostKeyQueryRequest request) {
        return hostKeyService.getHostKeyPage(request);
    }

    @DemoDisableApi
    @OperatorLog(HostKeyOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除主机密钥")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-key:delete')")
    public Integer deleteHostKey(@RequestParam("id") Long id) {
        return hostKeyService.deleteHostKeyById(id);
    }

    @DemoDisableApi
    @OperatorLog(HostKeyOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除主机密钥")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-key:delete')")
    public Integer batchDeleteHostKey(@RequestParam("idList") List<Long> idList) {
        return hostKeyService.deleteHostKeyByIdList(idList);
    }

}

