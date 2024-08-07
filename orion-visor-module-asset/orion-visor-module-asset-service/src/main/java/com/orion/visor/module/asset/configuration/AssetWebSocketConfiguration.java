package com.orion.visor.module.asset.configuration;

import com.orion.visor.module.asset.handler.host.exec.log.ExecLogTailHandler;
import com.orion.visor.module.asset.handler.host.terminal.TerminalMessageDispatcher;
import com.orion.visor.module.asset.handler.host.transfer.TransferMessageDispatcher;
import com.orion.visor.module.asset.interceptor.ExecLogTailInterceptor;
import com.orion.visor.module.asset.interceptor.TerminalAccessInterceptor;
import com.orion.visor.module.asset.interceptor.TerminalTransferInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * 资产模块 websocket 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/28 11:39
 */
@Configuration
public class AssetWebSocketConfiguration implements WebSocketConfigurer {

    @Value("${orion.websocket.prefix}")
    private String prefix;

    @Resource
    private TerminalAccessInterceptor terminalAccessInterceptor;

    @Resource
    private TerminalTransferInterceptor terminalTransferInterceptor;

    @Resource
    private ExecLogTailInterceptor execLogTailInterceptor;

    @Resource
    private TerminalMessageDispatcher terminalMessageDispatcher;

    @Resource
    private TransferMessageDispatcher transferMessageDispatcher;

    @Resource
    private ExecLogTailHandler execLogTailHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 终端会话
        registry.addHandler(terminalMessageDispatcher, prefix + "/host/terminal/{accessToken}")
                .addInterceptors(terminalAccessInterceptor)
                .setAllowedOrigins("*");
        // 文件传输
        registry.addHandler(transferMessageDispatcher, prefix + "/host/transfer/{transferToken}")
                .addInterceptors(terminalTransferInterceptor)
                .setAllowedOrigins("*");
        // 执行日志
        registry.addHandler(execLogTailHandler, prefix + "/exec/log/{token}")
                .addInterceptors(execLogTailInterceptor)
                .setAllowedOrigins("*");
    }

}
