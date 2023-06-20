package com.orion.ops.framework.web.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.time.Dates;
import com.orion.ops.framework.common.meta.TraceIdHolder;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;

/**
 * 日志打印拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2022/7/14 18:25
 */
@Slf4j
@Order(10)
public class LogPrintInterceptor implements MethodInterceptor {

    // TODO @OperatorLog(bizName = "")
    // TODO 日志规格模型 SIMPLIFY FULL

    // https://blog.csdn.net/gaojie_csdn/article/details/127810618
    // 加注解

    // TODO 改为正则?
    @Value("#{'${log.interceptor.ignore.fields:}'.split(',')}")
    private String[] ignoreFields;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Date startTime = new Date();
        String traceId = TraceIdHolder.get();
        // 打印开始日志
        this.beforeLogPrint(startTime, traceId, invocation);
        try {
            // 执行方法
            Object ret = invocation.proceed();
            // 返回打印
            this.afterReturnLogPrint(startTime, traceId, ret);
            return ret;
        } catch (Throwable t) {
            // 异常打印
            this.afterThrowingLogPrint(startTime, traceId, t);
            throw t;
        }
    }

    /**
     * 方法进入打印
     *
     * @param startTime  开始时间
     * @param traceId    trace
     * @param invocation invocation
     */
    public void beforeLogPrint(Date startTime, String traceId, MethodInvocation invocation) {
        StringBuilder requestLog = new StringBuilder("\napi请求-开始-traceId: ").append(traceId).append('\n');
        // TODO 登陆用户
        // requestLog.append("\t当前用户: ").append(JSON.toJSONString(UserHolder.get())).append('\n');
        // TODO @OperatorLog
        // http请求信息
        Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(s -> (ServletRequestAttributes) s)
                .map(ServletRequestAttributes::getRequest)
                .ifPresent(request -> {
                    // url
                    requestLog.append("\t").append(Servlets.getMethod(request)).append(" ")
                            .append(Servlets.getRequestUrl(request)).append('\n');
                    // query
                    requestLog.append("\tip: ").append(Servlets.getRemoteAddr(request)).append('\n')
                            .append("\tquery: ").append(Servlets.getQueryString(request)).append('\n');
                    // header
                    Servlets.getHeaderMap(request).forEach((hk, hv) -> requestLog.append('\t')
                            .append(hk).append(": ")
                            .append(hv).append('\n'));
                });
        // 方法信息
        Method method = invocation.getMethod();
        requestLog.append("\t开始时间: ").append(Dates.format(startTime, Dates.YMD_HMSS)).append('\n')
                .append("\t方法签名: ").append(method.getDeclaringClass().getName()).append('#')
                .append(method.getName()).append("\n")
                .append("\t请求参数: ").append(this.argsToString(invocation.getArguments()));
        log.info(requestLog.toString());
    }

    /**
     * 返回打印
     *
     * @param startTime 开始时间
     * @param traceId   traceId
     * @param ret       return
     */
    private void afterReturnLogPrint(Date startTime, String traceId, Object ret) {
        Date endTime = new Date();
        // 响应日志
        StringBuilder responseLog = new StringBuilder("\napi请求-结束-traceId: ").append(traceId).append('\n');
        responseLog.append("\t结束时间: ").append(Dates.format(endTime, Dates.YMD_HMSS))
                .append(" used: ").append(endTime.getTime() - startTime.getTime()).append("ms \n")
                .append("\t响应结果: ").append(this.argsToString(ret));
        log.info(responseLog.toString());
    }

    /**
     * 异常打印
     *
     * @param startTime 开始时间
     * @param traceId   trace
     * @param throwable ex
     */
    private void afterThrowingLogPrint(Date startTime, String traceId, Throwable throwable) {
        Date endTime = new Date();
        // 响应日志
        StringBuilder responseLog = new StringBuilder("\napi请求-异常-traceId: ").append(traceId).append('\n');
        responseLog.append("\t结束时间: ").append(Dates.format(endTime, Dates.YMD_HMSS))
                .append(" used: ").append(endTime.getTime() - startTime.getTime()).append("ms \n")
                .append("\t异常摘要: ").append(Exceptions.getDigest(throwable));
        log.error(responseLog.toString());
    }

    /**
     * 参数转json
     *
     * @param o object
     * @return json
     */
    private String argsToString(Object o) {
        try {
            if (ignoreFields.length == 1 && Const.EMPTY.equals(ignoreFields[0])) {
                // 不过滤
                return JSON.toJSONString(o);
            } else {
                return JSON.toJSONString(o, (PropertyFilter) (object, name, value) -> !Arrays1.contains(ignoreFields, name));
            }
        } catch (Exception e) {
            return String.valueOf(o);
        }
    }

}
