/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.common.validator.group.Page;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.define.operator.HostIdentityOperatorType;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostIdentityUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostIdentityVO;
import org.dromara.visor.module.asset.service.HostIdentityService;
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
public class HostIdentityController {

    @Resource
    private HostIdentityService hostIdentityService;

    @DemoDisableApi
    @OperatorLog(HostIdentityOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建主机身份")
    @PreAuthorize("@ss.hasPermission('asset:host-identity:create')")
    public Long createHostIdentity(@Validated @RequestBody HostIdentityCreateRequest request) {
        return hostIdentityService.createHostIdentity(request);
    }

    @DemoDisableApi
    @OperatorLog(HostIdentityOperatorType.UPDATE)
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

    @DemoDisableApi
    @OperatorLog(HostIdentityOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除主机身份")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-identity:delete')")
    public Integer deleteHostIdentity(@RequestParam("id") Long id) {
        return hostIdentityService.deleteHostIdentityById(id);
    }

    @DemoDisableApi
    @OperatorLog(HostIdentityOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除主机身份")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host-identity:delete')")
    public Integer batchDeleteHostIdentity(@RequestParam("idList") List<Long> idList) {
        return hostIdentityService.deleteHostIdentityByIdList(idList);
    }

}

