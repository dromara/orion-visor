package com.orion.ops.module.infra.controller;

import com.orion.ops.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.define.operator.DictKeyOperatorType;
import com.orion.ops.module.infra.entity.request.dict.DictKeyCreateRequest;
import com.orion.ops.module.infra.entity.request.dict.DictKeyUpdateRequest;
import com.orion.ops.module.infra.entity.vo.DictKeyVO;
import com.orion.ops.module.infra.service.DictKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class DictKeyController {

    @Resource
    private DictKeyService dictKeyService;

    @OperatorLog(DictKeyOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建字典配置项")
    @PreAuthorize("@ss.hasPermission('infra:dict-key:create')")
    public Long createDictKey(@Validated @RequestBody DictKeyCreateRequest request) {
        return dictKeyService.createDictKey(request);
    }

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

    @OperatorLog(DictKeyOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除字典配置项")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:dict-key:delete')")
    public Integer deleteDictKey(@RequestParam("id") Long id) {
        return dictKeyService.deleteDictKeyById(id);
    }

    @OperatorLog(DictKeyOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除字典配置项")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:dict-key:delete')")
    public Integer batchDeleteDictKey(@RequestParam("idList") List<Long> idList) {
        return dictKeyService.deleteDictKeyByIdList(idList);
    }

}

