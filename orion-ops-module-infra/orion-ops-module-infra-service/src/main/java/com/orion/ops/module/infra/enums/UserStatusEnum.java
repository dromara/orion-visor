package com.orion.ops.module.infra.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 11:35
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    /**
     * 0 正常
     */
    NORMAL(0),

    /**
     * 1 停用
     */
    DISABLED(1),

    /**
     * 2 锁定
     */
    LOCKED(2),

    ;

    private final Integer status;

}
