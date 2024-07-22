package com.orion.visor.module.asset.handler.host.transfer.utils;

import com.alibaba.fastjson.JSON;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.utils.Strings;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.websocket.core.utils.WebSockets;
import com.orion.visor.module.asset.handler.host.transfer.enums.TransferReceiver;
import com.orion.visor.module.asset.handler.host.transfer.model.TransferOperatorResponse;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.web.socket.WebSocketSession;

import java.util.function.Consumer;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/12 15:06
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
    public static void sendMessage(WebSocketSession channel, TransferReceiver type, Exception ex) {
        sendMessage(channel, type, ex, null);
    }

    /**
     * 发送消息
     *
     * @param channel channel
     * @param type    type
     * @param ex      ex
     * @param filler  filler
     */
    public static void sendMessage(WebSocketSession channel, TransferReceiver type, Exception ex, Consumer<TransferOperatorResponse> filler) {
        TransferOperatorResponse resp = TransferOperatorResponse.builder()
                .type(type.getType())
                .success(ex == null)
                .msg(TransferUtils.getErrorMessage(ex))
                .build();
        if (filler != null) {
            filler.accept(resp);
        }
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
        } else if (ex instanceof InvalidArgumentException || ex instanceof IllegalArgumentException) {
            String message = ex.getMessage();
            if (Strings.isBlank(message)) {
                return ErrorMessage.OPERATE_ERROR;
            }
            return message;
        } else if (ex instanceof ClientAbortException) {
            return ErrorMessage.CLIENT_ABORT;
        }
        return ErrorMessage.OPERATE_ERROR;
    }

}
