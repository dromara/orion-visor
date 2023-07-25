package com.orion.ops.module.infra.enums;

import com.orion.ops.framework.common.constant.ErrorCode;
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
     * 0 停用
     */
    DISABLED(0),

    /**
     * 1 启用
     */
    ENABLED(1),

    /**
     * 2 锁定
     */
    LOCKED(2),

    ;

    private final Integer status;

    public static UserStatusEnum of(Integer status) {
        if (status == null) {
            return null;
        }
        for (UserStatusEnum value : values()) {
            if (value.status.equals(status)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 检查用户状态
     *
     * @param status status
     */
    public static void checkUserStatus(Integer status) {
        if (UserStatusEnum.DISABLED.getStatus().equals(status)) {
            // 禁用状态
            throw ErrorCode.USER_DISABLED.exception();
        } else if (UserStatusEnum.LOCKED.getStatus().equals(status)) {
            // 锁定状态
            throw ErrorCode.USER_LOCKED.exception();
        }
    }

}
