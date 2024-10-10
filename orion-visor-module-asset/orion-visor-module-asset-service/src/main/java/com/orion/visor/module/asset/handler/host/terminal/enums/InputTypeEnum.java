package com.orion.visor.module.asset.handler.host.terminal.enums;

import com.alibaba.fastjson.JSONObject;
import com.orion.spring.SpringHolder;
import com.orion.visor.module.asset.handler.host.terminal.handler.*;
import com.orion.visor.module.asset.handler.host.terminal.model.TerminalBasePayload;
import com.orion.visor.module.asset.handler.host.terminal.model.request.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 输入操作类型枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 15:33
 */
public enum InputTypeEnum {

    /**
     * 主机连接检查
     */
    CHECK("ck",
            TerminalCheckHandler.class,
            new String[]{"type", "sessionId", "hostId", "connectType"},
            TerminalCheckRequest.class,
            true),

    /**
     * 连接主机
     */
    CONNECT("co",
            TerminalConnectHandler.class,
            new String[]{"type", "sessionId", "terminalType", "cols", "rows"},
            TerminalConnectRequest.class,
            true),

    /**
     * 关闭连接
     */
    CLOSE("cl",
            TerminalCloseHandler.class,
            new String[]{"type", "sessionId"},
            TerminalBasePayload.class,
            true),

    /**
     * ping
     */
    PING("p",
            TerminalPingHandler.class,
            new String[]{"type"},
            TerminalBasePayload.class,
            true),

    /**
     * SSH 修改大小
     */
    SSH_RESIZE("rs",
            SshResizeHandler.class,
            new String[]{"type", "sessionId", "cols", "rows"},
            SshResizeRequest.class,
            true),

    /**
     * SSH  输入
     */
    SSH_INPUT("i",
            SshInputHandler.class,
            new String[]{"type", "sessionId", "command"},
            SshInputRequest.class,
            false),

    /**
     * SFTP 文件列表
     */
    SFTP_LIST("ls",
            SftpListHandler.class,
            new String[]{"type", "sessionId", "showHiddenFile", "path"},
            SftpListRequest.class,
            true),

    /**
     * SFTP 创建文件夹
     */
    SFTP_MKDIR("mk",
            SftpMakeDirectoryHandler.class,
            new String[]{"type", "sessionId", "path"},
            SftpBaseRequest.class,
            true),

    /**
     * SFTP 创建文件
     */
    SFTP_TOUCH("to",
            SftpTouchHandler.class,
            new String[]{"type", "sessionId", "path"},
            SftpBaseRequest.class,
            true),

    /**
     * SFTP 移动文件
     */
    SFTP_MOVE("mv",
            SftpMoveHandler.class,
            new String[]{"type", "sessionId", "path", "target"},
            SftpMoveRequest.class,
            true),

    /**
     * SFTP 删除文件
     */
    SFTP_REMOVE("rm",
            SftpRemoveHandler.class,
            new String[]{"type", "sessionId", "path"},
            SftpBaseRequest.class,
            true),

    /**
     * SFTP 截断文件
     */
    SFTP_TRUNCATE("tc",
            SftpTruncateHandler.class,
            new String[]{"type", "sessionId", "path"},
            SftpBaseRequest.class,
            true),

    /**
     * SFTP 修改文件权限
     */
    SFTP_CHMOD("cm",
            SftpChangeModeHandler.class,
            new String[]{"type", "sessionId", "path", "mod"},
            SftpChangeModeRequest.class,
            true),

    /**
     * SFTP 下载文件夹展开文件
     */
    SFTP_DOWNLOAD_FLAT_DIRECTORY("df",
            SftpDownloadFlatDirectoryHandler.class,
            new String[]{"type", "sessionId", "currentPath", "path"},
            SftpDownloadFlatDirectoryRequest.class,
            true),

    /**
     * SFTP 获取内容
     */
    SFTP_GET_CONTENT("gc",
            SftpGetContentHandler.class,
            new String[]{"type", "sessionId", "path"},
            SftpBaseRequest.class,
            true),

    /**
     * SFTP 修改内容
     */
    SFTP_SET_CONTENT("sc",
            SftpSetContentHandler.class,
            new String[]{"type", "sessionId", "path"},
            SftpBaseRequest.class,
            true),

    ;

    private static final char SEPARATOR = '|';

    @Getter
    private final String type;

    private final Class<? extends ITerminalHandler<? extends TerminalBasePayload>> handlerBean;

    private final String[] payloadDefine;

    private final Class<? extends TerminalBasePayload> payloadClass;

    @Getter
    private final boolean asyncExec;

    @Getter
    private ITerminalHandler<? extends TerminalBasePayload> handler;

    <T extends TerminalBasePayload> InputTypeEnum(String type,
                                                  Class<? extends ITerminalHandler<T>> handlerBean,
                                                  String[] payloadDefine,
                                                  Class<T> payloadClass,
                                                  boolean asyncExec) {
        this.type = type;
        this.handlerBean = handlerBean;
        this.payloadDefine = payloadDefine;
        this.payloadClass = payloadClass;
        this.asyncExec = asyncExec;
    }

    public static InputTypeEnum of(String payload) {
        if (payload == null) {
            return null;
        }
        for (InputTypeEnum value : values()) {
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

    /**
     * 类型字段定义
     */
    @Component
    static class TypeFieldDefinition {

        @PostConstruct
        public void init() {
            for (InputTypeEnum value : InputTypeEnum.values()) {
                value.handler = SpringHolder.getBean(value.handlerBean);
            }
        }

    }

}
