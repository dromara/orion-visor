/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.terminal.handler.transfer.utils;

import cn.orionsec.kit.lang.utils.Strings;
import com.alibaba.fastjson.JSON;
import org.apache.catalina.connector.ClientAbortException;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.framework.websocket.core.utils.WebSockets;
import org.dromara.visor.module.terminal.handler.transfer.enums.TransferReceiver;
import org.dromara.visor.module.terminal.handler.transfer.model.TransferOperatorResponse;
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
        } else if (ErrorMessage.isBizException(ex)) {
            // 业务异常
            String message = ex.getMessage();
            if (Strings.isBlank(message)) {
                return ErrorMessage.OPERATE_ERROR;
            }
            return message;
        } else if (ex instanceof ClientAbortException) {
            // 客户端主动断开
            return ErrorMessage.CLIENT_ABORT;
        }
        return ErrorMessage.OPERATE_ERROR;
    }

}
