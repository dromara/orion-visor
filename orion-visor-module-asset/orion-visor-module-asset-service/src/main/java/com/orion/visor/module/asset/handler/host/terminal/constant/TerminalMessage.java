package com.orion.visor.module.asset.handler.host.terminal.constant;

/**
 * 终端信息常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 16:57
 */
public interface TerminalMessage {

    String CONFIG_DISABLED = "SSH configuration has been disabled.";

    String AUTHENTICATION_FAILURE = "authentication failed. please check the configuration.";

    String SERVER_UNREACHABLE = "remote server unreachable. please check the configuration.";

    String CONNECTION_FAILED = "connection failed.";

    String CONNECTION_TIMEOUT = "connection timeout.";

    String CONNECTION_CLOSED = "connection closed.";

    String FORCED_OFFLINE = "forced offline.";

}
