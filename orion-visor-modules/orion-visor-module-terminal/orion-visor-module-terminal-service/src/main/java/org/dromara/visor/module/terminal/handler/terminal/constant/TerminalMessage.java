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
package org.dromara.visor.module.terminal.handler.terminal.constant;

/**
 * 终端信息常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/1/3 16:57
 */
public interface TerminalMessage {

    String CONFIG_DISABLED = "configuration has been disabled.";

    String CONNECTION_FAILED = "connection failed.";

    String CONNECTION_CLOSED = "connection closed.";

    String GUACD_SERVICE = "check your guacd service.";

    String FORCED_OFFLINE = "forced offline.";

}
