/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.interceptor;

import com.orion.lang.utils.Urls;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.framework.common.meta.TraceIdHolder;
import com.orion.visor.framework.common.utils.Requests;
import com.orion.visor.module.asset.entity.dto.HostTerminalTransferDTO;
import com.orion.visor.module.asset.service.HostTerminalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 终端传输拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/27 23:53
 */
@Slf4j
@Component
public class TerminalTransferInterceptor implements HandshakeInterceptor {

    @Resource
    private HostTerminalService hostTerminalService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 获取 transferToken
        String transferToken = Urls.getUrlSource(request.getURI().getPath());
        log.info("TerminalTransferInterceptor-beforeHandshake start transferToken: {}", transferToken);
        // 获取连接数据
        HostTerminalTransferDTO transfer = hostTerminalService.getTransferInfoByToken(transferToken);
        if (transfer == null) {
            log.error("TerminalTransferInterceptor-beforeHandshake absent transferToken: {}", transferToken);
            return false;
        }
        // 设置参数
        attributes.put(ExtraFieldConst.USER_ID, transfer.getUserId());
        attributes.put(ExtraFieldConst.USERNAME, transfer.getUsername());
        attributes.put(ExtraFieldConst.TRACE_ID, TraceIdHolder.get());
        attributes.put(ExtraFieldConst.IDENTITY, Requests.getIdentity());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}
