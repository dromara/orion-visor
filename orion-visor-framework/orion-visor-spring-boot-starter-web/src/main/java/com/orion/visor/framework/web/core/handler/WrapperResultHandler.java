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
package com.orion.visor.framework.web.core.handler;

import com.orion.lang.constant.StandardContentType;
import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.define.wrapper.RpcWrapper;
import com.orion.visor.framework.common.constant.BeanOrderConst;
import com.orion.visor.framework.web.core.annotation.IgnoreWrapper;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 公共返回值包装处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/15 17:38
 */
@Order(BeanOrderConst.RESPONSE_ADVICE_WRAPPER)
@ControllerAdvice
public class WrapperResultHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, @NotNull Class converterType) {
        // 检查是否包含统一返回值注解
        if (!methodParameter.getContainingClass().isAnnotationPresent(RestWrapper.class)) {
            return false;
        }
        // 检查是否包含忽略返回值注解 && 方法返回值不为 void
        return !methodParameter.hasMethodAnnotation(IgnoreWrapper.class) &&
                methodParameter.getExecutable().getAnnotatedReturnType().getType() != Void.TYPE;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NotNull MethodParameter methodParameter, @NotNull MediaType selectedContentType, @NotNull Class selectedConverterType,
                                  @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
        HttpWrapper<?> wrapper;
        if (body instanceof HttpWrapper) {
            wrapper = (HttpWrapper<?>) body;
        } else if (body instanceof RpcWrapper) {
            wrapper = ((RpcWrapper<?>) body).toHttpWrapper();
        } else {
            wrapper = new HttpWrapper<>().data(body);
        }
        if (response instanceof ServletServerHttpResponse) {
            ((ServletServerHttpResponse) response).getServletResponse()
                    .setContentType(StandardContentType.APPLICATION_JSON_UTF8);
        }
        return wrapper;
    }

}
