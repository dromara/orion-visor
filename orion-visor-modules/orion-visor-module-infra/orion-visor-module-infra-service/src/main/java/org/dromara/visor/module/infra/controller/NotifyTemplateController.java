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
package org.dromara.visor.module.infra.controller;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.define.operator.NotifyTemplateOperatorType;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateCreateRequest;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateQueryRequest;
import org.dromara.visor.module.infra.entity.request.notify.NotifyTemplateUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.NotifyTemplateVO;
import org.dromara.visor.module.infra.service.NotifyTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知模板 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025-9-13 21:05
 */
@Tag(name = "infra - 通知模板服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/notify-template")
public class NotifyTemplateController {

    @Resource
    private NotifyTemplateService notifyTemplateService;

    @DemoDisableApi
    @OperatorLog(NotifyTemplateOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建通知模板")
    @PreAuthorize("@ss.hasPermission('infra:notify-template:create')")
    public Long createNotifyTemplate(@Validated @RequestBody NotifyTemplateCreateRequest request) {
        return notifyTemplateService.createNotifyTemplate(request);
    }

    @DemoDisableApi
    @OperatorLog(NotifyTemplateOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新通知模板")
    @PreAuthorize("@ss.hasPermission('infra:notify-template:update')")
    public Integer updateNotifyTemplate(@Validated @RequestBody NotifyTemplateUpdateRequest request) {
        return notifyTemplateService.updateNotifyTemplateById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "查询通知模板")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:notify-template:query')")
    public NotifyTemplateVO getNotifyTemplate(@RequestParam("id") Long id) {
        return notifyTemplateService.getNotifyTemplateById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询全部通知模板")
    @Parameter(name = "bizType", description = "bizType", required = true)
    @PreAuthorize("@ss.hasPermission('infra:notify-template:query')")
    public List<NotifyTemplateVO> getNotifyTemplateList(@Validated @RequestParam("bizType") String bizType) {
        return notifyTemplateService.getNotifyTemplateListByCache(bizType);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询通知模板")
    @PreAuthorize("@ss.hasPermission('infra:notify-template:query')")
    public DataGrid<NotifyTemplateVO> getNotifyTemplatePage(@Validated(Page.class) @RequestBody NotifyTemplateQueryRequest request) {
        return notifyTemplateService.getNotifyTemplatePage(request);
    }

    @DemoDisableApi
    @OperatorLog(NotifyTemplateOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除通知模板")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:notify-template:delete')")
    public Integer deleteNotifyTemplate(@RequestParam("id") Long id) {
        return notifyTemplateService.deleteNotifyTemplateById(id);
    }

}

