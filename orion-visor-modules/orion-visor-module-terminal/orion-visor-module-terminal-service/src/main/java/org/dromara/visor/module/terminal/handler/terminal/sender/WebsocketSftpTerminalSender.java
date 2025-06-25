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
package org.dromara.visor.module.terminal.handler.terminal.sender;

import com.alibaba.fastjson.JSON;
import org.dromara.visor.common.enums.BooleanBit;
import org.dromara.visor.module.terminal.handler.terminal.enums.OutputProtocolEnum;
import org.dromara.visor.module.terminal.handler.terminal.model.response.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

/**
 * websocket sftp 消息发送器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/30 14:33
 */
public class WebsocketSftpTerminalSender extends AbstractWebsocketTerminalSender implements ISftpTerminalSender {

    public WebsocketSftpTerminalSender(WebSocketSession channel) {
        super(channel);
    }

    @Override
    public void sendFileList(String path, boolean result, String message, List<SftpFileVO> list) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_LIST,
                SftpListResponse.builder()
                        .path(path)
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .body(JSON.toJSONString(list))
                        .build());
    }

    @Override
    public void sendMakeDirectoryResult(boolean result, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_MKDIR,
                SftpBaseResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .build());
    }

    @Override
    public void sendTouchResult(boolean result, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_TOUCH,
                SftpBaseResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .build());
    }

    @Override
    public void sendMoveResult(boolean result, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_MOVE,
                SftpBaseResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .build());
    }

    @Override
    public void sendRemoveResult(boolean result, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_REMOVE,
                SftpBaseResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .build());
    }

    @Override
    public void sendTruncateResult(boolean result, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_TRUNCATE,
                SftpBaseResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .build());
    }

    @Override
    public void sendChangeModeResult(boolean result, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_CHMOD,
                SftpBaseResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .build());
    }

    @Override
    public void sendChangeOwnerResult(boolean result, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_CHOWN,
                SftpBaseResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .build());
    }

    @Override
    public void sendChangeGroupResult(boolean result, String message) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_CHGRP,
                SftpBaseResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .build());
    }

    @Override
    public void sendDownloadFlatDirectory(String currentPath, boolean result, String message, List<SftpFileVO> list) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_DOWNLOAD_FLAT_DIRECTORY,
                SftpDownloadFlatDirectoryResponse.builder()
                        .currentPath(currentPath)
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .body(JSON.toJSONString(list))
                        .build());
    }

    @Override
    public void sendGetContentResult(boolean result, String message, String token) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_GET_CONTENT,
                SftpGetContentResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .token(token)
                        .build());
    }

    @Override
    public void sendSetContentResult(boolean result, String message, String token) {
        this.sendFormattedMessage(OutputProtocolEnum.SFTP_SET_CONTENT,
                SftpSetContentResponse.builder()
                        .result(BooleanBit.of(result).getValue())
                        .msg(message)
                        .token(token)
                        .build());
    }

}
