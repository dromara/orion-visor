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
package org.dromara.visor.framework.websocket.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ws 关闭码
 * <p>
 * > 2999 && < 5000
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/31 17:41
 */
@Getter
@AllArgsConstructor
public enum WsCloseCode implements CloseCode {

    /**
     * 初始化失败
     */
    INIT_ERROR(3000, "init error"),

    /**
     * 会话已关闭
     */
    SESSION_CLOSED(3100, "session closed"),

    ;

    private final int code;

    private final String reason;

}
