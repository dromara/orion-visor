package com.orion.ops.module.asset.controller;

import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.path.PathBookmarkCreateRequest;
import com.orion.ops.module.asset.entity.request.path.PathBookmarkUpdateRequest;
import com.orion.ops.module.asset.entity.vo.PathBookmarkVO;
import com.orion.ops.module.asset.service.PathBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
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
    public List<PathBookmarkVO> getPathBookmarkList() {
        return pathBookmarkService.getPathBookmarkList();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除路径标签")
    @Parameter(name = "id", description = "id", required = true)
    public Integer deletePathBookmark(@RequestParam("id") Long id) {
        return pathBookmarkService.deletePathBookmarkById(id);
    }

}

