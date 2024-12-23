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
package org.dromara.visor.framework.common.utils;

import cn.orionsec.kit.web.servlet.web.Servlets;
import org.dromara.visor.framework.common.entity.RequestIdentity;
import org.dromara.visor.framework.common.entity.RequestIdentityModel;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 请求工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/2 16:26
 */
public class Requests {

    private Requests() {
    }

    /**
     * 获取请求留痕信息
     *
     * @return model
     */
    public static RequestIdentityModel getIdentity() {
        RequestIdentityModel model = new RequestIdentityModel();
        fillIdentity(model);
        return model;
    }

    /**
     * 填充请求留痕信息
     *
     * @param identity identity
     */
    public static void fillIdentity(RequestIdentity identity) {
        Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(s -> (ServletRequestAttributes) s)
                .map(ServletRequestAttributes::getRequest)
                .ifPresent(request -> {
                    String address = IpUtils.getRemoteAddr(request);
                    identity.setAddress(address);
                    identity.setLocation(IpUtils.getLocation(address));
                    identity.setUserAgent(Servlets.getUserAgent(request));
                });
    }

}
