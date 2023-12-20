package com.orion.ops.module.asset.controller;

import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.host.HostAliasUpdateRequest;
import com.orion.ops.module.asset.service.HostExtraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HostExtraController {

    @Resource
    private HostExtraService hostExtraService;

    @PutMapping("/update-alias")
    @Operation(summary = "修改主机别名")
    public Integer updateHostAlias(@Validated @RequestBody HostAliasUpdateRequest request) {
        return hostExtraService.updateHostAlias(request);
    }

}

