package com.orion.visor.module.asset.controller;

import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.define.operator.HostGroupOperatorType;
import com.orion.visor.module.asset.define.operator.HostIdentityOperatorType;
import com.orion.visor.module.asset.define.operator.HostKeyOperatorType;
import com.orion.visor.module.asset.entity.request.asset.AssetAuthorizedDataQueryRequest;
import com.orion.visor.module.asset.entity.request.asset.AssetDataGrantRequest;
import com.orion.visor.module.asset.service.AssetAuthorizedDataService;
import com.orion.visor.module.asset.service.AssetDataGrantService;
import com.orion.visor.module.infra.enums.DataPermissionTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private AssetDataGrantService assetDataGrantService;

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @OperatorLog(HostGroupOperatorType.GRANT)
    @PutMapping("/grant-host-group")
    @Operation(summary = "主机分组授权")
    @PreAuthorize("@ss.hasPermission('asset:host-group:grant')")
    public Boolean grantHostGroup(@RequestBody AssetDataGrantRequest request) {
        assetDataGrantService.grantHostGroup(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-host-group")
    @Operation(summary = "获取已授权的主机分组")
    @PreAuthorize("@ss.hasPermission('asset:host-group:grant')")
    public List<Long> getAuthorizedHostGroup(AssetAuthorizedDataQueryRequest request) {
        return assetAuthorizedDataService.getAuthorizedDataRelId(DataPermissionTypeEnum.HOST_GROUP, request);
    }

    @OperatorLog(HostKeyOperatorType.GRANT)
    @PutMapping("/grant-host-key")
    @Operation(summary = "主机密钥授权")
    @PreAuthorize("@ss.hasPermission('asset:host-key:grant')")
    public Boolean grantHostKey(@RequestBody AssetDataGrantRequest request) {
        assetDataGrantService.grantHostKey(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-host-key")
    @Operation(summary = "获取已授权的主机密钥")
    @PreAuthorize("@ss.hasPermission('asset:host-key:grant')")
    public List<Long> getAuthorizedHostKey(AssetAuthorizedDataQueryRequest request) {
        return assetAuthorizedDataService.getAuthorizedDataRelId(DataPermissionTypeEnum.HOST_KEY, request);
    }

    @OperatorLog(HostIdentityOperatorType.GRANT)
    @PutMapping("/grant-host-identity")
    @Operation(summary = "主机身份授权")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:grant')")
    public Boolean grantHostIdentity(@RequestBody AssetDataGrantRequest request) {
        assetDataGrantService.grantHostIdentity(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-host-identity")
    @Operation(summary = "获取已授权的主机身份")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:grant')")
    public List<Long> getAuthorizedHostIdentity(AssetAuthorizedDataQueryRequest request) {
        return assetAuthorizedDataService.getAuthorizedDataRelId(DataPermissionTypeEnum.HOST_IDENTITY, request);
    }

}
