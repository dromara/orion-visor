package com.orion.ops.framework.websocket.core.constant;

/**
 * ws 服务端关闭 code
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/16 15:18
 */
public interface WsCloseCode {

    /**
     * code
     *
     * @return code
     */
    int getCode();

    /**
     * reason
     *
     * @return reason
     */
    String getReason();

}
