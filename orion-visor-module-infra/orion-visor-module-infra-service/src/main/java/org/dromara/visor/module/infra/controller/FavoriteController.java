/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.infra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.entity.request.favorite.FavoriteOperatorRequest;
import org.dromara.visor.module.infra.service.FavoriteService;
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

