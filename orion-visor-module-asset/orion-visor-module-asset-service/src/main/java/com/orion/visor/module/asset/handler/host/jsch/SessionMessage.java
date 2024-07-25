package com.orion.visor.module.asset.handler.host.jsch;

/**
 * 连接消息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/11 16:30
 */
public interface SessionMessage {

    String AUTHENTICATION_FAILURE = "authentication failed. please check the configuration. - {}";

    String SERVER_UNREACHABLE = "remote server unreachable. please check the configuration. - {}";

    String CONNECTION_TIMEOUT = "connection timeout. - {}";

}
