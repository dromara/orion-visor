package com.orion.ops.module.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 批量执行状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 17:08
 */
@Getter
@AllArgsConstructor
public enum ExecStatusEnum {

    /**
     * 等待中
     */
    WAITING(true),

    /**
     * 执行中
     */
    RUNNING(true),

    /**
     * 执行完成
     */
    COMPLETED(false),

    /**
     * 执行失败
     */
    FAILED(false),

    ;

    private final boolean closeable;

    public static ExecStatusEnum of(String status) {
        if (status == null) {
            return null;
        }
        for (ExecStatusEnum value : values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
