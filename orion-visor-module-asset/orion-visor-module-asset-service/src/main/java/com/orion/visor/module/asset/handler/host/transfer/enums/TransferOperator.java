package com.orion.visor.module.asset.handler.host.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 传输操作类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:03
 */
@Getter
@AllArgsConstructor
public enum TransferOperator {

    /**
     * 开始传输
     */
    START("start"),

    /**
     * 传输完成
     */
    FINISH("finish"),

    /**
     * 传输失败
     */
    ERROR("error"),

    /**
     * 传输中断
     */
    ABORT("abort"),

    ;

    private final String type;

    public static TransferOperator of(String type) {
        if (type == null) {
            return null;
        }
        for (TransferOperator value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
