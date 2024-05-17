package com.orion.visor.framework.common.web.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

import javax.servlet.Filter;

/**
 * 过滤器构造器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/25 15:05
 */
public class FilterCreator {

    private FilterCreator() {
    }

    /**
     * 创建过滤器
     *
     * @param filter filter
     * @param order  order
     * @param <T>    type
     * @return filter bean
     */
    public static <T extends Filter> FilterRegistrationBean<T> create(T filter, Integer order) {
        FilterRegistrationBean<T> bean = new FilterRegistrationBean<>(filter);
        bean.setOrder(order);
        return bean;
    }

}
