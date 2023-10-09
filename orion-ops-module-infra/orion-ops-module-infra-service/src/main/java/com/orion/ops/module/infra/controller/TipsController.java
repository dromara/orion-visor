package com.orion.ops.module.infra.controller;

import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.service.TipsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提示 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-8 17:52
 */
@Tag(name = "infra - 提示服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/tips")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class TipsController {

    @Resource
    private TipsService tipsService;

    @PutMapping("/tipped")
    @Operation(summary = "修改为已提示")
    @Parameter(name = "key", description = "key", required = true)
    public boolean tipped(@RequestParam("key") String key) {
        tipsService.tipped(key);
        return true;
    }

    @GetMapping("/get")
    @Operation(summary = "获取所有已提示的 key")
    public List<String> get() {
        return tipsService.getTippedKeys();
    }

}

