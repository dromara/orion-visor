/*
 * Copyright (c) 2023 - present Dromara (visor.dromara.org ljh1553488six@139.com).
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
package org.dromara.visor.framework.common.entity;

import lombok.Data;

/**
 * 请求留痕模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 11:57
 */
@Data
public class RequestIdentityModel implements RequestIdentity {

    /**
     * 请求地址
     */
    private String address;

    /**
     * 请求位置
     */
    private String location;

    /**
     * userAgent
     */
    private String userAgent;

}
