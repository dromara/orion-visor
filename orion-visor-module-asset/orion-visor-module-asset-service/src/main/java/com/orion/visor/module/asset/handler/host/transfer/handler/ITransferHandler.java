package com.orion.visor.module.asset.handler.host.transfer.handler;

import com.orion.lang.able.SafeCloseable;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;
import com.orion.visor.module.asset.handler.host.transfer.session.ITransferSession;

/**
 * 传输处理器定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:46
 */
public interface ITransferHandler extends SafeCloseable {

    /**
     * 处理文本消息
     *
     * @param payload payload
     */
    void handleMessage(TransferOperatorRequest payload);

    /**
     * 处理二进制消息
     *
     * @param content content
     */
    void handleMessage(byte[] content);

    /**
     * 通过 token 获取 session
     *
     * @param token token
     * @return session
     */
    ITransferSession getSessionByToken(String token);

}
