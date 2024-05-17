package com.orion.visor.module.infra.interceptor;

import com.orion.lang.utils.Urls;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.module.infra.entity.dto.FileUploadTokenDTO;
import com.orion.visor.module.infra.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 文件上传拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/27 23:53
 */
@Slf4j
@Component
public class FileUploadInterceptor implements HandshakeInterceptor {

    @Resource
    private FileUploadService fileUploadService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 获取 uploadToken
        String uploadToken = Urls.getUrlSource(request.getURI().getPath());
        log.info("FileUploadInterceptor-beforeHandshake start uploadToken: {}", uploadToken);
        // 检查 uploadToken
        FileUploadTokenDTO info = fileUploadService.checkUploadToken(uploadToken);
        if (info == null) {
            log.error("FileUploadInterceptor-beforeHandshake absent uploadToken: {}", uploadToken);
            return false;
        }
        // 设置参数
        attributes.put(ExtraFieldConst.INFO, info);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}
