package com.orion.visor.module.asset.handler.host.transfer.session;

import com.orion.lang.able.SafeCloseable;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorRequest;

/**
 * 主机传输会话定义
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 23:06
 */
public interface ITransferSession extends SafeCloseable {

    /**
     * 初始化
     */
    void init();

    /**
     * 处理二进制内容
     *
     * @param bytes bytes
     */
    void handleBinary(byte[] bytes);

    /**
     * 开始传输
     *
     * @param request request
     */
    void onStart(TransferOperatorRequest request);

    /**
     * 传输完成
     *
     * @param request request
     */
    void onFinish(TransferOperatorRequest request);

    /**
     * 传输失败
     *
     * @param request request
     */
    void onError(TransferOperatorRequest request);

    /**
     * 传输中断
     *
     * @param request request
     */
    void onAbort(TransferOperatorRequest request);

    /**
     * 获取文件路径
     *
     * @return path
     */
    String getPath();

    /**
     * 获取 token
     *
     * @return token
     */
    String getToken();

    /**
     * 设置 token
     *
     * @param token token
     */
    void setToken(String token);

    /**
     * 获取 hostId
     *
     * @return hostId
     */
    Long getHostId();

}
