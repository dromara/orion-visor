package com.orion.ops.module.asset.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.define.operator.HostGroupOperatorType;
import com.orion.ops.module.asset.define.operator.HostIdentityOperatorType;
import com.orion.ops.module.asset.define.operator.HostKeyOperatorType;
import com.orion.ops.module.asset.entity.request.asset.AssetDataGrantRequest;
import com.orion.ops.module.asset.service.AssetDataGrantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 资产模块 授权数据服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 14:10
 */
@Tag(name = "asset - 授权数据服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/data-grant")
public class AssetDataGrantServiceController {

    // FIXME 字典 菜单 http 前端api

    @Resource
    private AssetDataGrantService assetDataGrantService;

    @OperatorLog(HostGroupOperatorType.GRANT)
    @PutMapping("/host-group")
    @Operation(summary = "主机分组授权")
    @PreAuthorize("@ss.hasPermission('asset:host-group:grant')")
    public HttpWrapper<?> grantHostGroup(@RequestBody AssetDataGrantRequest request) {
        assetDataGrantService.grantHostGroup(request);
        return HttpWrapper.ok();
    }

    @OperatorLog(HostKeyOperatorType.GRANT)
    @PutMapping("/host-key")
    @Operation(summary = "主机秘钥授权")
    @PreAuthorize("@ss.hasPermission('asset:host-key:grant')")
    public HttpWrapper<?> grantHostKey(@RequestBody AssetDataGrantRequest request) {
        assetDataGrantService.grantHostKey(request);
        return HttpWrapper.ok();
    }

    @OperatorLog(HostIdentityOperatorType.GRANT)
    @PutMapping("/host-identity")
    @Operation(summary = "主机身份授权")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:grant')")
    public HttpWrapper<?> grantHostIdentity(@RequestBody AssetDataGrantRequest request) {
        assetDataGrantService.grantHostIdentity(request);
        return HttpWrapper.ok();
    }

}
