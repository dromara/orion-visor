package com.orion.ops.framework.websocket.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ws服务端关闭code
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/16 15:18
 */
@AllArgsConstructor
@Getter
public enum WsCloseCode {

    /**
     * 未查询到token
     */
    INCORRECT_TOKEN(4100, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 伪造token
     */
    FORGE_TOKEN(4120, WsCloseReason.CLOSED_CONNECTION),

    /**
     * token已被绑定
     */
    TOKEN_BIND(4125, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 未知的连接
     */
    UNKNOWN_CONNECT(4130, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 认证失败 id不匹配
     */
    IDENTITY_MISMATCH(4140, WsCloseReason.IDENTITY_MISMATCH),

    /**
     * 认证信息不匹配
     */
    VALID(4150, WsCloseReason.AUTHENTICATION_FAILURE),

    /**
     * 主机不合法
     */
    INVALID_HOST(4200, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 连接远程服务器连接超时
     */
    CONNECTION_TIMEOUT(4201, WsCloseReason.CONNECTION_TIMEOUT),

    /**
     * 连接远程服务器失败
     */
    CONNECTION_FAILURE(4202, WsCloseReason.REMOTE_SERVER_UNREACHABLE),

    /**
     * 远程服务器认证失败
     */
    CONNECTION_AUTH_FAILURE(4205, WsCloseReason.REMOTE_SERVER_AUTHENTICATION_FAILURE),

    /**
     * 远程服务器认证出现异常
     */
    CONNECTION_EXCEPTION(4210, WsCloseReason.UNABLE_TO_CONNECT_REMOTE_SERVER),

    /**
     * 主机未启用
     */
    HOST_DISABLED(4215, WsCloseReason.HOST_DISABLED),

    /**
     * 打开shell出现异常
     */
    OPEN_SHELL_EXCEPTION(4220, WsCloseReason.UNABLE_TO_CONNECT_REMOTE_SERVER),

    /**
     * 打开command出现异常
     */
    OPEN_COMMAND_EXCEPTION(4225, WsCloseReason.UNABLE_TO_CONNECT_REMOTE_SERVER),

    /**
     * 打开sftp出现异常
     */
    OPEN_SFTP_EXCEPTION(4230, WsCloseReason.UNABLE_TO_CONNECT_REMOTE_SERVER),

    /**
     * 服务出现异常
     */
    RUNTIME_EXCEPTION(4300, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 心跳结束
     */
    HEART_DOWN(4310, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 用户关闭
     */
    DISCONNECT(4320, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 结束
     */
    EOF(4330, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 读取失败
     */
    READ_EXCEPTION(4335, WsCloseReason.CLOSED_CONNECTION),

    /**
     * 强制下线
     */
    FORCED_OFFLINE(4500, WsCloseReason.FORCED_OFFLINE),

    ;

    private final int code;

    private final String reason;

    public static WsCloseCode of(int code) {
        for (WsCloseCode value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

}
