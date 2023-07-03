package com.orion.ops.framework.log.core.interceptor;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * 日志打印拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 10:36
 */
public interface LogPrinterInterceptor extends MethodInterceptor {

    String EMPTY_ARG = "<EMPTY>";

    String ERROR_ARG = "<ERROR>";

    String VOID_RES = "<VOID>";

    /**
     * 初始化
     */
    void init();

}
