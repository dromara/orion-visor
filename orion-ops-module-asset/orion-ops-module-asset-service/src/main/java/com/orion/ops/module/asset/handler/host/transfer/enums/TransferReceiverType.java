package com.orion.ops.module.asset.handler.host.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 传输响应类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/21 22:03
 */
@Getter
@AllArgsConstructor
public enum TransferReceiverType {

    /**
     * 请求下一块上传数据
     */
    NEXT_BLOCK("nextBlock"),

    /**
     * 请求下一个传输任务
     */
    NEXT_TRANSFER("nextTransfer"),

    ;

    private final String type;

    public static TransferReceiverType of(String type) {
        if (type == null) {
            return null;
        }
        for (TransferReceiverType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
