package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdatePartialRequest;
import com.orion.ops.module.infra.entity.request.preference.PreferenceUpdateRequest;
import com.orion.ops.module.infra.service.PreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
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
    public HttpWrapper<?> updatePreferencePartial(@Validated @RequestBody PreferenceUpdatePartialRequest request) {
        preferenceService.updatePreferencePartial(request);
        return HttpWrapper.ok();
    }

    @GetMapping("/get")
    @Operation(summary = "查询用户偏好")
    @Parameter(name = "type", description = "type", required = true)
    public Map<String, Object> getPreference(@RequestParam("type") String type) {
        return preferenceService.getPreferenceByType(type);
    }

}

