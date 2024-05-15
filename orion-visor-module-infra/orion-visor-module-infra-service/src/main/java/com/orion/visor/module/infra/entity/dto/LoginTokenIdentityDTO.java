package com.orion.visor.module.infra.entity.dto;

import com.orion.visor.framework.common.entity.RequestIdentity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 身份信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/1 1:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTokenIdentityDTO implements RequestIdentity {

    /**
     * 原始登录时间
     */
    private Long loginTime;

    /**
     * 当前设备登录地址
     */
    private String address;

    /**
     * 当前设备登录地址
     */
    private String location;

    /**
     * 当前设备 userAgent
     */
    private String userAgent;

}
