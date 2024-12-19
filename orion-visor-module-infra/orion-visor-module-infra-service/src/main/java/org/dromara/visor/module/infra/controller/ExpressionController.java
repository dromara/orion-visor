/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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

import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.lang.utils.time.cron.Cron;
import cn.orionsec.kit.lang.utils.time.cron.CronSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.entity.request.exoression.CronNextRequest;
import org.dromara.visor.module.infra.entity.vo.CronNextVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表达式服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/2 16:33
 */
@Tag(name = "infra - 表达式服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/expression")
public class ExpressionController {

    @PermitAll
    @GetMapping("/cron-next")
    @Operation(summary = "获取 cron 下次执行时间")
    public CronNextVO getCronNextTime(@Validated CronNextRequest request) {
        CronNextVO next = new CronNextVO();
        try {
            Cron cron = Cron.of(request.getExpression());
            List<String> nextTime = CronSupport.getNextTime(cron, request.getTimes())
                    .stream()
                    .map(Dates::format)
                    .collect(Collectors.toList());
            next.setNext(nextTime);
            next.setValid(true);
        } catch (Exception e) {
            next.setNext(Lists.empty());
            next.setValid(false);
        }
        return next;
    }

}
