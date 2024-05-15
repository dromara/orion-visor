package com.orion.ops.module.infra.controller;

import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.infra.entity.request.message.SystemMessageQueryRequest;
import com.orion.ops.module.infra.entity.vo.SystemMessageVO;
import com.orion.ops.module.infra.service.SystemMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 系统消息 api
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Tag(name = "infra - 系统消息服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/system-message")
@SuppressWarnings({"ELValidationInJSP", "SpringElInspection"})
public class SystemMessageController {

    @Resource
    private SystemMessageService systemMessageService;

    @IgnoreLog(IgnoreLogMode.ALL)
    @PostMapping("/list")
    @Operation(summary = "查询系统消息列表")
    public List<SystemMessageVO> getSystemMessageList(@RequestBody SystemMessageQueryRequest request) {
        return systemMessageService.getSystemMessageList(request);
    }

    @IgnoreLog(IgnoreLogMode.ALL)
    @GetMapping("/count")
    @Operation(summary = "查询系统消息数量")
    @Parameter(name = "queryUnread", description = "queryUnread", required = true)
    public Map<String, Integer> getSystemMessageCount(@RequestParam("queryUnread") Boolean queryUnread) {
        return systemMessageService.getSystemMessageCount(queryUnread);
    }

    @IgnoreLog(IgnoreLogMode.ALL)
    @GetMapping("/has-unread")
    @Operation(summary = "查询是否有未读消息")
    public Boolean checkHasUnreadMessage() {
        return systemMessageService.checkHasUnreadMessage();
    }

    @PutMapping("/read")
    @Operation(summary = "更新系统消息为已读")
    @Parameter(name = "id", description = "id", required = true)
    public Integer readSystemMessage(@RequestParam("id") Long id) {
        return systemMessageService.readSystemMessage(id);
    }

    @PutMapping("/read-all")
    @Operation(summary = "更新全部系统消息为已读")
    @Parameter(name = "classify", description = "classify", required = true)
    public Integer readAllSystemMessage(@RequestParam("classify") String classify) {
        return systemMessageService.readAllSystemMessage(classify);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除系统消息")
    @Parameter(name = "id", description = "id", required = true)
    public Integer deleteSystemMessage(@RequestParam("id") Long id) {
        return systemMessageService.deleteSystemMessageById(id);
    }

    @DeleteMapping("/clear")
    @Operation(summary = "清理已读的系统消息")
    @Parameter(name = "classify", description = "classify", required = true)
    public Integer clearSystemMessage(@RequestParam("classify") String classify) {
        return systemMessageService.clearSystemMessage(classify);
    }

}

