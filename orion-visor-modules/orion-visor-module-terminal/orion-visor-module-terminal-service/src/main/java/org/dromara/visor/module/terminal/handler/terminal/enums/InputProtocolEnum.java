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
package org.dromara.visor.module.terminal.handler.terminal.enums;

import cn.orionsec.kit.spring.SpringHolder;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.dromara.visor.module.terminal.handler.terminal.handler.*;
import org.dromara.visor.module.terminal.handler.terminal.model.TerminalBasePayload;
import org.dromara.visor.module.terminal.handler.terminal.model.request.*;
import org.dromara.visor.module.terminal.handler.terminal.sender.ITerminalSender;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 输入协议枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:33
 */
public enum InputProtocolEnum {

    /**
     * 请求连接
     */
    CONNECT("co",
            TerminalConnectHandler.class,
            new String[]{"type", "body"},
            TerminalConnectRequest.class),

    /**
     * 关闭连接
     */
    CLOSE("cl",
            TerminalCloseHandler.class,
            new String[]{"type"},
            TerminalBasePayload.class),

    /**
     * ping
     */
    PING("p",
            TerminalPingHandler.class,
            new String[]{"type"},
            TerminalBasePayload.class),

    /**
     * 修改大小
     */
    RESIZE("rs",
            TerminalResizeHandler.class,
            new String[]{"type", "width", "height"},
            TerminalResizeRequest.class),

    // ----------------------- guacd ----------------------

    /**
     * guacd 指令
     */
    GUACD_INSTRUCTION("gi",
            GuacdInstructionHandler.class,
            new String[]{"type", "instruction"},
            GuacdInstructionRequest.class),

    // ----------------------- ssh ----------------------

    /**
     * SSH  输入
     */
    SSH_INPUT("i",
            SshInputHandler.class,
            new String[]{"type", "command"},
            SshInputRequest.class),

    // ----------------------- sftp ----------------------

    /**
     * SFTP 文件列表
     */
    SFTP_LIST("ls",
            SftpListHandler.class,
            new String[]{"type", "showHiddenFile", "path"},
            SftpListRequest.class),

    /**
     * SFTP 创建文件夹
     */
    SFTP_MKDIR("mk",
            SftpMakeDirectoryHandler.class,
            new String[]{"type", "path"},
            SftpBaseRequest.class),

    /**
     * SFTP 创建文件
     */
    SFTP_TOUCH("to",
            SftpTouchHandler.class,
            new String[]{"type", "path"},
            SftpBaseRequest.class),

    /**
     * SFTP 移动文件
     */
    SFTP_MOVE("mv",
            SftpMoveHandler.class,
            new String[]{"type", "path", "target"},
            SftpMoveRequest.class),

    /**
     * SFTP 删除文件
     */
    SFTP_REMOVE("rm",
            SftpRemoveHandler.class,
            new String[]{"type", "path"},
            SftpBaseRequest.class),

    /**
     * SFTP 截断文件
     */
    SFTP_TRUNCATE("tc",
            SftpTruncateHandler.class,
            new String[]{"type", "path"},
            SftpBaseRequest.class),

    /**
     * SFTP 修改文件权限
     */
    SFTP_CHMOD("chm",
            SftpChangeModeHandler.class,
            new String[]{"type", "path", "mod"},
            SftpChangeModeRequest.class),

    /**
     * SFTP 修改文件归属
     */
    SFTP_CHOWN("cho",
            SftpChangeOwnerHandler.class,
            new String[]{"type", "path", "uid"},
            SftpChangeOwnerRequest.class),

    /**
     * SFTP 修改文件分组
     */
    SFTP_CHGRP("chg",
            SftpChangeGroupHandler.class,
            new String[]{"type", "path", "gid"},
            SftpChangeGroupRequest.class),

    /**
     * SFTP 下载文件夹展开文件
     */
    SFTP_DOWNLOAD_FLAT_DIRECTORY("df",
            SftpDownloadFlatDirectoryHandler.class,
            new String[]{"type", "currentPath", "path"},
            SftpDownloadFlatDirectoryRequest.class),

    /**
     * SFTP 获取内容
     */
    SFTP_GET_CONTENT("gc",
            SftpGetContentHandler.class,
            new String[]{"type", "path"},
            SftpBaseRequest.class),

    /**
     * SFTP 修改内容
     */
    SFTP_SET_CONTENT("sc",
            SftpSetContentHandler.class,
            new String[]{"type", "path"},
            SftpBaseRequest.class),

    // ----------------------- rdp ----------------------

    /**
     * RDP 文件系统事件
     */
    RDP_FILE_SYSTEM_EVENT("fse",
            RdpFileSystemEventHandler.class,
            new String[]{"type", "event"},
            RdpFileSystemEventRequest.class),

    ;

    private static final char SEPARATOR = '|';

    @Getter
    private final String type;

    private final Class<? extends ITerminalHandler<? extends ITerminalSender, ? extends TerminalBasePayload>> handlerBean;

    private final String[] payloadDefine;

    private final Class<? extends TerminalBasePayload> payloadClass;

    private ITerminalHandler<? extends ITerminalSender, ? extends TerminalBasePayload> handler;

    <S extends ITerminalSender, T extends TerminalBasePayload> InputProtocolEnum(String type,
                                                                                 Class<? extends ITerminalHandler<S, T>> handlerBean,
                                                                                 String[] payloadDefine,
                                                                                 Class<T> payloadClass) {
        this.type = type;
        this.handlerBean = handlerBean;
        this.payloadDefine = payloadDefine;
        this.payloadClass = payloadClass;
    }

    public static InputProtocolEnum of(String payload) {
        if (payload == null) {
            return null;
        }
        for (InputProtocolEnum value : values()) {
            if (payload.startsWith(value.type + SEPARATOR) || payload.equals(value.type)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 解析请求
     *
     * @param payload payload
     * @param <T>     T
     * @return payload
     */
    @SuppressWarnings("unchecked")
    public <T extends TerminalBasePayload> T parse(String payload) {
        JSONObject object = new JSONObject();
        int curr = 0;
        int len = payload.length();
        for (int i = 0, pl = payloadDefine.length; i < pl; i++) {
            if (i == pl - 1) {
                // 最后一次
                object.put(payloadDefine[i], payload.substring(curr, len));
            } else {
                // 非最后一次
                StringBuilder tmp = new StringBuilder();
                for (; curr < len; curr++) {
                    char c = payload.charAt(curr);
                    if (c == SEPARATOR) {
                        object.put(payloadDefine[i], tmp.toString());
                        curr++;
                        break;
                    } else {
                        tmp.append(c);
                    }
                }
            }
        }
        return (T) object.toJavaObject(payloadClass);
    }

    @SuppressWarnings("unchecked")
    public <S extends ITerminalSender, T extends TerminalBasePayload> ITerminalHandler<S, T> getHandler() {
        return (ITerminalHandler<S, T>) handler;
    }

    /**
     * 类型字段定义
     */
    @Component
    static class TypeFieldDefinition {

        @PostConstruct
        public void init() {
            for (InputProtocolEnum value : InputProtocolEnum.values()) {
                value.handler = SpringHolder.getBean(value.handlerBean);
            }
        }

    }

}
