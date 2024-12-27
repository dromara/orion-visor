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
package org.dromara.visor.module.asset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.entity.vo.AssetWorkplaceStatisticsVO;
import org.dromara.visor.module.asset.service.AssetStatisticsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * asset - 统计服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/12/23 16:07
 */
@Tag(name = "asset - 统计服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/statistics")
public class AssetStatisticsController {

    @Resource
    private AssetStatisticsService assetStatisticsService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-workplace")
    @Operation(summary = "查询工作台统计信息")
    public AssetWorkplaceStatisticsVO getWorkplaceStatisticsData() {
        return assetStatisticsService.getWorkplaceStatisticsData();
    }

}
