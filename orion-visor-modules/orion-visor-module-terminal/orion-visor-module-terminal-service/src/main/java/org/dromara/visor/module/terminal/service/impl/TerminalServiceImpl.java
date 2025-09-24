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
package org.dromara.visor.module.terminal.service.impl;

import cn.orionsec.kit.lang.id.UUIds;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.api.DictValueApi;
import org.dromara.visor.module.terminal.define.cache.TerminalCacheKeyDefine;
import org.dromara.visor.module.terminal.entity.dto.TerminalAccessDTO;
import org.dromara.visor.module.terminal.entity.dto.TerminalTransferDTO;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalSessionAccessRequest;
import org.dromara.visor.module.terminal.entity.vo.TerminalThemeVO;
import org.dromara.visor.module.terminal.service.TerminalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 终端连接服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 14:27
 */
@Slf4j
@Service
public class TerminalServiceImpl implements TerminalService {

    private static final String THEME_DICT_KEY = "terminalTheme";

    @Resource
    private DictValueApi dictValueApi;

    @Override
    public List<TerminalThemeVO> getTerminalThemes() {
        // if (true) {
        //     String arr = "";
        //     return JSON.parseArray(arr, TerminalThemeVO.class);
        // }
        List<JSONObject> themes = dictValueApi.getDictValue(THEME_DICT_KEY);
        return themes.stream()
                .map(s -> TerminalThemeVO.builder()
                        .name(s.getString(ExtraFieldConst.LABEL))
                        .dark(s.getBoolean(ExtraFieldConst.DARK))
                        .schema(s.getJSONObject(ExtraFieldConst.VALUE))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String getTerminalAccessToken(TerminalSessionAccessRequest request) {
        LoginUser user = SecurityUtils.getLoginUserNotNull();
        log.info("TerminalService.getHostAccessToken userId: {}", user.getId());
        String accessToken = UUIds.random19();
        TerminalAccessDTO access = TerminalAccessDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .hostId(request.getHostId())
                .connectType(request.getConnectType())
                .extra(request.getExtra())
                .build();
        // 设置 access 缓存
        String key = TerminalCacheKeyDefine.TERMINAL_ACCESS.format(accessToken);
        RedisStrings.setJson(key, TerminalCacheKeyDefine.TERMINAL_ACCESS, access);
        return accessToken;
    }

    @Override
    public String getTerminalTransferToken() {
        LoginUser user = SecurityUtils.getLoginUserNotNull();
        log.info("TerminalService.getTerminalTransferToken userId: {}", user.getId());
        String transferToken = UUIds.random19();
        TerminalTransferDTO transfer = TerminalTransferDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
        // 设置 transfer 缓存
        String key = TerminalCacheKeyDefine.TERMINAL_TRANSFER.format(transferToken);
        RedisStrings.setJson(key, TerminalCacheKeyDefine.TERMINAL_TRANSFER, transfer);
        return transferToken;
    }

    @Override
    public TerminalAccessDTO getAccessInfoByToken(String token) {
        // 获取缓存
        String key = TerminalCacheKeyDefine.TERMINAL_ACCESS.format(token);
        TerminalAccessDTO access = RedisStrings.getJson(key, TerminalCacheKeyDefine.TERMINAL_ACCESS);
        // 删除缓存
        if (access != null) {
            RedisStrings.delete(key);
        }
        return access;
    }

    @Override
    public TerminalTransferDTO getTransferInfoByToken(String token) {
        // 获取缓存
        String key = TerminalCacheKeyDefine.TERMINAL_TRANSFER.format(token);
        TerminalTransferDTO transfer = RedisStrings.getJson(key, TerminalCacheKeyDefine.TERMINAL_TRANSFER);
        // 删除缓存
        if (transfer != null) {
            RedisStrings.delete(key);
        }
        return transfer;
    }

}
