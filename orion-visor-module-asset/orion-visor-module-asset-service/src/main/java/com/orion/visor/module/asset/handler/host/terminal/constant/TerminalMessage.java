/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
