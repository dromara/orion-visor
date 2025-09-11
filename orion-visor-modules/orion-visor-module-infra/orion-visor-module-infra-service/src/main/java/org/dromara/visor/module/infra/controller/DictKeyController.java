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
import org.dromara.visor.module.infra.define.operator.DictKeyOperatorType;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyCreateRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyQueryRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictKeyUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.DictKeyVO;
import org.dromara.visor.module.infra.service.DictKeyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典配置项 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Tag(name = "infra - 字典配置项服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/dict-key")
public class DictKeyController {

    @Resource
    private DictKeyService dictKeyService;

    @DemoDisableApi
    @OperatorLog(DictKeyOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建字典配置项")
    @PreAuthorize("@ss.hasPermission('infra:dict-key:create')")
    public Long createDictKey(@Validated @RequestBody DictKeyCreateRequest request) {
        return dictKeyService.createDictKey(request);
    }

    @DemoDisableApi
    @OperatorLog(DictKeyOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新字典配置项")
    @PreAuthorize("@ss.hasPermission('infra:dict-key:update')")
    public Integer updateDictKey(@Validated @RequestBody DictKeyUpdateRequest request) {
        return dictKeyService.updateDictKeyById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/list")
    @Operation(summary = "查询全部字典配置项")
    public List<DictKeyVO> getDictKeyList() {
        return dictKeyService.getDictKeyList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询全部字典配置项")
    @PreAuthorize("@ss.hasPermission('infra:dict-key:query')")
    public DataGrid<DictKeyVO> getDictKeyPage(@Validated(Page.class) @RequestBody DictKeyQueryRequest request) {
        return dictKeyService.getDictKeyPage(request);
    }

    @PutMapping("/refresh-cache")
    @Operation(summary = "刷新字典缓存")
    @PreAuthorize("@ss.hasPermission('infra:dict-key:management:refresh-cache')")
    public Boolean refreshCache() {
        dictKeyService.refreshCache();
        return true;
    }

    @DemoDisableApi
    @OperatorLog(DictKeyOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除字典配置项")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:dict-key:delete')")
    public Integer deleteDictKey(@RequestParam("id") Long id) {
        return dictKeyService.deleteDictKeyById(id);
    }

    @DemoDisableApi
    @OperatorLog(DictKeyOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除字典配置项")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:dict-key:delete')")
    public Integer batchDeleteDictKey(@RequestParam("idList") List<Long> idList) {
        return dictKeyService.deleteDictKeyByIdList(idList);
    }

}

