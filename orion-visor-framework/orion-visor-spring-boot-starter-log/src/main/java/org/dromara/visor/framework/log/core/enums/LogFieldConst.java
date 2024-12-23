/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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
package org.dromara.visor.framework.log.core.enums;

/**
 * 日志字段常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 11:33
 */
public interface LogFieldConst {

    String TRACE_ID = "traceId";

    String SUMMARY = "summary";

    String START = "start";

    String END = "end";

    String USED = "used";

    String HEADERS = "headers";

    String METHOD = "method";

    String URL = "url";

    String USER = "user";

    String REMOTE_ADDR = "remoteAddr";

    String METHOD_SIGN = "methodSign";

    String PARAMETER = "parameter";

    String RESPONSE = "response";

    String ERROR_DIGEST = "errorDigest";

}
