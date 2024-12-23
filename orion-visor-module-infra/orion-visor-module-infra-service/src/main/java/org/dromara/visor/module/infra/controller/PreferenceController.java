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
package org.dromara.visor.module.infra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.entity.request.preference.PreferenceUpdatePartialRequest;
import org.dromara.visor.module.infra.entity.request.preference.PreferenceUpdateRequest;
import org.dromara.visor.module.infra.service.PreferenceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户偏好 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-27 18:37
 */
@Tag(name = "infra - 用户偏好服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/preference")
public class PreferenceController {

    @Resource
    private PreferenceService preferenceService;

    @PutMapping("/update")
    @Operation(summary = "更新用户偏好-单个")
    public Integer updatePreference(@Validated @RequestBody PreferenceUpdateRequest request) {
        return preferenceService.updatePreference(request);
    }

    @PutMapping("/update-partial")
    @Operation(summary = "更新用户偏好-部分")
    public Boolean updatePreferencePartial(@Validated @RequestBody PreferenceUpdatePartialRequest request) {
        preferenceService.updatePreferencePartial(request);
        return true;
    }

    @GetMapping("/get")
    @Operation(summary = "查询用户偏好")
    @Parameter(name = "type", description = "type", required = true)
    @Parameter(name = "items", description = "items")
    public Map<String, Object> getPreference(@RequestParam("type") String type,
                                             @RequestParam(name = "items", required = false) List<String> items) {
        return preferenceService.getPreferenceByType(type, items);
    }

    @IgnoreLog
    @GetMapping("/get-default")
    @Operation(summary = "查询默认偏好")
    @Parameter(name = "type", description = "type", required = true)
    @Parameter(name = "items", description = "items")
    public Map<String, Object> getDefaultPreference(@RequestParam("type") String type,
                                                    @RequestParam(name = "items", required = false) List<String> items) {
        return preferenceService.getDefaultPreferenceByType(type, items);
    }

}

