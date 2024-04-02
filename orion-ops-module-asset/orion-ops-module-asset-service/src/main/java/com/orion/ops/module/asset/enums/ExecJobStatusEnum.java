package com.orion.ops.module.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 执行任务状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/28 16:25
 */
@Getter
@AllArgsConstructor
public enum ExecJobStatusEnum {

    /**
     * 停用
     */
    DISABLED(0),

    /**
     * 启用
     */
    ENABLED(1),

    ;

    private final Integer status;

    public static ExecJobStatusEnum of(Integer status) {
        if (status == null) {
            return null;
        }
        for (ExecJobStatusEnum value : values()) {
            if (value.status.equals(status)) {
                return value;
            }
        }
        return null;
    }

}
