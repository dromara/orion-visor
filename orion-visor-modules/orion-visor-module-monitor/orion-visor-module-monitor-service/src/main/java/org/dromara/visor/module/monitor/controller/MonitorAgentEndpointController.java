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
package org.dromara.visor.module.monitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.CustomHeaderConst;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.ExposeApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.monitor.entity.dto.HostMetaDTO;
import org.dromara.visor.module.monitor.entity.dto.MetricsDataDTO;
import org.dromara.visor.module.monitor.service.MonitorAgentEndpointService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 监控探针端点 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/22 14:33
 */
@Tag(name = "monitor - 监控探针端点")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/monitor/agent-endpoint")
public class MonitorAgentEndpointController {

    @Resource
    private MonitorAgentEndpointService monitorAgentEndpointService;

    @ExposeApi
    @IgnoreLog(IgnoreLogMode.ALL)
    @PostMapping("/metrics")
    @Operation(summary = "上报指标数据")
    public Boolean addMetrics(@RequestHeader(CustomHeaderConst.AGENT_KEY_HEADER) String key,
                              @RequestBody MetricsDataDTO data) {
        monitorAgentEndpointService.addMetrics(key, data);
        return true;
    }

    @ExposeApi
    @PostMapping("/sync-host-meta")
    @Operation(summary = "上线时同步主机元数据")
    public Boolean syncHostMeta(@RequestHeader(CustomHeaderConst.AGENT_KEY_HEADER) String key,
                                @RequestBody HostMetaDTO data) {
        monitorAgentEndpointService.syncHostMeta(key, data);
        return true;
    }

}
