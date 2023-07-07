package com.orion.ops.framework.log.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.time.Dates;
import com.orion.ops.framework.common.utils.SwaggerUtils;
import com.orion.ops.framework.log.core.config.LogPrinterConfig;
import com.orion.ops.framework.log.core.enums.LogFieldConst;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 单行 日志打印拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 10:36
 */
@Slf4j
public class RowLogPrinterInterceptor extends AbstractLogPrinterInterceptor implements LogFieldConst {

    public RowLogPrinterInterceptor(LogPrinterConfig config) {
        super(config);
    }

    @Override
    protected void requestPrinter(Date startTime, String traceId, MethodInvocation invocation) {
        // http请求信息
        HttpServletRequest request = Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(s -> (ServletRequestAttributes) s)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
        Map<String, Object> fields = new LinkedHashMap<>();
        // url
        if (request != null) {
            fields.put(METHOD, Servlets.getMethod(request));
            fields.put(URL, Servlets.getRequestUrl(request));
        }
        // traceId
        fields.put(TRACE_ID, traceId);
        // 开始时间
        fields.put(START, Dates.format(startTime, Dates.YMD_HMSS));
        // api 描述
        String summary = SwaggerUtils.getOperationSummary(invocation.getMethod());
        if (!Strings.isEmpty(summary)) {
            fields.put(SUMMARY, summary);
        }
        // 登陆用户
        fields.put(USER, securityHolder.getLoginUserId());
        // http
        if (request != null) {
            // remoteAddr
            fields.put(REMOTE_ADDR, Servlets.getRemoteAddr(request));
            // header
            Map<String, Object> headers = new LinkedHashMap<>();
            Servlets.getHeaderMap(request).forEach((hk, hv) -> {
                if (headerFilter.test(hk.toLowerCase())) {
                    headers.put(hk, hv);
                }
            });
            fields.put(HEADERS, headers);
        }
        Method method = invocation.getMethod();
        // 方法签名
        fields.put(METHOD_SIGN, method.getDeclaringClass().getName() + "#" + method.getName());
        // 参数
        fields.put(PARAMETER, this.requestToString(method, invocation.getArguments()));
        log.info("api请求-开始 {}", JSON.toJSONString(fields));
        fields.clear();
    }

    @Override
    protected void responsePrinter(Date startTime, String traceId, MethodInvocation invocation, Object ret) {
        Date endTime = new Date();
        // 响应日志
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put(TRACE_ID, traceId);
        fields.put(END, Dates.format(endTime, Dates.YMD_HMSS));
        fields.put(USED, endTime.getTime() - startTime.getTime() + "ms");
        if (invocation.getMethod().getReturnType().equals(Void.TYPE)) {
            fields.put(RESPONSE, VOID_RES);
        } else {
            fields.put(RESPONSE, this.responseToString(ret));
        }
        log.info("api请求-结束 {}", JSON.toJSONString(fields));
        fields.clear();
    }

    @Override
    protected void errorPrinter(Date startTime, String traceId, Throwable throwable) {
        Date endTime = new Date();
        // 异常日志
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put(TRACE_ID, traceId);
        fields.put(END, Dates.format(endTime, Dates.YMD_HMSS));
        fields.put(USED, endTime.getTime() - startTime.getTime() + "ms");
        fields.put(ERROR_DIGEST, Exceptions.getDigest(throwable));
        log.error("api请求-异常 {}", JSON.toJSONString(fields));
        fields.clear();
    }

}
