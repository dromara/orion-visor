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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.entity.request.host.HostExtraQueryRequest;
import org.dromara.visor.module.asset.entity.request.host.HostExtraUpdateRequest;
import org.dromara.visor.module.asset.service.HostExtraService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 主机拓展信息 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-11 14:16
 */
@Tag(name = "asset - 主机拓展信息服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/host-extra")
public class HostExtraController {

    @Resource
    private HostExtraService hostExtraService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "获取主机拓展信息")
    @Parameter(name = "hostId", description = "hostId", required = true)
    @Parameter(name = "item", description = "item", required = true)
    public Map<String, Object> getHostExtra(@RequestParam("hostId") Long hostId, @RequestParam("item") String item) {
        return hostExtraService.getHostExtra(hostId, item);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list")
    @Operation(summary = "获取多个主机拓展信息")
    public Map<String, Map<String, Object>> getHostExtraList(@Validated @RequestBody HostExtraQueryRequest request) {
        return hostExtraService.getHostExtraList(request);
    }

    @PutMapping("/update")
    @Operation(summary = "修改主机拓展信息")
    public Integer updateHostExtra(@Validated @RequestBody HostExtraUpdateRequest request) {
        return hostExtraService.updateHostExtra(request);
    }

}

