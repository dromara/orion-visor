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
package com.orion.visor.framework.common.entity;

import java.io.Serializable;

/**
 * 请求留痕信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/2 16:23
 */
public interface RequestIdentity extends Serializable {

    /**
     * 获取请求地址
     *
     * @return address
     */
    String getAddress();

    /**
     * 获取请求位置
     *
     * @return location
     */
    String getLocation();

    /**
     * 获取请求 userAgent
     *
     * @return userAgent
     */
    String getUserAgent();

    /**
     * 设置请求地址
     *
     * @param address address
     */
    void setAddress(String address);

    /**
     * 设置请求位置
     *
     * @param location location
     */
    void setLocation(String location);

    /**
     * 设置请求 userAgent
     *
     * @param userAgent userAgent
     */
    void setUserAgent(String userAgent);

}
