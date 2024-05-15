package com.orion.visor.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import com.orion.visor.framework.common.validator.group.Page;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.infra.define.operator.OperatorLogOperatorType;
import com.orion.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import com.orion.visor.module.infra.entity.vo.OperatorLogVO;
import com.orion.visor.module.infra.service.OperatorLogService;
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
 * 操作日志 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Tag(name = "infra - 操作日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/operator-log")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class OperatorLogController {

    @Resource
    private OperatorLogService operatorLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询操作日志")
    @PreAuthorize("@ss.hasPermission('infra:operator-log:query')")
    public DataGrid<OperatorLogVO> getOperatorLogPage(@Validated(Page.class) @RequestBody OperatorLogQueryRequest request) {
        return operatorLogService.getOperatorLogPage(request);
    }

    @OperatorLog(OperatorLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除操作日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:operator-log:delete')")
    public Integer deleteOperatorLog(@RequestParam("idList") List<Long> idList) {
        return operatorLogService.deleteOperatorLog(idList);
    }

    @PostMapping("/query-count")
    @Operation(summary = "查询操作日志数量")
    @PreAuthorize("@ss.hasPermission('infra:operator-log:management:clear')")
    public Long getOperatorLogCount(@RequestBody OperatorLogQueryRequest request) {
        return operatorLogService.getOperatorLogCount(request);
    }

    @OperatorLog(OperatorLogOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清空操作日志")
    @PreAuthorize("@ss.hasPermission('infra:operator-log:management:clear')")
    public Integer clearOperatorLog(@RequestBody OperatorLogQueryRequest request) {
        return operatorLogService.clearOperatorLog(request);
    }

}

