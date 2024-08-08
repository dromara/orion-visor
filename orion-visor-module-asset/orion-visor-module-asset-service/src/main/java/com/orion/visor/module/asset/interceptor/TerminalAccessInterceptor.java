package com.orion.visor.module.asset.interceptor;

import com.orion.lang.utils.Urls;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.framework.common.meta.TraceIdHolder;
import com.orion.visor.framework.common.utils.Requests;
import com.orion.visor.module.asset.entity.dto.HostTerminalAccessDTO;
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
    private HostTerminalService hostTerminalService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 获取 accessToken
        String accessToken = Urls.getUrlSource(request.getURI().getPath());
        log.info("TerminalAccessInterceptor-beforeHandshake start accessToken: {}", accessToken);
        // 获取连接数据
        HostTerminalAccessDTO access = hostTerminalService.getAccessInfoByToken(accessToken);
        if (access == null) {
            log.error("TerminalAccessInterceptor-beforeHandshake absent accessToken: {}", accessToken);
            return false;
        }
        // 设置参数
        attributes.put(ExtraFieldConst.USER_ID, access.getUserId());
        attributes.put(ExtraFieldConst.USERNAME, access.getUsername());
        attributes.put(ExtraFieldConst.TRACE_ID, TraceIdHolder.get());
        attributes.put(ExtraFieldConst.IDENTITY, Requests.getIdentity());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}
