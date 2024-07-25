package com.orion.visor.module.infra.controller;

import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.infra.entity.vo.AppInfoVO;
import com.orion.visor.module.infra.service.SystemSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-17 11:39
 */
@Tag(name = "infra - 系统服务")
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

}

