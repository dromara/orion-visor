package com.orion.ops.framework.common.entity;

import java.io.Serializable;

/**
 * 请求留痕
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/2 16:23
 */
public interface RequestIdentity extends Serializable {

    /**
     * 设置请求地址
     *
     * @param address address
     */
    void setAddress(String address);

    /**
     * 设置请求位置
     *
     * @param location location
     */
    void setLocation(String location);

    /**
     * 设置请求 userAgent
     *
     * @param userAgent userAgent
     */
    void setUserAgent(String userAgent);

}
