/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
import org.dromara.visor.module.asset.define.operator.HostOperatorType;
import org.dromara.visor.module.asset.entity.request.host.HostConfigQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostConfigUpdateRequest;
import org.dromara.visor.module.asset.service.HostConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 主机 api
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
public class HostConfigController {

    @Resource
    private HostConfigService hostConfigService;

    @DemoDisableApi
    @OperatorLog(HostOperatorType.UPDATE_CONFIG)
    @PutMapping("/update")
    @Operation(summary = "更新主机配置")
    @PreAuthorize("@ss.hasPermission('asset:host:update-config')")
    public Integer updateHostConfig(@Validated @RequestBody HostConfigUpdateRequest request) {
        return hostConfigService.updateHostConfig(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/get")
    @Operation(summary = "查询主机配置")
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public Object getHostConfig(@Validated @RequestBody HostConfigQueryRequest request) {
        return hostConfigService.getHostConfigView(request);
    }

}
