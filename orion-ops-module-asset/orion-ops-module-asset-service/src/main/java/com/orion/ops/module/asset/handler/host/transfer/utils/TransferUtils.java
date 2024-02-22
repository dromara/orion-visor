package com.orion.ops.module.asset.handler.host.transfer.utils;

import com.alibaba.fastjson.JSON;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.websocket.core.utils.WebSockets;
import com.orion.ops.module.asset.handler.host.transfer.enums.TransferReceiverType;
import com.orion.ops.module.asset.handler.host.transfer.model.TransferOperatorResponse;
import org.springframework.web.socket.WebSocketSession;

/**
 * 传输工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/22 22:14
 */
public class TransferUtils {

    private TransferUtils() {
    }

    /**
     * 发送消息
     *
     * @param channel channel
     * @param type    type
     * @param ex      ex
     */
    public static void sendMessage(WebSocketSession channel, TransferReceiverType type, Exception ex) {
        TransferOperatorResponse resp = TransferOperatorResponse.builder()
                .type(type.getType())
                .success(ex == null)
                .msg(TransferUtils.getErrorMessage(ex))
                .build();
        WebSockets.sendText(channel, JSON.toJSONString(resp));
    }

    /**
     * 获取错误信息
     *
     * @param ex ex
     * @return msg
     */
    public static String getErrorMessage(Exception ex) {
        if (ex == null) {
            return null;
        }
        if (ex instanceof InvalidArgumentException) {
            return ex.getMessage();
        }
        return ErrorMessage.OPERATE_ERROR;
    }

}
