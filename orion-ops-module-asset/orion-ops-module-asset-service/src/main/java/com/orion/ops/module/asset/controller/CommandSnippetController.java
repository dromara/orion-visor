package com.orion.ops.module.asset.controller;

import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetCreateRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetQueryRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetUpdateRequest;
import com.orion.ops.module.asset.entity.vo.CommandSnippetVO;
import com.orion.ops.module.asset.service.CommandSnippetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 命令片段 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Tag(name = "asset - 命令片段服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/command-snippet")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
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
    @PostMapping("/list")
    @Operation(summary = "查询全部命令片段")
    public List<CommandSnippetVO> getCommandSnippetList(@Validated @RequestBody CommandSnippetQueryRequest request) {
        return commandSnippetService.getCommandSnippetList(request);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除命令片段")
    @Parameter(name = "id", description = "id", required = true)
    public Integer deleteCommandSnippet(@RequestParam("id") Long id) {
        return commandSnippetService.deleteCommandSnippetById(id);
    }

}

