package com.orion.visor.module.infra.handler.upload.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件上传响应类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/7 18:01
 */
@Getter
@AllArgsConstructor
public enum FileUploadReceiverType {

    /**
     * 请求下一块数据
     */
    NEXT("next"),

    /**
     * 上传完成
     */
    FINISH("finish"),

    /**
     * 上传失败
     */
    ERROR("error"),

    ;

    private final String type;

    public static FileUploadReceiverType of(String type) {
        if (type == null) {
            return null;
        }
        for (FileUploadReceiverType value : values()) {
            if (value.type.equals(type)) {
                return value;
            }
        }
        return null;
    }

}
