/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.controller;

import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.entity.vo.TerminalThemeVO;
import com.orion.visor.module.asset.service.TerminalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 终端 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Tag(name = "asset - 终端服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/terminal")
public class TerminalController {

    @Resource
    private TerminalService terminalService;

    @IgnoreLog(IgnoreLogMode.ALL)
    @GetMapping("/themes")
    @Operation(summary = "获取终端主题")
    public List<TerminalThemeVO> getTerminalThemes() {
        return terminalService.getTerminalThemes();
    }

    @GetMapping("/access")
    @Operation(summary = "获取终端 accessToken")
    @PreAuthorize("@ss.hasPermission('asset:terminal:access')")
    public String getTerminalAccessToken() {
        return terminalService.getTerminalAccessToken();
    }

    @GetMapping("/transfer")
    @Operation(summary = "获取终端 transferToken")
    @PreAuthorize("@ss.hasPermission('asset:terminal:access')")
    public String getTerminalTransferToken() {
        return terminalService.getTerminalTransferToken();
    }

}

