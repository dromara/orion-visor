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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.define.operator.HostGroupOperatorType;
import org.dromara.visor.module.asset.entity.request.host.HostGroupRelUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostGroupTreeVO;
import org.dromara.visor.module.asset.service.HostGroupService;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupMoveDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 主机分组 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-09 16:16
 */
@Tag(name = "asset - 主机分组服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-group")
public class HostGroupController {

    @Resource
    private HostGroupService hostGroupService;

    @DemoDisableApi
    @OperatorLog(HostGroupOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建主机分组")
    @PreAuthorize("@ss.hasPermission('asset:host-group:update')")
    public Long createHostGroup(@Validated @RequestBody DataGroupCreateDTO request) {
        return hostGroupService.createHostGroup(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/tree")
    @Operation(summary = "查询主机分组")
    @PreAuthorize("@ss.hasPermission('asset:host-group:update')")
    public List<HostGroupTreeVO> queryHostGroupTree() {
        return hostGroupService.queryHostGroupTree();
    }

    @DemoDisableApi
    @OperatorLog(HostGroupOperatorType.RENAME)
    @PutMapping("/rename")
    @Operation(summary = "修改名称")
    @PreAuthorize("@ss.hasAnyPermission('asset:host-group:update', 'asset:host:query')")
    public Integer updateHostGroupName(@Validated @RequestBody DataGroupRenameDTO request) {
        return hostGroupService.updateHostGroupName(request);
    }

    @DemoDisableApi
    @OperatorLog(HostGroupOperatorType.MOVE)
    @PutMapping("/move")
    @Operation(summary = "移动位置")
    @PreAuthorize("@ss.hasPermission('asset:host-group:update')")
    public Integer moveHostGroup(@Validated @RequestBody DataGroupMoveDTO request) {
        return hostGroupService.moveHostGroup(request);
    }

    @DemoDisableApi
    @OperatorLog(HostGroupOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除主机分组")
    @PreAuthorize("@ss.hasPermission('asset:host-group:update')")
    public Integer deleteHostGroup(@RequestParam("id") Long id) {
        return hostGroupService.deleteHostGroup(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/rel-list")
    @Operation(summary = "查询分组内主机")
    @Parameter(name = "groupId", description = "groupId", required = true)
    @PreAuthorize("@ss.hasAnyPermission('asset:host-group:update', 'asset:host:query')")
    public Set<Long> queryHostGroupRel(@RequestParam("groupId") Long groupId) {
        return hostGroupService.queryHostGroupRel(groupId);
    }

    @DemoDisableApi
    @OperatorLog(HostGroupOperatorType.UPDATE_REL)
    @PutMapping("/update-rel")
    @Operation(summary = "修改分组内主机")
    @PreAuthorize("@ss.hasPermission('asset:host-group:update')")
    public Boolean updateHostGroupRel(@Validated @RequestBody HostGroupRelUpdateRequest request) {
        hostGroupService.updateHostGroupRel(request);
        return true;
    }

}

