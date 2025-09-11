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

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.CustomHeaderConst;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.ExposeApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.entity.vo.HostOnlineAgentConfigVO;
import org.dromara.visor.module.asset.service.HostAgentEndpointService;
import org.dromara.visor.module.asset.service.HostExtraService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 主机探针端点 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/22 14:33
 */
@Tag(name = "asset - 主机探针端点")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/host/agent-endpoint")
public class HostAgentEndpointController {

    @Resource
    private HostExtraService hostExtraService;

    @Resource
    private HostAgentEndpointService hostAgentEndpointService;

    @ExposeApi
    @GetMapping("/set-online")
    @Operation(summary = "设置探针已上线")
    public HostOnlineAgentConfigVO setAgentOnline(@RequestHeader(CustomHeaderConst.AGENT_KEY_HEADER) String agentKey,
                                                  @RequestHeader(CustomHeaderConst.AGENT_VERSION_HEADER) String version) {
        return hostAgentEndpointService.setAgentOnline(agentKey, version);
    }

    @ExposeApi
    @GetMapping("/set-offline")
    @Operation(summary = "设置探针已下线")
    public Boolean setAgentOffline(@RequestHeader(CustomHeaderConst.AGENT_KEY_HEADER) String agentKey) {
        hostAgentEndpointService.setAgentOffline(agentKey);
        return true;
    }

    @ExposeApi
    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/heartbeat")
    @Operation(summary = "设置探针心跳")
    public Boolean setAgentHeartbeat(@RequestHeader(CustomHeaderConst.AGENT_KEY_HEADER) String key) {
        hostAgentEndpointService.setAgentHeartbeat(key);
        return true;
    }

    @ExposeApi
    @PostMapping("/sync-host-spec")
    @Operation(summary = "同步主机规格")
    public Boolean syncHostSpec(@RequestHeader(CustomHeaderConst.AGENT_KEY_HEADER) String key,
                                @RequestParam(ExtraFieldConst.TASK_ID) String taskId,
                                @RequestBody JSONObject spec) {
        hostExtraService.syncHostSpec(key, taskId, spec);
        return true;
    }

}
