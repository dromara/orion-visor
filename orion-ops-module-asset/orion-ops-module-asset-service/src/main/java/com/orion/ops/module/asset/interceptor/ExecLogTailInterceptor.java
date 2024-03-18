package com.orion.ops.module.asset.interceptor;

import com.orion.lang.utils.Urls;
import com.orion.ops.framework.common.constant.ExtraFieldConst;
import com.orion.ops.module.asset.entity.dto.ExecLogTailDTO;
import com.orion.ops.module.asset.service.ExecService;
import lombok.extern.slf4j.Slf4j;
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
    private ExecService execService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 获取 token
        String token = Urls.getUrlSource(request.getURI().getPath());
        log.info("ExecLogTailInterceptor-beforeHandshake start token: {}", token);
        // 获取日志数据
        ExecLogTailDTO info = execService.getExecLogTailInfo(token);
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
