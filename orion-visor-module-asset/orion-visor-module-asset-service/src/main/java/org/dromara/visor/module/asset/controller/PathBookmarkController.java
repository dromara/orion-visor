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
package org.dromara.visor.module.asset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.entity.request.path.PathBookmarkCreateRequest;
import org.dromara.visor.module.asset.entity.request.path.PathBookmarkUpdateRequest;
import org.dromara.visor.module.asset.entity.vo.PathBookmarkWrapperVO;
import org.dromara.visor.module.asset.service.PathBookmarkService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 路径标签 api
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-23 23:15
 */
@Tag(name = "asset - 路径标签服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/path-bookmark")
public class PathBookmarkController {

    @Resource
    private PathBookmarkService pathBookmarkService;

    @PostMapping("/create")
    @Operation(summary = "创建路径标签")
    public Long createPathBookmark(@Validated @RequestBody PathBookmarkCreateRequest request) {
        return pathBookmarkService.createPathBookmark(request);
    }

    @PutMapping("/update")
    @Operation(summary = "更新路径标签")
    public Integer updatePathBookmark(@Validated @RequestBody PathBookmarkUpdateRequest request) {
        return pathBookmarkService.updatePathBookmarkById(request);
    }

    @GetMapping("/list")
    @Operation(summary = "查询全部路径标签")
    public PathBookmarkWrapperVO getPathBookmarkList() {
        return pathBookmarkService.getPathBookmark();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除路径标签")
    @Parameter(name = "id", description = "id", required = true)
    public Integer deletePathBookmark(@RequestParam("id") Long id) {
        return pathBookmarkService.deletePathBookmarkById(id);
    }

}

