package com.orion.visor.module.asset.handler.host.transfer.handler;

import com.orion.lang.able.SafeCloseable;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import com.orion.visor.module.asset.handler.host.transfer.session.IDownloadSession;

import java.util.Map;

/**
 * 传输处理器定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:46
 */
public interface ITransferHandler extends SafeCloseable {

    /**
     * 处理消息
     *
     * @param payload payload
     */
    void handleMessage(TransferOperatorRequest payload);

    /**
     * 写入内容
     *
     * @param content content
     */
    void putContent(byte[] content);

    /**
     * 获取 token sessions
     *
     * @return token sessions
     */
    Map<String, IDownloadSession> getTokenSessions();

}
