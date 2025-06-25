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
package org.dromara.visor.module.terminal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.terminal.entity.request.snippet.CommandSnippetCreateRequest;
import org.dromara.visor.module.terminal.entity.request.snippet.CommandSnippetUpdateRequest;
import org.dromara.visor.module.terminal.entity.vo.CommandSnippetWrapperVO;
import org.dromara.visor.module.terminal.service.CommandSnippetService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 命令片段 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Tag(name = "terminal - 命令片段服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/terminal/command-snippet")
public class CommandSnippetController {

    @Resource
    private CommandSnippetService commandSnippetService;

    @PostMapping("/create")
    @Operation(summary = "创建命令片段")
    public Long createCommandSnippet(@Validated @RequestBody CommandSnippetCreateRequest request) {
        return commandSnippetService.createCommandSnippet(request);
    }

    @PutMapping("/update")
    @Operation(summary = "更新命令片段")
    public Integer updateCommandSnippet(@Validated @RequestBody CommandSnippetUpdateRequest request) {
        return commandSnippetService.updateCommandSnippetById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询全部命令片段")
    public CommandSnippetWrapperVO getCommandSnippetList() {
        return commandSnippetService.getCommandSnippet();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除命令片段")
    @Parameter(name = "id", description = "id", required = true)
    public Integer deleteCommandSnippet(@RequestParam("id") Long id) {
        return commandSnippetService.deleteCommandSnippetById(id);
    }

}

