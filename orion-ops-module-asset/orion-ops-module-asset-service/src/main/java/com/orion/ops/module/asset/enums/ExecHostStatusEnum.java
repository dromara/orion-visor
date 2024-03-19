package com.orion.ops.module.asset.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 批量执行主机状态
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 17:08
 */
@Getter
@AllArgsConstructor
public enum ExecHostStatusEnum {

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

    /**
     * 执行超时
     */
    TIMEOUT(false),

    /**
     * 中断执行
     */
    INTERRUPTED(false),

    ;

    private final boolean closeable;

    public static final List<String> CLOSEABLE_STATUS = Arrays.stream(ExecHostStatusEnum.values())
            .filter(ExecHostStatusEnum::isCloseable)
            .map(Enum::name)
            .collect(Collectors.toList());

    public static ExecHostStatusEnum of(String status) {
        if (status == null) {
            return null;
        }
        for (ExecHostStatusEnum value : values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
