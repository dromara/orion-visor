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
package org.dromara.visor.module.terminal.interceptor;

import cn.orionsec.kit.lang.define.collect.MutableHashMap;
import cn.orionsec.kit.lang.utils.Urls;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.FieldConst;
import org.dromara.visor.common.trace.TraceIdHolder;
import org.dromara.visor.common.utils.Requests;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.terminal.entity.dto.TerminalAccessDTO;
import org.dromara.visor.module.terminal.handler.terminal.enums.SessionChannelEnum;
import org.dromara.visor.module.terminal.handler.terminal.enums.SessionTypeEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelExtra;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalChannelProps;
import org.dromara.visor.module.terminal.service.TerminalService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 终端访问拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/27 23:53
 */
@Slf4j
@Component
public class TerminalAccessInterceptor implements HandshakeInterceptor {

    @Resource
    private TerminalService terminalService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 设置子协议
        WebSockets.setSubProtocols(request, response);
        // 获取 accessToken
        String accessToken = Urls.getUrlSource(request.getURI().getPath());
        log.info("TerminalAccessInterceptor-beforeHandshake start accessToken: {}", accessToken);
        // 获取连接数据
        TerminalAccessDTO access = terminalService.getAccessInfoByToken(accessToken);
        if (access == null) {
            log.error("TerminalAccessInterceptor-beforeHandshake absent accessToken: {}", accessToken);
            return false;
        }
        // 设置属性
        TerminalChannelProps props = TerminalChannelProps.builder()
                .type(SessionTypeEnum.TERMINAL.name())
                .channel(SessionChannelEnum.WEB.name())
                .hostId(access.getHostId())
                .userId(access.getUserId())
                .username(access.getUsername())
                .nickname(access.getNickname())
                .traceId(TraceIdHolder.get())
                .identity(Requests.getIdentity())
                .extra(this.toExtra(access))
                .attr(new MutableHashMap<>())
                .build();
        attributes.put(FieldConst.PROPS, props);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

    /**
     * 转为 extra
     *
     * @param access access
     * @return extra
     */
    private TerminalChannelExtra toExtra(TerminalAccessDTO access) {
        TerminalChannelExtra extra;
        // 解析参数
        Map<String, Object> extraMap = access.getExtra();
        if (extraMap == null) {
            extra = new TerminalChannelExtra();
        } else {
            extra = JSON.parseObject(JSON.toJSONString(extraMap), TerminalChannelExtra.class);
        }
        extra.setConnectType(access.getConnectType());
        return extra;
    }

}
