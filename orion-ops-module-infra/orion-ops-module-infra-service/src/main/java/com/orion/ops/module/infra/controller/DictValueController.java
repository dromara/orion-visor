package com.orion.ops.module.infra.controller;

import com.alibaba.fastjson.JSONObject;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.define.operator.DictValueOperatorType;
import com.orion.ops.module.infra.entity.request.dict.DictValueCreateRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueQueryRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueRollbackRequest;
import com.orion.ops.module.infra.entity.request.dict.DictValueUpdateRequest;
import com.orion.ops.module.infra.entity.vo.DictValueVO;
import com.orion.ops.module.infra.service.DictValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class DictValueController {

    @Resource
    private DictValueService dictValueService;

    @OperatorLog(DictValueOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建字典配置值")
    @PreAuthorize("@ss.hasPermission('infra:dict-value:create')")
    public Long createDictValue(@Validated @RequestBody DictValueCreateRequest request) {
        return dictValueService.createDictValue(request);
    }

    @OperatorLog(DictValueOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "更新字典配置值")
    @PreAuthorize("@ss.hasPermission('infra:dict-value:update')")
    public Integer updateDictValue(@Validated @RequestBody DictValueUpdateRequest request) {
        return dictValueService.updateDictValueById(request);
    }

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

    @OperatorLog(DictValueOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除字典配置值")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:dict-value:delete')")
    public Integer deleteDictValue(@RequestParam("id") Long id) {
        return dictValueService.deleteDictValueById(id);
    }

    @OperatorLog(DictValueOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除字典配置值")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:dict-value:delete')")
    public Integer batchDeleteDictValue(@RequestParam("idList") List<Long> idList) {
        return dictValueService.deleteDictValueByIdList(idList);
    }

}

