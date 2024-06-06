package com.orion.visor.module.asset.handler.host.transfer.manager;

import com.orion.visor.module.asset.handler.host.transfer.handler.ITransferHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 主机传输管理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/6/4 17:58
 */
@Component
public class HostTransferManager {

    private final ConcurrentHashMap<String, ITransferHandler> handlers = new ConcurrentHashMap<>();

    /**
     * 添加处理器
     *
     * @param id      id
     * @param handler handler
     */
    public void putHandler(String id, ITransferHandler handler) {
        handlers.put(id, handler);
    }

    /**
     * 获取处理器
     *
     * @param id id
     * @return handler
     */
    public ITransferHandler getHandler(String id) {
        return handlers.get(id);
    }

    /**
     * 删除处理器
     *
     * @param id id
     * @return handler
     */
    public ITransferHandler removeHandler(String id) {
        return handlers.remove(id);
    }

}
