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
import org.dromara.visor.module.asset.define.operator.HostOperatorType;
import org.dromara.visor.module.asset.entity.request.host.HostAgentInstallRequest;
import org.dromara.visor.module.asset.entity.request.host.HostAgentInstallStatusUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.HostAgentLogVO;
import org.dromara.visor.module.asset.entity.vo.HostAgentStatusVO;
import org.dromara.visor.module.asset.service.HostAgentLogService;
import org.dromara.visor.module.asset.service.HostAgentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 主机探针端点 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/22 14:33
 */
@Tag(name = "asset - 主机探针")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-agent")
public class HostAgentController {

    @Resource
    private HostAgentService hostAgentService;

    @Resource
    private HostAgentLogService hostAgentLogService;

    @IgnoreLog(IgnoreLogMode.ALL)
    @GetMapping("/status")
    @Operation(summary = "查询探针状态")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public List<HostAgentStatusVO> getAgentStatus(@RequestParam("idList") List<Long> idList) {
        return hostAgentService.getAgentStatus(idList);
    }

    @IgnoreLog(IgnoreLogMode.ALL)
    @GetMapping("/install-status")
    @Operation(summary = "查询探针安装状态")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('asset:host:query')")
    public List<HostAgentLogVO> getAgentInstallLogStatus(@RequestParam("idList") List<Long> idList) {
        return hostAgentLogService.getAgentInstallLogStatus(idList);
    }

    @DemoDisableApi
    @OperatorLog(HostOperatorType.INSTALL_AGENT)
    @PostMapping("/install")
    @Operation(summary = "安装主机探针")
    @PreAuthorize("@ss.hasPermission('asset:host:install-agent')")
    public Boolean installAgent(@Validated @RequestBody HostAgentInstallRequest request) {
        hostAgentService.installAgent(request);
        return true;
    }

    @DemoDisableApi
    @OperatorLog(HostOperatorType.UPDATE_AGENT_INSTALL_STATUS)
    @PutMapping("/update-install-status")
    @Operation(summary = "修改探针安装状态")
    @PreAuthorize("@ss.hasPermission('asset:host:install-agent')")
    public Boolean updateAgentInstallStatus(@Validated @RequestBody HostAgentInstallStatusUpdateRequest request) {
        hostAgentLogService.updateStatus(request.getId(), request.getStatus(), request.getMessage());
        return true;
    }

    @DemoDisableApi
    @OperatorLog(HostOperatorType.UPLOAD_AGENT_RELEASE)
    @PostMapping("/upload-agent-release")
    @Operation(summary = "上传探针发布包")
    @PreAuthorize("@ss.hasPermission('asset:host:install-agent')")
    public String uploadAgentRelease(@RequestParam("file") MultipartFile file) {
        // 上传
        hostAgentService.uploadAgentRelease(file);
        // 获取最新版本
        return hostAgentService.getAgentVersion();
    }

}
