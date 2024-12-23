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

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.dromara.visor.framework.common.utils.IpUtils;
import org.dromara.visor.framework.common.utils.SwaggerUtils;
import org.dromara.visor.framework.log.configuration.config.LogPrinterConfig;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;

/**
 * 美化 日志打印拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 10:36
 */
@Slf4j
public class PrettyLogPrinterInterceptor extends AbstractLogPrinterInterceptor {

    public PrettyLogPrinterInterceptor(LogPrinterConfig config) {
        super(config);
    }

    @Override
    protected void printRequestLog(Date startTime, String traceId, MethodInvocation invocation) {
        StringBuilder requestLog = new StringBuilder("\napi请求-开始\n");
        // http请求信息
        HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(s -> (ServletRequestAttributes) s)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
        // url
        if (request != null) {
            requestLog.append("\t").append(Servlets.getMethod(request)).append(" ")
                    .append(Servlets.getRequestUrl(request)).append('\n');
        }
        // traceId
        requestLog.append("\ttraceId: ").append(traceId).append("\n");
        // 开始时间
        requestLog.append("\tstart: ").append(Dates.format(startTime, Dates.YMD_HMSS)).append('\n');
        // api 描述
        String summary = SwaggerUtils.getOperationSummary(invocation.getMethod());
        if (!Strings.isEmpty(summary)) {
            requestLog.append("\tsummary: ").append(summary).append('\n');
        }
        // 登录用户
        Long loginUserId = securityHolder.getLoginUserId();
        if (loginUserId != null) {
            requestLog.append("\tuser: ").append(loginUserId).append('\n');
        }
        // http
        if (request != null) {
            // remoteAddr
            requestLog.append("\tremoteAddr: ").append(IpUtils.getRemoteAddr(request)).append('\n');
            // header
            Servlets.getHeaderMap(request).forEach((hk, hv) -> {
                if (headerFilter.test(hk.toLowerCase())) {
                    requestLog.append('\t')
                            .append(hk).append(": ")
                            .append(hv).append('\n');
                }
            });
        }
        Method method = invocation.getMethod();
        // 方法签名
        // requestLog.append("\tmethodSign: ").append(method.getDeclaringClass().getName()).append('#')
        //         .append(method.getName()).append("\n");
        // 方法参数
        requestLog.append("\tparameter: ").append(this.requestToString(method, invocation.getArguments()));
        log.info(requestLog.toString());
    }

    @Override
    protected void printResponseLog(Date startTime, String traceId, MethodInvocation invocation, Object ret) {
        Date endTime = new Date();
        // 响应日志
        StringBuilder responseLog = new StringBuilder("\napi请求-结束\n")
                .append("\ttraceId: ").append(traceId).append('\n')
                .append("\tend: ").append(Dates.format(endTime, Dates.YMD_HMSS)).append('\n')
                .append("\tused: ").append(endTime.getTime() - startTime.getTime()).append("ms \n");

        if (invocation.getMethod().getReturnType().equals(Void.TYPE)) {
            responseLog.append("\tresponse: ").append(VOID_TAG);
        } else {
            responseLog.append("\tresponse: ").append(this.responseToString(ret));
        }
        log.info(responseLog.toString());
    }

    @Override
    protected void printExceptionLog(Date startTime, String traceId, Throwable throwable) {
        Date endTime = new Date();
        // 异常日志
        StringBuilder errorLog = new StringBuilder("\napi请求-异常\n")
                .append("\ttraceId: ").append(traceId).append('\n')
                .append("\tend: ").append(Dates.format(endTime, Dates.YMD_HMSS)).append('\n')
                .append("\tused: ").append(endTime.getTime() - startTime.getTime()).append("ms \n")
                .append("\terrorDigest: ").append(Exceptions.getDigest(throwable));
        log.error(errorLog.toString());
    }

}
