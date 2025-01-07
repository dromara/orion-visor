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
package org.dromara.visor.module.asset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.asset.entity.vo.AuthorizedHostWrapperVO;
import org.dromara.visor.module.asset.entity.vo.HostIdentityVO;
import org.dromara.visor.module.asset.entity.vo.HostKeyVO;
import org.dromara.visor.module.asset.service.AssetAuthorizedDataService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产模块 授权数据服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 14:10
 */
@Tag(name = "asset - 授权数据服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/authorized-data")
public class AssetAuthorizedDataServiceController {

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/current-host")
    @Operation(summary = "查询当前用户已授权的主机")
    public AuthorizedHostWrapperVO getCurrentAuthorizedHost(@RequestParam("type") String type) {
        return assetAuthorizedDataService.getUserAuthorizedHost(SecurityUtils.getLoginUserId(), type);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/current-host-key")
    @Operation(summary = "查询当前用户已授权的主机密钥")
    public List<HostKeyVO> getCurrentAuthorizedHostKey() {
        return assetAuthorizedDataService.getUserAuthorizedHostKey(SecurityUtils.getLoginUserId());
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/current-host-identity")
    @Operation(summary = "查询当前用户已授权的主机身份")
    public List<HostIdentityVO> getCurrentAuthorizedHostIdentity() {
        return assetAuthorizedDataService.getUserAuthorizedHostIdentity(SecurityUtils.getLoginUserId());
    }

}
