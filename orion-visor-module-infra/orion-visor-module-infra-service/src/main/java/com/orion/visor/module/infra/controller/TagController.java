package com.orion.visor.module.infra.controller;

import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.infra.entity.request.tag.TagCreateRequest;
import com.orion.visor.module.infra.entity.vo.TagVO;
import com.orion.visor.module.infra.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签枚举 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Tag(name = "infra - 标签枚举服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/tag")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class TagController {

    @Resource
    private TagService tagService;

    @PostMapping("/create")
    @Operation(summary = "创建标签")
    public Long createTag(@Validated @RequestBody TagCreateRequest request) {
        return tagService.createTag(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询标签")
    @Parameter(name = "type", description = "type", required = true)
    public List<TagVO> getTagList(@RequestParam("type") String type) {
        return tagService.getTagList(type);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除标签")
    @Parameter(name = "id", description = "id", required = true)
    public Integer deleteTag(@RequestParam("id") Long id) {
        return tagService.deleteTagById(id);
    }

}

