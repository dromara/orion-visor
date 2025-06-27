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
package org.dromara.visor.common.session;

import cn.orionsec.kit.lang.exception.AuthenticationException;
import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import org.dromara.visor.common.constant.Const;

/**
 * 连接消息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/11 16:30
 */
public interface SessionMessage {

    String AUTHENTICATION_FAILURE = "身份认证失败. {}";

    String SERVER_UNREACHABLE = "无法连接至服务器. {}";

    String CONNECTION_TIMEOUT = "连接服务器超时. {}";

    /**
     * 获取错误信息
     *
     * @param address address
     * @param e       e
     * @return errorMessage
     */
    static String getErrorMessage(String address, Exception e) {
        if (e == null) {
            return null;
        }
        String message = e.getMessage();
        if (Strings.contains(message, Const.TIMEOUT)) {
            // 连接超时
            return Strings.format(SessionMessage.CONNECTION_TIMEOUT, address);
        } else if (Exceptions.isCausedBy(e, AuthenticationException.class)) {
            // 认证失败
            return Strings.format(SessionMessage.AUTHENTICATION_FAILURE, address);
        } else if (Exceptions.isCausedBy(e, InvalidArgumentException.class)) {
            // 参数错误
            if (Strings.isBlank(message)) {
                return Strings.format(SessionMessage.SERVER_UNREACHABLE, address);
            } else {
                return message;
            }
        } else {
            // 其他错误
            return Strings.format(SessionMessage.SERVER_UNREACHABLE, address);
        }
    }

}
