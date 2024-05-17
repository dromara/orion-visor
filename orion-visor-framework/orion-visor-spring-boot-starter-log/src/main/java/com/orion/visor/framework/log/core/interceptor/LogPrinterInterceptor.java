package com.orion.visor.framework.log.core.interceptor;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * 日志打印拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/29 10:36
 */
public interface LogPrinterInterceptor extends MethodInterceptor {

    String EMPTY_TAG = "<EMPTY>";

    String ERROR_TAG = "<ERROR>";

    String VOID_TAG = "<VOID>";

    String NULL_TAG = "<NULL>";

    String IGNORED_TAG = "<IGNORED>";

    /**
     * 初始化
     */
    void init();

}
