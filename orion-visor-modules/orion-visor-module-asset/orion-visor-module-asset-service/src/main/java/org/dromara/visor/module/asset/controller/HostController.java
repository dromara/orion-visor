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

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.define.operator.HostOperatorType;
import org.dromara.visor.module.asset.entity.request.host.*;
import org.dromara.visor.module.asset.entity.vo.HostConfigVO;
import org.dromara.visor.module.asset.entity.vo.HostVO;
import org.dromara.visor.module.asset.service.HostService;
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
public class HostController {

    @Resource
    private HostService hostService;

    @DemoDisableApi
    @OperatorLog(HostOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建主机")
    @PreAuthorize("@ss.hasPermission('asset:host:create')")
    public Long createHost(@Validated @RequestBody HostCreateRequest request) {
        return hostService.createHost(request);
    }

    @DemoDisableApi
    @OperatorLog(HostOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "通过 id 更新主机")
    @PreAuthorize("@ss.hasPermission('asset:host:update')")
    public Integer updateHost(@Validated @RequestBody HostUpdateRequest request) {
        return hostService.updateHostById(request);
    }

    @DemoDisableApi
    @OperatorLog(HostOperatorType.UPDATE_STATUS)
    @PutMapping("/update-status")
    @Operation(summary = "更新主机状态")
    @PreAuthorize("@ss.hasPermission('asset:host:update-status')")
    public Integer updateHostStatus(@Validated @RequestBody HostUpdateStatusRequest request) {
        return hostService.updateHostStatus(request);
    }

    @DemoDisableApi
    @OperatorLog(HostOperatorType.UPDATE_CONFIG)
    @PutMapping("/update-config")
    @Operation(summary = "更新主机配置")
    @PreAuthorize("@ss.hasPermission('asset:host:update-config')")
    public Integer updateHostConfig(@Validated @RequestBody HostUpdateConfigRequest request) {
        return hostService.updateHostConfig(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "通过 id 查询主机")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public HostVO getHost(@RequestParam("id") Long id) {
        return hostService.getHostById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-config")
    @Operation(summary = "查询主机配置")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public HostConfigVO getHostConfig(@RequestParam("id") Long id) {
        return hostService.getHostConfig(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询主机")
    @Parameter(name = "type", description = "type")
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public List<HostVO> getHostList(@RequestParam(value = "type", required = false) String type) {
        return hostService.getHostList(type);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询主机")
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public DataGrid<HostVO> getHostPage(@Validated(Page.class) @RequestBody HostQueryRequest request) {
        return hostService.getHostPage(request);
    }

    @DemoDisableApi
    @PostMapping("/count")
    @Operation(summary = "查询主机数量")
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public Long getHostExportCount(@Validated @RequestBody HostQueryRequest request) {
        return hostService.getHostCount(request);
    }

    @DemoDisableApi
    @OperatorLog(HostOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除主机")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:delete')")
    public Integer deleteHost(@RequestParam("id") Long id) {
        return hostService.deleteHostById(id);
    }

    @DemoDisableApi
    @OperatorLog(HostOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除主机")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:delete')")
    public Integer batchDeleteHost(@RequestParam("idList") List<Long> idList) {
        return hostService.deleteHostByIdList(idList);
    }

}

