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
public enum TransferOperatorType {

    /**
     * 开始上传
     */
    UPLOAD_START(TransferOperatorType.UPLOAD, "uploadStart"),

    /**
     * 上传完成
     */
    UPLOAD_FINISH(TransferOperatorType.UPLOAD, "uploadFinish"),

    /**
     * 上传失败
     */
    UPLOAD_ERROR(TransferOperatorType.UPLOAD, "uploadError"),

    /**
     * 开始下载
     */
    DOWNLOAD_START(TransferOperatorType.DOWNLOAD, "downloadStart"),

    /**
     * 中断下载
     */
    DOWNLOAD_ABORT(TransferOperatorType.DOWNLOAD, "downloadAbort"),

    ;

    public static final String UPLOAD = "UPLOAD";

    public static final String DOWNLOAD = "DOWNLOAD";

    private final String kind;

    private final String type;

    public static TransferOperatorType of(String type) {
        if (type == null) {
            return null;
        }
        for (TransferOperatorType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
