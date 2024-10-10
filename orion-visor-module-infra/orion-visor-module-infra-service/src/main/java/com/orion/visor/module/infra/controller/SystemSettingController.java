package com.orion.visor.module.infra.controller;

import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.DemoDisableApi;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.infra.define.operator.SystemSettingOperatorType;
import com.orion.visor.module.infra.entity.request.system.SystemSettingUpdatePartialRequest;
import com.orion.visor.module.infra.entity.request.system.SystemSettingUpdateRequest;
import com.orion.visor.module.infra.entity.vo.AppInfoVO;
import com.orion.visor.module.infra.service.SystemSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 系统设置服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-17 11:39
 */
@Tag(name = "infra - 系统设置服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/system-setting")
public class SystemSettingController {

    @Resource
    private SystemSettingService systemSettingService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/app-info")
    @Operation(summary = "查询应用信息")
    public AppInfoVO getAppInfo() {
        return systemSettingService.getAppInfo();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/setting")
    @Operation(summary = "查询系统设置")
    @Parameter(name = "type", description = "type", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-setting:query')")
    public Map<String, Object> getSystemSettingByType(@RequestParam("type") String type) {
        return systemSettingService.getSystemSettingByType(type);
    }

    @DemoDisableApi
    @OperatorLog(SystemSettingOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新系统设置")
    @PreAuthorize("@ss.hasPermission('infra:system-setting:update')")
    public Integer updateSystemSetting(@Validated @RequestBody SystemSettingUpdateRequest request) {
        return systemSettingService.updateSystemSetting(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemSettingOperatorType.UPDATE)
    @PutMapping("/update-partial")
    @Operation(summary = "更新部分系统设置")
    @PreAuthorize("@ss.hasPermission('infra:system-setting:update')")
    public Boolean updatePartialSystemSetting(@Validated @RequestBody SystemSettingUpdatePartialRequest request) {
        systemSettingService.updatePartialSystemSetting(request);
        return true;
    }

}
