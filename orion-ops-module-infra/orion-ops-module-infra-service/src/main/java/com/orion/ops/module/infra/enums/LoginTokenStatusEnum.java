package com.orion.ops.module.infra.enums;

import lombok.Getter;

/**
 * 登录 token 状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 16:15
 */
@Getter
public enum LoginTokenStatusEnum {

    /**
     * 正常
     */
    OK(0),

    /**
     * 已在其他设备登录
     */
    OTHER_DEVICE(1, "已在其他设备登录"),

    ;

    LoginTokenStatusEnum(Integer status) {
        this(status, null);
    }

    LoginTokenStatusEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private final Integer status;

    private final String message;

}
