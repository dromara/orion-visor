package com.orion.ops.framework.log.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.reflect.Annotations;
import com.orion.lang.utils.reflect.Classes;
import com.orion.ops.framework.common.annotation.IgnoreLog;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.meta.TraceIdHolder;
import com.orion.ops.framework.common.utils.Desensitizes;
import com.orion.ops.framework.log.core.config.LogPrinterConfig;
import io.swagger.v3.oas.annotations.Operation;
import org.aopalliance.intercept.MethodInvocation;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 日志打印拦截器 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 10:36
 */
public abstract class BaseLogPrinterInterceptor implements LogPrinterInterceptor {

    private static final String EMPTY_ARG = "<EMPTY>";

    private static final String ERROR_ARG = "<ERROR>";

    /**
     * 请求头过滤器
     */
    protected Predicate<String> headerFilter;

    /**
     * 字段过滤器
     */
    protected SerializeFilter[] fieldFilter;

    /**
     * api 描述
     */
    private final Map<String, String> summaryMapping;

    /**
     * 忽略的参数
     */
    private final Map<String, boolean[]> ignoreParameter;

    public BaseLogPrinterInterceptor(LogPrinterConfig config) {
        this.summaryMapping = Maps.newMap();
        this.ignoreParameter = Maps.newMap();
        this.init(config);
    }

    /**
     * 初始化
     *
     * @param config config
     */
    protected void init(LogPrinterConfig config) {
        // 请求头过滤器
        this.headerFilter = header -> config.getHeaders().contains(header);
        // 忽略字段过滤器
        PropertyFilter ignoreFilter = (Object object, String name, Object value) -> !config.getField().getIgnore().contains(name);
        // 脱敏字段过滤器
        ValueFilter desensitizeFilter = (Object object, String name, Object value) -> {
            if (config.getField().getDesensitize().contains(name)) {
                return Desensitizes.mix(Objects1.toString(value), 1, 1);
            } else {
                return value;
            }
        };
        this.fieldFilter = new SerializeFilter[]{ignoreFilter, desensitizeFilter};
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Date startTime = new Date();
        String traceId = TraceIdHolder.get();
        // 打印请求日志
        this.requestPrinter(startTime, traceId, invocation);
        try {
            // 执行方法
            Object ret = invocation.proceed();
            // 打印响应日志
            this.responsePrinter(startTime, traceId, ret);
            return ret;
        } catch (Throwable t) {
            // 打印异常日志
            this.errorPrinter(startTime, traceId, t);
            throw t;
        }
    }

    /**
     * 打印请求信息
     *
     * @param startTime  开始时间
     * @param traceId    traceId
     * @param invocation invocation
     */
    protected abstract void requestPrinter(Date startTime, String traceId, MethodInvocation invocation);

    /**
     * 打印响应信息
     *
     * @param startTime 开始时间
     * @param traceId   traceId
     * @param ret       return
     */
    protected abstract void responsePrinter(Date startTime, String traceId, Object ret);

    /**
     * 打印异常信息
     *
     * @param startTime 开始时间
     * @param traceId   traceId
     * @param throwable ex
     */
    protected abstract void errorPrinter(Date startTime, String traceId, Throwable throwable);

    /**
     * 获取 api 描述
     *
     * @param m method
     * @return summary
     */
    protected String getApiSummary(Method m) {
        // 缓存中获取描述
        String key = m.toString();
        String cache = summaryMapping.get(key);
        if (cache != null) {
            return cache;
        }
        // 获取注解描述
        Operation operation = Annotations.getAnnotation(m, Operation.class);
        String summary = Const.EMPTY;
        if (operation != null) {
            summary = operation.summary();
        }
        summaryMapping.put(key, summary);
        return summary;
    }

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
            return EMPTY_ARG;
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
                return EMPTY_ARG;
            } else if (printCount == 1) {
                // 单个打印参数
                return JSON.toJSONString(args[lastPrintIndex], fieldFilter);
            } else {
                // 多个打印参数
                JSONArray arr = new JSONArray();
                for (int i = 0; i < ignored.length; i++) {
                    if (!ignored[i]) {
                        arr.add(args[i]);
                    }
                }
                return JSON.toJSONString(arr, fieldFilter);
            }
        } catch (Exception e) {
            return ERROR_ARG;
        }
    }

    /**
     * 响应结果 json
     *
     * @param o object
     * @return json
     */
    protected String responseToString(Object o) {
        try {
            return JSON.toJSONString(o, fieldFilter);
        } catch (Exception e) {
            return ERROR_ARG;
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
            if (arg instanceof ServletRequest || arg instanceof ServletResponse) {
                ignored[i] = true;
                continue;
            }
            // 是否为代理对象 (bean)
            if (arg != null) {
                if (Classes.isJdkProxy(arg) || Classes.isCglibProxy(arg)) {
                    ignored[i] = true;
                }
            }
        }
        return ignored;
    }

}
