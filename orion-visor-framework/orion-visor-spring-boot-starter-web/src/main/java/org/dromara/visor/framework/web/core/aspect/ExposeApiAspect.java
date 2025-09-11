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
package org.dromara.visor.framework.web.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.dromara.visor.common.constant.BeanOrderConst;
import org.dromara.visor.common.constant.ErrorCode;
import org.dromara.visor.framework.web.configuration.config.ExposeApiConfig;
import org.dromara.visor.framework.web.core.annotation.ExposeApi;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 对外服务 api 切面
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/22 16:52
 */
@Slf4j
@Aspect
@Order(BeanOrderConst.EXPOSE_API_ASPECT)
public class ExposeApiAspect {

    private final ExposeApiConfig config;

    public ExposeApiAspect(ExposeApiConfig config) {
        this.config = config;
    }

    @Pointcut("@annotation(e)")
    public void exposeApi(ExposeApi e) {
    }

    @Before(value = "exposeApi(e)", argNames = "e")
    public void beforeExposeApi(ExposeApi e) {
        // 获取请求
        HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(s -> (ServletRequestAttributes) s)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
        if (request == null) {
            throw ErrorCode.EXPOSE_UNAUTHORIZED.exception();
        }
        // 验证对外服务参数
        if (!config.getToken().equals(request.getHeader(config.getHeader()))) {
            log.warn("expose api unauthorized, url: {}", request.getRequestURI());
            throw ErrorCode.EXPOSE_UNAUTHORIZED.exception();
        }
    }

}
