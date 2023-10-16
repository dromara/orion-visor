package com.orion.ops.module.infra.controller;

import com.orion.lang.define.wrapper.DataGrid;
import com.orion.ops.framework.common.validator.group.Page;
import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.history.HistoryValueQueryRequest;
import com.orion.ops.module.infra.entity.vo.HistoryValueVO;
import com.orion.ops.module.infra.service.HistoryValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 历史归档 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Tag(name = "infra - 历史归档服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/history-value")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class HistoryValueController {

    @Resource
    private HistoryValueService historyValueService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询历史归档")
    @PreAuthorize("@ss.hasPermission('infra:history-value:query')")
    public DataGrid<HistoryValueVO> getHistoryValuePage(@Validated(Page.class) @RequestBody HistoryValueQueryRequest request) {
        return historyValueService.getHistoryValuePage(request);
    }

}

