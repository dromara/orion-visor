package org.dromara.visor.module.infra.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.entity.vo.SystemMenuVO;
import org.dromara.visor.module.infra.entity.vo.UserAggregateVO;
import org.dromara.visor.module.infra.service.UserAggregateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户聚合服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 11:20
 */
@Tag(name = "infra - 用户聚合服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/user-aggregate")
public class UserAggregateController {

    @Resource
    private UserAggregateService userAggregateService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/menu")
    @Operation(summary = "获取用户菜单")
    public List<SystemMenuVO> getUserMenuList() {
        return userAggregateService.getUserMenuList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/user")
    @Operation(summary = "获取用户权限聚合信息")
    public UserAggregateVO getUserAggregateInfo() {
        return userAggregateService.getUserAggregateInfo();
    }

}
