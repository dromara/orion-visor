package com.orion.ops.module.asset.enums;

/**
 * 批量执行状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 17:08
 */
public enum ExecStatusEnum {

    /**
     * 等待中
     */
    WAITING,

    /**
     * 执行中
     */
    RUNNING,

    /**
     * 执行完成
     */
    COMPLETED,

    /**
     * 执行失败
     */
    FAILED,

    ;

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
