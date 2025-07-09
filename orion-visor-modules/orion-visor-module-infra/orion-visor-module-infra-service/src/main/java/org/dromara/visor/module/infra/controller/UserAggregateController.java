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

import cn.orionsec.kit.web.servlet.web.Servlets;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.AppConst;
import org.dromara.visor.common.constant.CustomHeaderConst;
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
import javax.servlet.http.HttpServletResponse;
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
    public UserAggregateVO getUserAggregateInfo(HttpServletResponse response) {
        // 设置版本号请求头
        Servlets.addCustomHeader(response, CustomHeaderConst.APP_VERSION, AppConst.VERSION);
        // 获取用户信息
        return userAggregateService.getUserAggregateInfo();
    }

}
