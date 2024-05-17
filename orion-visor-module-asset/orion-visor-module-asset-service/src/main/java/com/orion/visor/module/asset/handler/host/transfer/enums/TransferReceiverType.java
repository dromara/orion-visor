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
public enum TransferReceiverType {

    /**
     * 请求下一个传输任务
     */
    NEXT_TRANSFER("nextTransfer"),

    /**
     * 请求下一块上传数据
     */
    UPLOAD_NEXT_BLOCK("uploadNextBlock"),

    /**
     * 上传完成
     */
    UPLOAD_FINISH("uploadFinish"),

    /**
     * 上传失败
     */
    UPLOAD_ERROR("uploadError"),

    /**
     * 下载完成
     */
    DOWNLOAD_FINISH("downloadFinish"),

    /**
     * 下载失败
     */
    DOWNLOAD_ERROR("downloadError"),

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
