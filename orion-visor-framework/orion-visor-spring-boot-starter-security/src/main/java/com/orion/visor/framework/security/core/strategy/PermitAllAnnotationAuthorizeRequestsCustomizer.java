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
package com.orion.visor.framework.security.core.strategy;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.security.PermitAll;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * API @PermitAll 认证策略
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @see javax.annotation.security.PermitAll
 * @since 2023/7/7 13:04
 */
public class PermitAllAnnotationAuthorizeRequestsCustomizer extends AuthorizeRequestsCustomizer {

    private final ApplicationContext applicationContext;

    public PermitAllAnnotationAuthorizeRequestsCustomizer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        // 获取匿名接口
        Map<HttpMethod, Set<String>> permitAllUrls = getPermitAllUrlsFromAnnotations();
        // @PermitAll 无需认证
        registry.antMatchers(permitAllUrls.get(null).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.GET, permitAllUrls.get(HttpMethod.GET).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.POST, permitAllUrls.get(HttpMethod.POST).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.PUT, permitAllUrls.get(HttpMethod.PUT).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.DELETE, permitAllUrls.get(HttpMethod.DELETE).toArray(new String[0])).permitAll();
    }

    /**
     * 通过注解获取所有匿名接口
     *
     * @return 匿名接口
     */
    private Map<HttpMethod, Set<String>> getPermitAllUrlsFromAnnotations() {
        Set<String> getList = new HashSet<>();
        Set<String> postList = new HashSet<>();
        Set<String> putList = new HashSet<>();
        Set<String> deleteList = new HashSet<>();
        Set<String> requestList = new HashSet<>();
        // 获取 RequestMappingHandlerMapping
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        // 获得接口对应的 HandlerMethod
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @PermitAll 注解的接口
        handlerMethodMap.forEach((mapping, method) -> {
            // 非 @PermitAll 则跳过
            if (!method.hasMethodAnnotation(PermitAll.class)) {
                return;
            }
            if (mapping.getPatternsCondition() == null) {
                return;
            }
            Set<String> urls = mapping.getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = mapping.getMethodsCondition().getMethods();
            // 为空证明为 @RequestMapping
            if (methods.isEmpty()) {
                requestList.addAll(urls);
            }
            // 根据请求方法过滤
            methods.forEach(requestMethod -> {
                switch (requestMethod) {
                    case GET:
                        getList.addAll(urls);
                        break;
                    case POST:
                        postList.addAll(urls);
                        break;
                    case PUT:
                        putList.addAll(urls);
                        break;
                    case DELETE:
                        deleteList.addAll(urls);
                        break;
                    default:
                        break;
                }
            });
        });
        // 设置返回
        Map<HttpMethod, Set<String>> result = new HashMap<>();
        result.put(HttpMethod.GET, getList);
        result.put(HttpMethod.POST, postList);
        result.put(HttpMethod.PUT, putList);
        result.put(HttpMethod.DELETE, deleteList);
        result.put(null, requestList);
        return result;
    }

}
