/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.asset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.define.operator.HostGroupOperatorType;
import org.dromara.visor.module.asset.define.operator.HostIdentityOperatorType;
import org.dromara.visor.module.asset.define.operator.HostKeyOperatorType;
import org.dromara.visor.module.asset.entity.request.asset.AssetAuthorizedDataQueryRequest;
import org.dromara.visor.module.asset.entity.request.asset.AssetDataGrantRequest;
import org.dromara.visor.module.asset.service.AssetAuthorizedDataService;
import org.dromara.visor.module.asset.service.AssetDataGrantService;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
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

    @DemoDisableApi
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

    @DemoDisableApi
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

    @DemoDisableApi
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
