package com.orion.ops.module.infra.controller;

import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.favorite.FavoriteOperatorRequest;
import com.orion.ops.module.infra.service.FavoriteService;
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
 * 收藏 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Tag(name = "infra - 收藏服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/favorite")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    @PutMapping("/add")
    @Operation(summary = "添加收藏")
    public Long addFavorite(@Validated @RequestBody FavoriteOperatorRequest request) {
        return favoriteService.addFavorite(request);
    }

    @PutMapping("/cancel")
    @Operation(summary = "取消收藏")
    public Integer cancelFavorite(@Validated @RequestBody FavoriteOperatorRequest request) {
        return favoriteService.cancelFavorite(request);
    }

}

