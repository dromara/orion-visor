package com.orion.visor.framework.common.entity;

import lombok.Data;

/**
 * 请求留痕模型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 11:57
 */
@Data
public class RequestIdentityModel implements RequestIdentity {

    /**
     * 请求地址
     */
    private String address;

    /**
     * 请求位置
     */
    private String location;

    /**
     * userAgent
     */
    private String userAgent;

}
