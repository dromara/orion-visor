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
package com.orion.visor.framework.websocket.core.constant;

/**
 * ws 服务端关闭 code
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/6/16 15:18
 */
public interface WsCloseCode {

    /**
     * code
     *
     * @return code
     */
    int getCode();

    /**
     * reason
     *
     * @return reason
     */
    String getReason();

}
