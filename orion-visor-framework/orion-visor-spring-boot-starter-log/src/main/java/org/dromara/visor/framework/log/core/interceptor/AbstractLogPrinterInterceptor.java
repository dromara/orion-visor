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
package org.dromara.visor.framework.log.core.interceptor;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.reflect.Classes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.aopalliance.intercept.MethodInvocation;
import org.dromara.visor.common.json.FieldDesensitizeFilter;
import org.dromara.visor.common.json.FieldIgnoreFilter;
import org.dromara.visor.common.security.SecurityHolder;
import org.dromara.visor.common.trace.TraceIdHolder;
import org.dromara.visor.framework.log.configuration.config.LogPrinterConfig;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 日志打印拦截器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 10:36
 */
public abstract class AbstractLogPrinterInterceptor implements LogPrinterInterceptor {

    private static final ThreadLocal<IgnoreLogMode> IGNORE_LOG_MODE = new ThreadLocal<>();

    /**
     * 字段过滤器
     */
    protected SerializeFilter[] serializeFilters;

    /**
     * 脱敏配置
     */
    private final LogPrinterConfig config;

    /**
     * 忽略的参数
     */
    private final Map<String, boolean[]> ignoreParameter;

    @Resource
    protected SecurityHolder securityHolder;

    @Resource
    private ValueFilter desensitizeValueFilter;

    public AbstractLogPrinterInterceptor(LogPrinterConfig config) {
        this.config = config;
        this.ignoreParameter = Maps.newMap();
    }

    @Override
    public void init() {
        // 参数过滤器
        this.serializeFilters = new SerializeFilter[]{
                // 忽略字段过滤器
                new FieldIgnoreFilter(config.getField().getIgnore()),
                // 脱敏字段过滤器
                new FieldDesensitizeFilter(config.getField().getDesensitize()),
                // 脱敏字段注解过滤器
                desensitizeValueFilter
        };
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 日志忽略模式
        IgnoreLogMode ignoreLogMode = Optional.ofNullable(invocation.getMethod().getAnnotation(IgnoreLog.class))
                .map(IgnoreLog::value)
                .orElse(null);
        // 如果不打印任何日志 则直接跳出逻辑
        if (IgnoreLogMode.ALL.equals(ignoreLogMode)) {
            return invocation.proceed();
        }
        IGNORE_LOG_MODE.set(ignoreLogMode);
        Date startTime = new Date();
        String traceId = TraceIdHolder.get();
        // 打印请求日志
        this.printRequestLog(startTime, traceId, invocation);
        try {
            // 执行方法
            Object ret = invocation.proceed();
            // 打印响应日志
            this.printResponseLog(startTime, traceId, invocation, ret);
            return ret;
        } catch (Throwable t) {
            // 打印异常日志
            this.printExceptionLog(startTime, traceId, t);
            throw t;
        } finally {
            IGNORE_LOG_MODE.remove();
        }
    }

    /**
     * 获取请求头
     *
     * @param request request
     * @return headers
     */
    protected Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> headers = new LinkedHashMap<>();
        for (String headerName : config.getHeaders()) {
            String headerValue = request.getHeader(headerName);
            if (Strings.isBlank(headerValue)) {
                continue;
            }
            headers.put(headerName, headerValue);
        }
        return headers;
    }

    /**
     * 打印请求信息
     *
     * @param startTime  开始时间
     * @param traceId    traceId
     * @param invocation invocation
     */
    protected abstract void printRequestLog(Date startTime, String traceId, MethodInvocation invocation);

    /**
     * 打印响应信息
     *
     * @param startTime  开始时间
     * @param traceId    traceId
     * @param invocation invocation
     * @param ret        return
     */
    protected abstract void printResponseLog(Date startTime, String traceId, MethodInvocation invocation, Object ret);

    /**
     * 打印异常信息
     *
     * @param startTime 开始时间
     * @param traceId   traceId
     * @param throwable ex
     */
    protected abstract void printExceptionLog(Date startTime, String traceId, Throwable throwable);

    /**
     * 请求参数 json
     *
     * @param method method
     * @param args   object
     * @return json
     */
    protected String requestToString(Method method, Object[] args) {
        int length = args.length;
        if (length == 0) {
            return EMPTY_TAG;
        }
        // 忽略日志
        IgnoreLogMode ignoreLogMode = IGNORE_LOG_MODE.get();
        if (IgnoreLogMode.ARGS.equals(ignoreLogMode) || IgnoreLogMode.ARGS_RET.equals(ignoreLogMode)) {
            return IGNORED_TAG;
        }
        try {
            // 检查是否需要忽略字段
            boolean[] ignored = this.getParameterIgnoreConfig(method, args);
            int printCount = 0;
            int lastPrintIndex = 0;
            for (int i = 0; i < ignored.length; i++) {
                if (!ignored[i]) {
                    printCount++;
                    lastPrintIndex = i;
                }
            }
            if (printCount == 0) {
                // 无打印参数
                return EMPTY_TAG;
            } else if (printCount == 1) {
                // 单个打印参数
                return JSON.toJSONString(args[lastPrintIndex], serializeFilters);
            } else {
                // 多个打印参数
                JSONArray arr = new JSONArray();
                for (int i = 0; i < ignored.length; i++) {
                    if (!ignored[i]) {
                        arr.add(args[i]);
                    }
                }
                return JSON.toJSONString(arr, serializeFilters);
            }
        } catch (Exception e) {
            return ERROR_TAG;
        }
    }

    /**
     * 响应结果 json
     *
     * @param o object
     * @return json
     */
    protected String responseToString(Object o) {
        if (o == null) {
            return NULL_TAG;
        }
        // 忽略日志
        IgnoreLogMode ignoreLogMode = IGNORE_LOG_MODE.get();
        if (IgnoreLogMode.RET.equals(ignoreLogMode) || IgnoreLogMode.ARGS_RET.equals(ignoreLogMode)) {
            return IGNORED_TAG;
        }
        try {
            return JSON.toJSONString(o, serializeFilters);
        } catch (Exception e) {
            return ERROR_TAG;
        }
    }

    /**
     * 获取参数忽略字段配置
     *
     * @param method method
     * @param args   args
     * @return ignoreArr
     */
    private boolean[] getParameterIgnoreConfig(Method method, Object[] args) {
        int length = args.length;
        // 获取缓存
        String key = method.toString();
        boolean[] ignored = ignoreParameter.get(key);
        if (ignored != null) {
            return ignored;
        }
        ignored = new boolean[length];
        ignoreParameter.put(key, ignored);
        // 检查是否有忽略字段注解
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int parameterIndex = 0; parameterIndex < length; parameterIndex++) {
            boolean ignore = false;
            Annotation[] parameterAnnotations = annotations[parameterIndex];
            for (Annotation parameterAnnotation : parameterAnnotations) {
                // 需要忽略该字段
                if (parameterAnnotation.annotationType().equals(IgnoreLog.class)) {
                    ignore = true;
                    break;
                }
            }
            ignored[parameterIndex] = ignore;
        }
        // 检查是否可以序列化
        for (int i = 0; i < args.length; i++) {
            if (ignored[i]) {
                continue;
            }
            Object arg = args[i];
            // 是否为 request / response
            if (arg instanceof ServletRequest ||
                    arg instanceof ServletResponse ||
                    arg instanceof MultipartFile ||
                    arg instanceof BindingResult) {
                ignored[i] = true;
                continue;
            }
            // 是否为代理对象 (bean)
            if (Classes.isJdkProxy(arg) || Classes.isCglibProxy(arg)) {
                ignored[i] = true;
            }
        }
        return ignored;
    }

}
