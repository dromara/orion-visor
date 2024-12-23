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
import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.common.validator.group.Page;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.define.operator.DictValueOperatorType;
import org.dromara.visor.module.infra.entity.request.dict.DictValueCreateRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictValueQueryRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictValueRollbackRequest;
import org.dromara.visor.module.infra.entity.request.dict.DictValueUpdateRequest;
import org.dromara.visor.module.infra.entity.vo.DictValueVO;
import org.dromara.visor.module.infra.service.DictValueService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 字典配置值 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Tag(name = "infra - 字典配置值服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/dict-value")
public class DictValueController {

    @Resource
    private DictValueService dictValueService;

    @DemoDisableApi
    @OperatorLog(DictValueOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建字典配置值")
    @PreAuthorize("@ss.hasPermission('infra:dict-value:create')")
    public Long createDictValue(@Validated @RequestBody DictValueCreateRequest request) {
        return dictValueService.createDictValue(request);
    }

    @DemoDisableApi
    @OperatorLog(DictValueOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新字典配置值")
    @PreAuthorize("@ss.hasPermission('infra:dict-value:update')")
    public Integer updateDictValue(@Validated @RequestBody DictValueUpdateRequest request) {
        return dictValueService.updateDictValueById(request);
    }

    @DemoDisableApi
    @OperatorLog(DictValueOperatorType.UPDATE)
    @PutMapping("/rollback")
    @Operation(summary = "回滚字典配置值")
    @PreAuthorize("@ss.hasPermission('infra:dict-value:update')")
    public Integer rollbackDictValue(@Validated @RequestBody DictValueRollbackRequest request) {
        return dictValueService.rollbackDictValueById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询字典配置值选项")
    public Map<String, List<JSONObject>> getDictValueList(@RequestParam("keys") List<String> keys) {
        return dictValueService.getDictValueList(keys);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询字典配置值")
    @PreAuthorize("@ss.hasPermission('infra:dict-value:query')")
    public DataGrid<DictValueVO> getDictValuePage(@Validated(Page.class) @RequestBody DictValueQueryRequest request) {
        return dictValueService.getDictValuePage(request);
    }

    @DemoDisableApi
    @OperatorLog(DictValueOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除字典配置值")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:dict-value:delete')")
    public Integer deleteDictValue(@RequestParam("id") Long id) {
        return dictValueService.deleteDictValueById(id);
    }

    @DemoDisableApi
    @OperatorLog(DictValueOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除字典配置值")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:dict-value:delete')")
    public Integer batchDeleteDictValue(@RequestParam("idList") List<Long> idList) {
        return dictValueService.deleteDictValueByIdList(idList);
    }

}

