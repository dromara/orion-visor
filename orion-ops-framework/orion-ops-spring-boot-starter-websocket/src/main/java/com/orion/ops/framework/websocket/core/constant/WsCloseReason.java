package com.orion.ops.framework.websocket.core.constant;

/**
 * ws服务端关闭reason
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/16 15:21
 */
public interface WsCloseReason {

    String CLOSED_CONNECTION = "closed connection...";

    String IDENTITY_MISMATCH = "identity mismatch...";

    String AUTHENTICATION_FAILURE = "authentication failure...";

    String REMOTE_SERVER_UNREACHABLE = "remote server unreachable...";

    String CONNECTION_TIMEOUT = "connection timeout...";

    String REMOTE_SERVER_AUTHENTICATION_FAILURE = "remote server authentication failure...";

    String HOST_DISABLED = "host disabled...";

    String UNABLE_TO_CONNECT_REMOTE_SERVER = "unable to connect remote server...";

    String FORCED_OFFLINE = "forced offline...";

}
