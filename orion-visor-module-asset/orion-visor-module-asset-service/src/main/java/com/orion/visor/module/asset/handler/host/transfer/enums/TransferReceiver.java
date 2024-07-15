package com.orion.visor.module.asset.handler.host.transfer.enums;

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
public enum TransferReceiver {

    /**
     * 请求下一分片
     */
    NEXT_PART("nextPart"),

    /**
     * 开始
     */
    START("start"),

    /**
     * 进度
     */
    PROGRESS("progress"),

    /**
     * 完成
     */
    FINISH("finish"),

    /**
     * 失败
     */
    ERROR("error"),

    /**
     * 关闭
     */
    ABORT("abort"),

    ;

    private final String type;

    public static TransferReceiver of(String type) {
        if (type == null) {
            return null;
        }
        for (TransferReceiver value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
