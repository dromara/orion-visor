package com.orion.visor.module.asset.handler.host.transfer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 传输类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/12 12:41
 */
@Getter
@AllArgsConstructor
public enum TransferType {

    /**
     * 上传
     */
    UPLOAD("upload"),

    /**
     * 下载
     */
    DOWNLOAD("download"),

    ;

    private final String type;

    public static TransferType of(String type) {
        if (type == null) {
            return null;
        }
        for (TransferType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
