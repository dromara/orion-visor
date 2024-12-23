/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.interceptor;

import cn.orionsec.kit.lang.utils.Urls;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.ExtraFieldConst;
import org.dromara.visor.module.asset.entity.dto.ExecLogTailDTO;
import org.dromara.visor.module.asset.service.ExecLogService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 执行日志拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/18 17:32
 */
@Slf4j
@Component
public class ExecLogTailInterceptor implements HandshakeInterceptor {

    @Resource
    private ExecLogService execLogService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 获取 token
        String token = Urls.getUrlSource(request.getURI().getPath());
        log.info("ExecLogTailInterceptor-beforeHandshake start token: {}", token);
        // 获取日志数据
        ExecLogTailDTO info = execLogService.getExecLogTailInfo(token);
        if (info == null) {
            log.error("ExecLogTailInterceptor-beforeHandshake absent token: {}", token);
            return false;
        }
        // 保存
        attributes.put(ExtraFieldConst.INFO, info);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}
